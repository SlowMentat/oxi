package oxi.configs;

import oxi.repositories.es.SuggestEsRepositoryCustom;
import oxi.repositories.es.SuggestEsRepositoryImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

//import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.RestClients;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress; 
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;


@Configuration
@EnableElasticsearchRepositories(basePackages = "oxi.repositories.es")
@ComponentScan(basePackages = {"oxi.models.dto.es"})
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration{

	@Value("${elasticsearch.home:/home/dbryant/elasticsearch-6.0.1}")
	private String elasticsearchHome;

	@Value("${elasticsearch.cluster.name:elasticsearch}")
	private String clusterName;

	@Bean
	public SuggestEsRepositoryCustom suggestEsRepositoryCustom(){
		return new SuggestEsRepositoryImpl();
	}

	//@Override
	//@Bean
	//public RestHighLevelClient client(){
//
	//	TransportClient client = null;
	//	
	//	try{
	//		Settings elasticsearchSettings = Settings.builder()
	//			.put("client.transport.sniff", true)
	//			.put("path.home", elasticsearchHome)
	//			.put("cluster.name", clusterName).build();
	//		client = new PreBuiltTransportClient(elasticsearchSettings);
	//		client.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
	//	}
	//	catch(UnknownHostException e){
	//		e.printStackTrace();
	//	}
//
	//	return client;
	//}

	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient(){
		
		//try{
			final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo("localhost:9200")
				.build();
		//}
		//catch(UnknownHostException e){
		//	e.printStackTrace();
		//}

		//return client;
		return RestClients.create(clientConfiguration).rest(); 
	}

	//This is already being configured by spring boot. Set spring.main.allow-bean-definition-overriding=true to override spring-boot elasticsearchTemplate
	//@Bean
	//public ElasticsearchOperations elasticsearchTemplate(){
	//	return new ElasticsearchTemplate(client());
	//}
}