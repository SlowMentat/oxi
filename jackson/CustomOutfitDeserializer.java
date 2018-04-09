package oxi.jackson;

import oxi.models.*;
import java.io.IOException;
//import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.*;
import java.util.*;

//public class CustomOutfitDeserializer extends StdDeserializer<Outfit>{
public class CustomOutfitDeserializer extends StdDeserializer<Outfit>{
	private static final Logger logger = LogManager.getLogger(CustomOutfitDeserializer.class);
	
	public CustomOutfitDeserializer(){
		this(null);
	}
	
	public CustomOutfitDeserializer(Class<?> vc){
		super(vc);
	}
	
	@Override
	public Outfit deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Iterator<JsonNode> elements = node.elements();
		//List<Outfit> outfits = new List<Outfit>;
		logger.trace("*** ***Creating new Outfit object");
		return new Outfit();
		/*for (; elements.hasNext();) {
            String type = elements.next().toString();
			
        }*/
	}
} 