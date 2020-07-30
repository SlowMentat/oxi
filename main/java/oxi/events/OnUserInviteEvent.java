package oxi.events;

import oxi.models.User;
import org.springframework.context.ApplicationEvent;
import java.util.Locale;


/*
*  Custom ApplicationEvent published on successful user registration
*/
@SuppressWarnings("serial")
public class OnUserInviteEvent extends ApplicationEvent {
	private String appUrl;
	private Locale locale;
	private User user;
	private String decodedPassword;

	public OnUserInviteEvent(User user, Locale locale, String appUrl, String password){
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
		this.decodedPassword = password;
	}

	public String getAppUrl(){return this.appUrl;}
	public Locale getLocale(){return this.locale;}
	public User getUser(){return this.user;}
	public String getDecodedPassword(){return this.decodedPassword;}
}

