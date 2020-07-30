package oxi.events;

import oxi.models.UserVerificationToken;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


/*
*  Custom ApplicationEvent published on successful user registration
*/
@SuppressWarnings("serial")
public class OnSendUserVerificationEvent extends ApplicationEvent {
	private String appUrl;
	private Locale locale;
	private UserVerificationToken uvt;

	public OnSendUserVerificationEvent(UserVerificationToken uvt, Locale locale, String appUrl){
		super(uvt);
		this.uvt = uvt;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public String getAppUrl(){return this.appUrl;}
	public Locale getLocale(){return this.locale;}
	public UserVerificationToken getUserVerificationToken(){return this.uvt;}
}

