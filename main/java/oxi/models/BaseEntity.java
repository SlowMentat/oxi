package oxi.models;

import java.util.Date;
import javax.persistence.Column;

public class BaseEntity{

	@Column(name = "created_on", nullable=false, updatable = false)
	private Date createdOn = new Date();

	@Column(name = "updated_on", nullable=true)
	//private Instant updatedOn = createdOn;
	private Date updatedOn = null;


	public void setUpdatedOn(Date updatedOn){this.updatedOn = updatedOn;}

	public Date getUpdatedOn(){return this.updatedOn;}
	public Date getCreatedOn(){return this.createdOn;}
}