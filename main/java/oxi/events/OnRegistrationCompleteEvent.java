package oxi.events;

import oxi.models.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


/*
*  Custom ApplicationEvent published on successful user registration
*/
@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private String appUrl;
	private Locale locale;
	private User user;

	public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl){
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public String getAppUrl(){return this.appUrl;}
	public Locale getLocale(){return this.locale;}
	public User getUser(){return this.user;}
}

