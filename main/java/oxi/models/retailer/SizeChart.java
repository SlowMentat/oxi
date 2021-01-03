package oxi.models.retailer;

import oxi.models.RelatedEntity;
import oxi.models.Relational;
import oxi.models.Item;
//import oxi.models.projection.*;
import oxi.models.dto.retailer.SizeGroupDto;
import oxi.models.dto.retailer.SizeChartDto;
import oxi.models.retailer.*;
//import oxi.models.*;
//import oxi.models.retailer.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Iterator;
import java.io.Serializable;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.server.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.GenericGenerator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Entity
@Table(name="size_chart")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SizeChart.class)
public class SizeChart extends RelatedEntity implements Serializable/*Identifiable<UUID>*/
{
	@Transient
	private static final Logger logger = LogManager.getLogger(SizeChart.class);

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	@Column(name = "id_text", columnDefinition="VARCHAR(36)", updatable = false, insertable = false)
	private String idText;
	@Column(name = "name", columnDefinition="VARCHAR(36)")
	private String name;
	//private List<SizeChartSizeGroupId> sizeChartSizeGroupId;

	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval= true, mappedBy = "sizeChart")
	@RestResource(rel="client_0")	
	@JsonIdentityReference(alwaysAsId=true)
	private List<SizeChartSizeGroup> sizeGroups = new ArrayList<SizeChartSizeGroup>();

	//@OneToMany(cascade = CascadeType.ALL, orphanRemoval= true, mappedBy = "sizeChart")
	//@RestResource(rel="client_0")	
	//@JsonIdentityReference(alwaysAsId=true)
	//private List<Item> items = new ArrayList<>();

	@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="client_1")
	@JsonIdentityReference(alwaysAsId=true)
	@JoinColumn(name = "retailer_account_id")
	private RetailerAccount retailerAccount;

	public SizeChart(){}

	public SizeChart(
		UUID id, 
		String name,
		List<SizeChartSizeGroup> sizeGroups
		/*List<Item> items*/){
		//super();
		this.id = id;
		this.name = name;
		this.sizeGroups = sizeGroups;
		//this.items = items;
	}

	public SizeChart(SizeChartDto sizeChartDto){
		this.id = sizeChartDto.getId() != null ? UUID.fromString(sizeChartDto.getId()) : null;
		this.name = sizeChartDto.getChartName();
		this.sizeGroups = new ArrayList<SizeChartSizeGroup>(sizeChartDto.getSizeGroupDtos().size());
		
		for(SizeGroupDto sizeGroupDto : sizeChartDto.getSizeGroupDtos()){
			sizeGroups.add(new SizeChartSizeGroup(new SizeGroup(sizeGroupDto), this));
		}
	}

	//Setters
	public void setId(UUID id){this.id = id;}
	public void setName(String name){this.name = name;}
	public void setRetailerAccount(RetailerAccount retailerAccount){this.retailerAccount = (RetailerAccount)this.setManyToOneParent(retailerAccount, this.retailerAccount, this);}
	//public void setItems(List<Item> items){
	//	this.items = items;
	//}

	//@JsonAnySetter
	//public void addItem(Item item){
	//	this.items.add(item);
	//	if (item.getSizeChart() != this) item.setSizeChart(this);
	//}

	public void addSizeGroup(SizeGroup sizeGroup){
		SizeChartSizeGroup sizeChartSizeGroup = new SizeChartSizeGroup(sizeGroup, this);
		this.sizeGroups.add(sizeChartSizeGroup);
		sizeGroup.getSizeCharts().add(sizeChartSizeGroup);		
	}

	public void addSizeGroup(SizeChartSizeGroup sizeChartSizeGroup){
		this.sizeGroups.add(sizeChartSizeGroup);
		sizeChartSizeGroup.getSizeGroup().getSizeCharts().add(sizeChartSizeGroup);
	}

	public void removeSizeGroup(SizeGroup sizeGroup){
		for(Iterator<SizeChartSizeGroup> iterator = sizeGroups.iterator(); iterator.hasNext();){
			SizeChartSizeGroup sizeChartSizeGroup = iterator.next();

			if(sizeChartSizeGroup.getSizeChart().equals(this) && sizeChartSizeGroup.getSizeGroup().equals(sizeGroup)){
				iterator.remove();
				sizeChartSizeGroup.getSizeGroup().getSizeCharts().remove(sizeChartSizeGroup);  //Needed for bi-directional mapping
				sizeChartSizeGroup.setSizeChart(null);
				sizeChartSizeGroup.setSizeGroup(null);
			}
		}
	}

	public void removeSizeGroup(String sizeGroupId){
		for(Iterator<SizeChartSizeGroup> iterator = sizeGroups.iterator(); iterator.hasNext();){
			SizeChartSizeGroup sizeChartSizeGroup = iterator.next();

			logger.debug("SizeChart#removeSizeGroup: sieChart equal = " + sizeChartSizeGroup.getSizeChart().equals(this));
			logger.debug("\nSizeChart#removeSizeGroup:  sizeChartSizeGroup#getSizeGroups() = \"" + sizeChartSizeGroup.getSizeGroup().getId() + "\", \nsizeGroupId parameter = \"" + sizeGroupId + "\"\n");
			logger.debug("SizeChart#removeSizeGroup: sizeGroup ids equal = " + sizeChartSizeGroup.getSizeGroup().getId().toString().equalsIgnoreCase(sizeGroupId));
			
			if(sizeChartSizeGroup.getSizeChart().equals(this) && sizeChartSizeGroup.getSizeGroup().getId().toString().equals(sizeGroupId)){
				logger.debug("SizeChart#removeSizeGroup: sizeChartSizeGroup = \n" + sizeChartSizeGroup.toString());
				iterator.remove();
				sizeChartSizeGroup.getSizeGroup().getSizeCharts().remove(sizeChartSizeGroup);  //Needed for bi-directional mapping
				sizeChartSizeGroup.setSizeChart(null);
				sizeChartSizeGroup.setSizeGroup(null);
			}
		}
	}


	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		logger.debug("internalAddChild invoked");
		/*if(this.sizeGroup == null){
			logger.debug("instantiating new List<T>");
			this.sizeGroup = new ArrayList<SizeChartSizeGroup>();
		}
		this.sizeGroup.add((SizeChartSizeGroup)targetChild);*/

		SizeGroup childItem = (SizeGroup) targetChild;
		SizeChartSizeGroup sizeChartSizeGroup = new SizeChartSizeGroup(childItem, this);
		this.sizeGroups.add(sizeChartSizeGroup);
		childItem.getSizeCharts().add(sizeChartSizeGroup);
	}

	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.sizeGroups.remove((SizeGroup)targetChild);
	}

	//Getters
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public List<SizeChartSizeGroup> getSizeGroups(){return this.sizeGroups;}
	public RetailerAccount getRetailerAccount(){return this.retailerAccount;}
	//public List<Item> getItems(){return this.items;}
	public String getChartName(){return this.name;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("name:").append(((this.name == null) ? "null" : this.name));
        return sb.toString();		
	}
}
