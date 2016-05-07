package com.example.godkiller.rencai.base;

public class Channel {
	private String id;
	private String name;
	private int order;
	private String weburl;
	private String hweburl;
	
	
	
	public Channel(){
		
	}
	public Channel(String id,String name, int order) {
		super();
		this.id=id;
		this.name = name;
		this.order = order;

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
