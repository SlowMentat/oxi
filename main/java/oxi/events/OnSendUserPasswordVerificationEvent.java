package oxi.events;

import oxi.models.User;
//import oxi.models.UserPasswordVerificationToken;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


/*
*  Custom ApplicationEvent published on successful user registration
*/
@SuppressWarnings("serial")
public class OnSendUserPasswordVerificationEvent extends ApplicationEvent {
	/*private String appUrl;
	private Locale locale;
	private UserPasswordVerificationToken uPVT;

	public OnSendUserPasswordVerificationEvent(UserPasswordVerificationToken uPVT, Locale locale, String appUrl){
		super(uPVT);
		this.uPVT = uPVT;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public String getAppUrl(){return this.appUrl;}
	public Locale getLocale(){return this.locale;}
	public UserPasswordVerificationToken getUserPasswordVerificationToken(){return this.uPVT;}*/


	private String baseUrl;
	private String appUrl;
	private Locale locale;
	private User user;

	public OnSendUserPasswordVerificationEvent(User user, Locale locale, String baseUrl, String appUrl){
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
		this.baseUrl = baseUrl;
	}

	public String getAppUrl(){return this.appUrl;}
	public String getBaseUrl(){return this.baseUrl;}
	public Locale getLocale(){return this.locale;}
	public User getUser(){return this.user;}
}

