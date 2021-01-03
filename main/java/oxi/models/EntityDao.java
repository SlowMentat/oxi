package oxi.models;

import java.lang.*;
//import org.springframework.hateoas.Identifiable;

public class EntityDao /*implements Identifiable<Long>*/
{
	private Long Id;

	public Long getId(){return this.Id;}

	public void setId(Long id){this.Id = id;}
}
