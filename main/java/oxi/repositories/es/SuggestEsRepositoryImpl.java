package oxi.repositories.es;

import oxi.models.dto.es.*;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.lang.NumberFormatException;

import javax.persistence.Transient;

import org.springframework.data.jpa.repository.*;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
//import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder.Contexts2x;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.search.suggest.completion.context.CategoryQueryContext;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.common.xcontent.ToXContent.MapParams;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
//import org.elasticsearch.search.internal.InternalSearchHit;  //Folded into its interface
import org.elasticsearch.search.internal.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.*;
import org.elasticsearch.common.document.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class SuggestEsRepositoryImpl implements SuggestEsRepositoryCustom{

	@Transient
	private static final Logger logger = LogManager.getLogger(SuggestEsRepositoryImpl.class);
	private SuggestionBuilder termSuggestionBuilder;
	private CompletionSuggestionBuilder completionSuggestionBuilder;

	@Autowired RestHighLevelClient client;

	@Override
	public List<SizeLabelEsDto> sizeLabelSuggest(String prefix, String context){
		return null;
	}


	@Override
	public List<SuggestItemEsDto> itemSuggest(String prefix, String context){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = "retailer_name";
		final String SUGGEST_NAME = "item-suggest-1";

		Map<String, String> inclusionProps;
		//BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(field, ter));
		//termSuggestionBuilder = new TermSuggestionBuilder(FIELD).text(prefix);
		Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().setCategory(context).build()));
		
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix)
			.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
			.fetchSource(new String[]{
				"product.featuredImage.originalSrc", 
				"product.handle", 
				"product.description", 
				"product.variants", 
				"product.vendor"
			}, null)
			.suggest(suggestionBuilder);

		SearchRequest searchRequest = new SearchRequest(new String[]{"item"}, searchSourceBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestItemEsDto> suggestedItems = new ArrayList<SuggestItemEsDto>();

		try{
			searchResponse = client.search(searchRequest, null);

			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());

			//XContentBuilder jsonContentBuilder = JsonXContent.contentBuilder();
			//inclusionProps = new HashMap<String, String>();
			//inclusionProps.put("", "");
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			//XContentBuilder xContentBuilder = searchResponse.getSuggest().getSuggestion(CONTEXT_CAT)
			//	.iterator().next().getOptions()
			//	.iterator().next().toXContent(JsonXContent.contentBuilder(), new ToXContent.MapaParams(inclusionProps));

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){
				//log the suggested text from provided prefix
				logger.debug("suggestion text: " + entry.getText().toString());
				//logger.debug("suggestion options: " + entry.getOption())

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					//Map<String, Object> wtf = option.getPayloadAsMap();
					SearchHit hit = option.getHit();

					if(hit != null){
						//Map<String, SearchHits> innerHits = hit.getInnerHits();
						suggestedItems.add(new SuggestItemEsDto(hit.getId(), hit.getSourceAsMap()));
						Map<String, Object> source = hit.getSourceAsMap();
						logger.debug("source = " + source.toString());
						SearchHit sourceHit = SearchHit.createFromMap(source);

						//=========================== dubuging ===========================
						
						if(sourceHit != null){
							logger.debug("sourceHit = " + sourceHit.toString());
							Map<String, SearchHits> innerHits = sourceHit.getInnerHits();
							DocumentField product = sourceHit.field("product");

							if(product != null){
								logger.debug("product = " + product.toString());
								Iterator<Object> itr = product.iterator();
								int index = 0;

								while(itr.hasNext()){
									Object value = itr.next();
									logger.debug("product[" + index + "]" + (value != null ? value.toString() : "null"));
									index++;
								}
							}
							else{
								logger.debug("product is null");
							}

							logger.debug("sourceHit = ", sourceHit.toString());

							// Todo: Clean this up.
							if(innerHits != null){
								SearchHits products = innerHits.get("products");
								SearchHits picture = innerHits.get("picture");

								if(products != null){

									if(products.iterator().hasNext()){

										SearchHit prod = products.iterator().next();

										if(prod != null){
											DocumentField picUrl = prod.field("onlineStorePreviewUrl");

											if(picUrl != null){
												logger.debug("picUrl = " + picUrl.getValue());
											}
											else{
												logger.debug("picUrl is null");
											}
										}
										else{
											logger.debug("prod is null");
										}
									}
									else{
										logger.debug("product.iterator().hasNext() is false");
									}
								}
								else{
									logger.debug("product is null.");
								}
							}
							else{
								logger.debug("innerHits is null");
							}
							//logger.debug("preview picture: " + sourceHit.getInnerHits().get("product").iterator().next().field("onlineStorePreviewUrl").getValue());
						}
						else{
							logger.debug("sourceHit = " + sourceHit.toString());
						}

						//=========================== dubuging ===========================
					}
				}
			}			
		}
		catch(IOException e ){
			logger.error("Exception while executing query {}", e);
		}

		return suggestedItems;
	}

	@Override
	public List<SuggestUDItemESDTO> userDefinedItemSuggest(String prefix, String retailer, String apparelTypeId, String sizeLabel){
		final String FIELD = "suggest";
		final String platform = "wearsit";
		//final String CONTEXT_CAT1 = "retailer_name";
		//final String CONTEXT_CAT2 = "apparel_type_id";
		//final String CONTEXT_CAT3 = "platform";
		final String CONTEXT = "conjunction";
		final String contextValue = retailer + "_" + platform + "_" + apparelTypeId + "_" + sizeLabel;
		final String SUGGEST_NAME = "cust-item-suggest-1";

		Map<String, String> inclusionProps;
		//BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(field, ter));
		//termSuggestionBuilder = new TermSuggestionBuilder(FIELD).text(prefix);
		Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT, Collections.singletonList(CategoryQueryContext.builder().setCategory(contextValue).build()));
		
		//Map<String, List<? extends ToXContent>> contexts = new HashMap<String, List<? extends ToXContent>>(2);
		//contexts.put(CONTEXT_CAT1, Collections.singletonList(CategoryQueryContext.builder().setCategory(retailer).build()));
		//contexts.put(CONTEXT_CAT2, Collections.singletonList(CategoryQueryContext.builder().setCategory(apparelTypeId).build()));
		//contexts.put(CONTEXT_CAT3, Collections.singletonList(CategoryQueryContext.builder().setCategory(platform).build()));
		
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			//.prefix(prefix)
			.prefix(prefix)
			.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
			.fetchSource(new String[]{
				"product.handle", 
				"product.udr", 
				"product.uds", 
				"product.onlineStoreUrl", 
				"apparel_type_id"
			}, null)
			.suggest(suggestionBuilder);

		SearchRequest searchRequest = new SearchRequest(new String[]{"item"}, searchSourceBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestUDItemESDTO> suggestedItems = new ArrayList<SuggestUDItemESDTO>();

		try{
			searchResponse = client.search(searchRequest, null);

			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());

			//XContentBuilder jsonContentBuilder = JsonXContent.contentBuilder();
			//inclusionProps = new HashMap<String, String>();
			//inclusionProps.put("", "");
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			//XContentBuilder xContentBuilder = searchResponse.getSuggest().getSuggestion(CONTEXT_CAT)
			//	.iterator().next().getOptions()
			//	.iterator().next().toXContent(JsonXContent.contentBuilder(), new ToXContent.MapaParams(inclusionProps));

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){
				//log the suggested text from provided prefix
				logger.debug("suggestion text: " + entry.getText().toString());
				//logger.debug("suggestion options: " + entry.getOption())
		
				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					//Map<String, Object> wtf = option.getPayloadAsMap();
					SearchHit hit = option.getHit();
		
					if(hit != null){
						//Map<String, SearchHits> innerHits = hit.getInnerHits();
						suggestedItems.add(new SuggestUDItemESDTO(hit.getId(), hit.getSourceAsMap()));
						Map<String, Object> source = hit.getSourceAsMap();
						logger.debug("source = " + source.toString());
						SearchHit sourceHit = SearchHit.createFromMap(source);
		
						//=========================== dubuging ===========================/
						
						if(sourceHit != null){
							logger.debug("sourceHit = " + sourceHit.toString());
							Map<String, SearchHits> innerHits = sourceHit.getInnerHits();
							DocumentField product = sourceHit.field("product");
		
							if(product != null){
								logger.debug("product = " + product.toString());
								Iterator<Object> itr = product.iterator();
								int index = 0;
		
								while(itr.hasNext()){
									Object value = itr.next();
									logger.debug("product[" + index + "]" + (value != null ? value.toString() : "null"));
									index++;
								}
							}
							else{
								logger.debug("product is null");
							}
		
							logger.debug("sourceHit = ", sourceHit.toString());
		
							// Todo: Clean this up.
							if(innerHits != null){
								SearchHits products = innerHits.get("products");
								SearchHits picture = innerHits.get("picture");
		
								if(products != null){
		
									if(products.iterator().hasNext()){
		
										SearchHit prod = products.iterator().next();
		
										if(prod != null){
											DocumentField picUrl = prod.field("onlineStorePreviewUrl");
		
											if(picUrl != null){
												logger.debug("picUrl = " + picUrl.getValue());
											}
											else{
												logger.debug("picUrl is null");
											}
										}
										else{
											logger.debug("prod is null");
										}
									}
									else{
										logger.debug("product.iterator().hasNext() is false");
									}
								}
								else{
									logger.debug("product is null.");
								}
							}
							else{
								logger.debug("innerHits is null");
							}
							//logger.debug("preview picture: " + sourceHit.getInnerHits().get("product").iterator().next().field("onlineStorePreviewUrl").getValue());
						}
						else{
							logger.debug("sourceHit = " + sourceHit.toString());
						}
		
						//=========================== dubuging ===========================
					}
				}
			}			
		}
		catch(IOException e ){
			logger.error("Exception while executing query {}", e);
		}
		return suggestedItems;		
	}

	@Override
	public List<SuggestRetailerEsDto> retailerNameSuggest(String prefix){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = null;
		final String SUGGEST_NAME = "retailerName-suggest-1";

		Map<String, String> inclusionProps;
		//Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().build()));
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix);
			//.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
			.fetchSource(new String[]{
				"logo_url", 
				"name", 
				"home_page_url"
			}, null)
			.suggest(suggestionBuilder);

		SearchRequest searchRequest = new SearchRequest(new String[]{"retailer"}, searchSourceBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestRetailerEsDto> suggestedNames = new ArrayList<SuggestRetailerEsDto>();

		try{
			searchResponse = client.search(searchRequest, null);

			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){

				logger.debug("suggestion text: " + entry.getText().toString());

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					SearchHit hit = option.getHit();
					if(hit != null){
						try{
							suggestedNames.add(new SuggestRetailerEsDto(Integer.parseInt(hit.getId()), hit.getSourceAsMap()));
						}catch(NumberFormatException nfe){
							logger.error(nfe);
							suggestedNames.add(new SuggestRetailerEsDto(null, hit.getSourceAsMap()));
						}
					}
				}
			}
			
		}catch(IOException e ){
			logger.error(e);
		}
		return suggestedNames;
	}

	@Override
	public List<SuggestUdrEsDto> udrNameSuggest(String prefix){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = null;
		final String SUGGEST_NAME = "udr-suggest-1";

		Map<String, String> inclusionProps;
		//Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().build()));
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix);
			//.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
			.fetchSource(new String[]{
				"name"
			}, null)
			.suggest(suggestionBuilder);

		SearchRequest searchRequest = new SearchRequest(new String[]{"udr"}, searchSourceBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestUdrEsDto> suggestedNames = new ArrayList<SuggestUdrEsDto>();

		try{
			searchResponse = client.search(searchRequest, null);

			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){

				logger.debug("suggestion text: " + entry.getText().toString());

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					SearchHit hit = option.getHit();
					if(hit != null){
						suggestedNames.add(new SuggestUdrEsDto(null, hit.getSourceAsMap()));
					}
				}
			}
			
		}catch(IOException e ){
			logger.error(e);
		}
		return suggestedNames;
	}

	@Override
	public List<SuggestUdsEsDto> udsLabelSuggest(String prefix){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = null;
		final String SUGGEST_NAME = "uds-suggest-1";

		Map<String, String> inclusionProps;
		//Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().build()));
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix);
			//.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
			.fetchSource(new String[]{
				"name"
			}, null)
			.suggest(suggestionBuilder);

		SearchRequest searchRequest = new SearchRequest(new String[]{"size_label"}, searchSourceBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestUdsEsDto> suggestedNames = new ArrayList<SuggestUdsEsDto>();

		try{
			searchResponse = client.search(searchRequest, null);
			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){

				logger.debug("suggestion text: " + entry.getText().toString());

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					SearchHit hit = option.getHit();
					if(hit != null){
						suggestedNames.add(new SuggestUdsEsDto(null, hit.getSourceAsMap()));
					}
				}
			}
			
		}catch(IOException e ){
			logger.error(e);
		}
		return suggestedNames;
	}

	@Override
	public List<SuggestApparelTypeEsDto> apparelTypeSuggest(String prefix){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = null;
		final String SUGGEST_NAME = "apparel_type-suggest-1";

		Map<String, String> inclusionProps;
		//Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().build()));
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix);
			//.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);


		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
			.fetchSource(new String[]{
				"id",
				"name"
			}, null)
			.suggest(suggestionBuilder);

		SearchRequest searchRequest = new SearchRequest(new String[]{"apparel_type"}, searchSourceBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestApparelTypeEsDto> suggestedNames = new ArrayList<SuggestApparelTypeEsDto>();

		try{
			searchResponse = client.search(searchRequest, null);
			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){

				logger.debug("suggestion text: " + entry.getText().toString());

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					SearchHit hit = option.getHit();
					if(hit != null){
						suggestedNames.add(new SuggestApparelTypeEsDto(null, hit.getSourceAsMap()));
					}
				}
			}
			
		}catch(IOException e ){
			logger.error(e);
		}
		return suggestedNames;
	}


//
	/*
	@Override
	public List<SuggestItemEsDto> itemSuggest(String prefix, String context){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = "retailer_name";
		final String SUGGEST_NAME = "item-suggest-1";

		Map<String, String> inclusionProps;
		//BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(field, ter));
		//termSuggestionBuilder = new TermSuggestionBuilder(FIELD).text(prefix);
		Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().setCategory(context).build()));
		
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix)
			.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		//prepare search on size_label index
		SearchRequestBuilder searchReqBuilder = client.prepareSearch("item")
			.setTypes("doc")
			.setFetchSource(new String[]{"product.featuredImage.originalSrc", "product.handle", "product.description", "product.variants", "product.vendor"}, null)
			.suggest(suggestionBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestItemEsDto> suggestedItems = new ArrayList<SuggestItemEsDto>();

		try{
			//execute() returns a ListenableActionFuture<Response>, which calls get() when complete, returning a Response.
			searchResponse = searchReqBuilder.execute().get();	
			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());

			//XContentBuilder jsonContentBuilder = JsonXContent.contentBuilder();
			//inclusionProps = new HashMap<String, String>();
			//inclusionProps.put("", "");
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			//XContentBuilder xContentBuilder = searchResponse.getSuggest().getSuggestion(CONTEXT_CAT)
			//	.iterator().next().getOptions()
			//	.iterator().next().toXContent(JsonXContent.contentBuilder(), new ToXContent.MapaParams(inclusionProps));

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){
				//log the suggested text from provided prefix
				logger.debug("suggestion text: " + entry.getText().toString());
				//logger.debug("suggestion options: " + entry.getOption())

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					//Map<String, Object> wtf = option.getPayloadAsMap();
					SearchHit hit = option.getHit();

					if(hit != null){
						//Map<String, SearchHits> innerHits = hit.getInnerHits();
						suggestedItems.add(new SuggestItemEsDto(hit.getId(), hit.getSourceAsMap()));
						Map<String, Object> source = hit.getSourceAsMap();
						logger.debug("source = " + source.toString());
						SearchHit sourceHit = SearchHit.createFromMap(source);

						//=========================== dubuging ===========================
						
						if(sourceHit != null){
							logger.debug("sourceHit = " + sourceHit.toString());
							Map<String, SearchHits> innerHits = sourceHit.getInnerHits();
							DocumentField product = sourceHit.field("product");

							if(product != null){
								logger.debug("product = " + product.toString());
								Iterator<Object> itr = product.iterator();
								int index = 0;

								while(itr.hasNext()){
									Object value = itr.next();
									logger.debug("product[" + index + "]" + (value != null ? value.toString() : "null"));
									index++;
								}
							}
							else{
								logger.debug("product is null");
							}

							logger.debug("sourceHit = ", sourceHit.toString());

							// Todo: Clean this up.
							if(innerHits != null){
								SearchHits products = innerHits.get("products");
								SearchHits picture = innerHits.get("picture");

								if(products != null){

									if(products.iterator().hasNext()){

										SearchHit prod = products.iterator().next();

										if(prod != null){
											DocumentField picUrl = prod.field("onlineStorePreviewUrl");

											if(picUrl != null){
												logger.debug("picUrl = " + picUrl.getValue());
											}
											else{
												logger.debug("picUrl is null");
											}
										}
										else{
											logger.debug("prod is null");
										}
									}
									else{
										logger.debug("product.iterator().hasNext() is false");
									}
								}
								else{
									logger.debug("product is null.");
								}
							}
							else{
								logger.debug("innerHits is null");
							}
							//logger.debug("preview picture: " + sourceHit.getInnerHits().get("product").iterator().next().field("onlineStorePreviewUrl").getValue());
						}
						else{
							logger.debug("sourceHit = " + sourceHit.toString());
						}

						/=========================== dubuging ===========================
					}
				}
			}			
		}
		catch(InterruptedException | ExecutionException e ){
			logger.error("Exception while executing query {}", e);
		}
		return suggestedItems;
	}

	@Override
	public List<SuggestUDItemESDTO> userDefinedItemSuggest(String prefix, String retailer, String apparelTypeId, String sizeLabel){
		final String FIELD = "suggest";
		final String platform = "wearsit";
		//final String CONTEXT_CAT1 = "retailer_name";
		//final String CONTEXT_CAT2 = "apparel_type_id";
		//final String CONTEXT_CAT3 = "platform";
		final String CONTEXT = "conjunction";
		final String contextValue = retailer + "_" + platform + "_" + apparelTypeId + "_" + sizeLabel;
		final String SUGGEST_NAME = "cust-item-suggest-1";

		Map<String, String> inclusionProps;
		//BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(field, ter));
		//termSuggestionBuilder = new TermSuggestionBuilder(FIELD).text(prefix);
		Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT, Collections.singletonList(CategoryQueryContext.builder().setCategory(contextValue).build()));
		
		//Map<String, List<? extends ToXContent>> contexts = new HashMap<String, List<? extends ToXContent>>(2);
		//contexts.put(CONTEXT_CAT1, Collections.singletonList(CategoryQueryContext.builder().setCategory(retailer).build()));
		//contexts.put(CONTEXT_CAT2, Collections.singletonList(CategoryQueryContext.builder().setCategory(apparelTypeId).build()));
		//contexts.put(CONTEXT_CAT3, Collections.singletonList(CategoryQueryContext.builder().setCategory(platform).build()));
		
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			//.prefix(prefix)
			.prefix(prefix)
			.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		//prepare search on size_label index
		SearchRequestBuilder searchReqBuilder = client.prepareSearch("item")
			.setTypes("doc")
			.setFetchSource(new String[]{"product.handle", "product.udr", "product.uds", "product.onlineStoreUrl", "apparel_type_id"}, null)
			.suggest(suggestionBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestUDItemESDTO> suggestedItems = new ArrayList<SuggestUDItemESDTO>();

		try{
			//execute() returns a ListenableActionFuture<Response>, which calls get() when complete, returning a Response.
			searchResponse = searchReqBuilder.execute().get();	
			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());

			//XContentBuilder jsonContentBuilder = JsonXContent.contentBuilder();
			//inclusionProps = new HashMap<String, String>();
			//inclusionProps.put("", "");
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			//XContentBuilder xContentBuilder = searchResponse.getSuggest().getSuggestion(CONTEXT_CAT)
			//	.iterator().next().getOptions()
			//	.iterator().next().toXContent(JsonXContent.contentBuilder(), new ToXContent.MapaParams(inclusionProps));

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){
				//log the suggested text from provided prefix
				logger.debug("suggestion text: " + entry.getText().toString());
				//logger.debug("suggestion options: " + entry.getOption())
		
				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					//Map<String, Object> wtf = option.getPayloadAsMap();
					SearchHit hit = option.getHit();
		
					if(hit != null){
						//Map<String, SearchHits> innerHits = hit.getInnerHits();
						suggestedItems.add(new SuggestUDItemESDTO(hit.getId(), hit.getSourceAsMap()));
						Map<String, Object> source = hit.getSourceAsMap();
						logger.debug("source = " + source.toString());
						SearchHit sourceHit = SearchHit.createFromMap(source);
		
						/=========================== dubuging ===========================/
						
						if(sourceHit != null){
							logger.debug("sourceHit = " + sourceHit.toString());
							Map<String, SearchHits> innerHits = sourceHit.getInnerHits();
							DocumentField product = sourceHit.field("product");
		
							if(product != null){
								logger.debug("product = " + product.toString());
								Iterator<Object> itr = product.iterator();
								int index = 0;
		
								while(itr.hasNext()){
									Object value = itr.next();
									logger.debug("product[" + index + "]" + (value != null ? value.toString() : "null"));
									index++;
								}
							}
							else{
								logger.debug("product is null");
							}
		
							logger.debug("sourceHit = ", sourceHit.toString());
		
							// Todo: Clean this up.
							if(innerHits != null){
								SearchHits products = innerHits.get("products");
								SearchHits picture = innerHits.get("picture");
		
								if(products != null){
		
									if(products.iterator().hasNext()){
		
										SearchHit prod = products.iterator().next();
		
										if(prod != null){
											DocumentField picUrl = prod.field("onlineStorePreviewUrl");
		
											if(picUrl != null){
												logger.debug("picUrl = " + picUrl.getValue());
											}
											else{
												logger.debug("picUrl is null");
											}
										}
										else{
											logger.debug("prod is null");
										}
									}
									else{
										logger.debug("product.iterator().hasNext() is false");
									}
								}
								else{
									logger.debug("product is null.");
								}
							}
							else{
								logger.debug("innerHits is null");
							}
							//logger.debug("preview picture: " + sourceHit.getInnerHits().get("product").iterator().next().field("onlineStorePreviewUrl").getValue());
						}
						else{
							logger.debug("sourceHit = " + sourceHit.toString());
						}
		
						/=========================== dubuging ===========================
					}
				}
			}			
		}
		catch(InterruptedException | ExecutionException e ){
			logger.error("Exception while executing query {}", e);
		}
		return suggestedItems;		
	}

	@Override
	public List<SuggestRetailerEsDto> retailerNameSuggest(String prefix){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = null;
		final String SUGGEST_NAME = "retailerName-suggest-1";

		Map<String, String> inclusionProps;
		//Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().build()));
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix);
			//.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		//prepare search on size_label index
		SearchRequestBuilder searchReqBuilder = client.prepareSearch("retailer")
			.setTypes("doc")
			.setFetchSource(new String[]{"logo_url", "name", "home_page_url"}, null)
			.suggest(suggestionBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestRetailerEsDto> suggestedNames = new ArrayList<SuggestRetailerEsDto>();

		try{
			//execute() returns a ListenableActionFuture<Response>, which calls get() when complete, returning a Response.
			searchResponse = searchReqBuilder.execute().get();	
			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){

				logger.debug("suggestion text: " + entry.getText().toString());

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					SearchHit hit = option.getHit();
					if(hit != null){
						try{
							suggestedNames.add(new SuggestRetailerEsDto(Integer.parseInt(hit.getId()), hit.getSourceAsMap()));
						}catch(NumberFormatException nfe){
							logger.error(nfe);
							suggestedNames.add(new SuggestRetailerEsDto(null, hit.getSourceAsMap()));
						}
					}
				}
			}
			
		}catch(InterruptedException | ExecutionException e ){
			logger.error(e);
		}
		return suggestedNames;
	}

	@Override
	public List<SuggestUdrEsDto> udrNameSuggest(String prefix){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = null;
		final String SUGGEST_NAME = "udr-suggest-1";

		Map<String, String> inclusionProps;
		//Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().build()));
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix);
			//.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		//prepare search on size_label index
		SearchRequestBuilder searchReqBuilder = client.prepareSearch("udr")
			.setTypes("doc")
			.setFetchSource(new String[]{"name"}, null)
			.suggest(suggestionBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestUdrEsDto> suggestedNames = new ArrayList<SuggestUdrEsDto>();

		try{
			//execute() returns a ListenableActionFuture<Response>, which calls get() when complete, returning a Response.
			searchResponse = searchReqBuilder.execute().get();	
			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){

				logger.debug("suggestion text: " + entry.getText().toString());

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					SearchHit hit = option.getHit();
					if(hit != null){
						suggestedNames.add(new SuggestUdrEsDto(null, hit.getSourceAsMap()));
					}
				}
			}
			
		}catch(InterruptedException | ExecutionException e ){
			logger.error(e);
		}
		return suggestedNames;
	}

	@Override
	public List<SuggestUdsEsDto> udsLabelSuggest(String prefix){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = null;
		final String SUGGEST_NAME = "uds-suggest-1";

		Map<String, String> inclusionProps;
		//Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().build()));
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix);
			//.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		//prepare search on size_label index
		SearchRequestBuilder searchReqBuilder = client.prepareSearch("size_label")
			.setTypes("doc")
			.setFetchSource(new String[]{"name"}, null)
			.suggest(suggestionBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestUdsEsDto> suggestedNames = new ArrayList<SuggestUdsEsDto>();

		try{
			//execute() returns a ListenableActionFuture<Response>, which calls get() when complete, returning a Response.
			searchResponse = searchReqBuilder.execute().get();	
			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){

				logger.debug("suggestion text: " + entry.getText().toString());

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					SearchHit hit = option.getHit();
					if(hit != null){
						suggestedNames.add(new SuggestUdsEsDto(null, hit.getSourceAsMap()));
					}
				}
			}
			
		}catch(InterruptedException | ExecutionException e ){
			logger.error(e);
		}
		return suggestedNames;
	}

	@Override
	public List<SuggestApparelTypeEsDto> apparelTypeSuggest(String prefix){
		final String FIELD = "suggest";
		final String CONTEXT_CAT = null;
		final String SUGGEST_NAME = "apparel_type-suggest-1";

		Map<String, String> inclusionProps;
		//Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(CONTEXT_CAT, Collections.singletonList(CategoryQueryContext.builder().build()));
		completionSuggestionBuilder = new CompletionSuggestionBuilder(FIELD)
			.prefix(prefix);
			//.contexts(contexts);

		SuggestBuilder suggestionBuilder = new SuggestBuilder().addSuggestion(SUGGEST_NAME, completionSuggestionBuilder);

		//prepare search on size_label index
		SearchRequestBuilder searchReqBuilder = client.prepareSearch("apparel_type")
			.setTypes("doc")
			.setFetchSource(new String[]{"id","name"}, null)
			.suggest(suggestionBuilder);

		//extends ActionResponse 
		SearchResponse searchResponse;
		List<SuggestApparelTypeEsDto> suggestedNames = new ArrayList<SuggestApparelTypeEsDto>();

		try{
			//execute() returns a ListenableActionFuture<Response>, which calls get() when complete, returning a Response.
			searchResponse = searchReqBuilder.execute().get();	
			logger.debug("searchResponse: " + searchResponse.toString());
			logger.debug("REST status: " + RestStatus.fromCode(searchResponse.status().getStatus()));
			logger.debug("suggestion: " + searchResponse.getSuggest().toString());
			
			CompletionSuggestion suggestion = (CompletionSuggestion) searchResponse.getSuggest().getSuggestion(SUGGEST_NAME);

			for(CompletionSuggestion.Entry entry : suggestion.getEntries()){

				logger.debug("suggestion text: " + entry.getText().toString());

				for(CompletionSuggestion.Entry.Option option : entry.getOptions()){
					SearchHit hit = option.getHit();
					if(hit != null){
						suggestedNames.add(new SuggestApparelTypeEsDto(null, hit.getSourceAsMap()));
					}
				}
			}
			
		}catch(InterruptedException | ExecutionException e ){
			logger.error(e);
		}
		return suggestedNames;
	}*/
//
}