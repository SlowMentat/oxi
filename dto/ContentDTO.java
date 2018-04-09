package oxi.dto;

class ContentDTO{
	private long photo_id;	
	private long user_id;
	private String photo;
	private int component;
	private int xcomponent;
	private int ycomponent;
	private String link;
	private int likes;
	
	//Constructor
	public ContentDTO(){
	}
	
	//Setters
	public void setPhoto(String photo){
		this.photo = photo;
	}
	
	public void setLink(String link){
		this.link = link;
	}
	
	public void setComponent(int component){
		this.component = component;
	}
	
	public void setXComponent(int xcomponent){
		this.xcomponent = xcomponent;
	}
	
	public void setYCompenent(int ycomponent){
		this.ycomponent = ycomponent;
	}
	
	public void setlikes(int likes){
		this.likes = likes;
	}
	
	//Getters
	public String getPhoto(){
		return this.photo;
	}
	
	public String getLink(){
		return this.link;
	}
	
	public int getComponent(){
		return this.component;
	}
	
	public int getXComponent(){
		return this.xcomponent;
	}
	
	public int getYCompenent(){
		return this.ycomponent;
	}
	
	public int getlikes(){
		return this.likes;
	}
}