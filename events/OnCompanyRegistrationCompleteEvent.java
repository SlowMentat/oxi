package oxi.events;

import oxi.models.Company;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


/*
*  Custom ApplicationEvent published on successful user registration
*/
@SuppressWarnings("serial")
public class OnCompanyRegistrationCompleteEvent extends ApplicationEvent {
	private String appUrl;
	private Locale locale;
	private Company company;

	public OnCompanyRegistrationCompleteEvent(Company company, Locale locale, String appUrl){
		super(company);
		this.company = company;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public String getAppUrl(){return this.appUrl;}
	public Locale getLocale(){return this.locale;}
	public Company getCompany(){return this.company;}
}