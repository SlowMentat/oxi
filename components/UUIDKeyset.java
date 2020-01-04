package oxi.components;

import oxi.models.retailer.RetailerAccount;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Calendar;
import java.sql.Timestamp;
import java.io.Serializable;
import javax.persistence.Tuple;
import javax.xml.bind.DatatypeConverter;
import org.javatuples.Pair;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;

import com.blazebit.persistence.Keyset;
import java.util.Date;


/*
*  An implimentation of Keyset interface for 
*/
public class UUIDKeyset implements Keyset
{
	
	private UUID id = null;
	private Date date = null;

	public UUIDKeyset(UUID id, String date){
		this.id = id;
		this.date = DatatypeConverter.parseDateTime(date).getTime();
	}

	@Override
	public Serializable[] getTuple(){
		//return idSet;
		Serializable[] keyset = {this.id, this.date};
		return keyset;
	}
}