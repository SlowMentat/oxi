/*package oxi.events;

import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import oxi.models.CursorDto;

import java.util.Locale;



//Custom ApplicationEvent published on successful user registration

@SuppressWarnings("serial")
public class CursorResultsRetrivedEvent<T extends Serializable> extends ApplicationEvent {
	private final UriComponentsBuilder uriBuilder;
	private final HttpServletResponse response;

	private final CursorDto cursor;

	public CursorResultsRetrivedEvent(final Class<T> clazz, final UriComponentsBuilder uriBuilder, final HttpServletResponse response, final CursorDto cursor){
		super(clazz);

		this.uriBuilder = uriBuilder;
		this.response = response;
		this.cursor = cursor;
	}

	public final UriComponentsBuilder getUriBuilder(){
		return this.uriBuilder;
	}

	public final HttpServletResponse getResponse(){

	}
}*/