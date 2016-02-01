package com.ricky.codelab.lucene.model;

public class Article {
	private String title;
	private String datetime;
	private String author;
	
	public Article(String title, String datetime, String author) {
		this.title = title;
		this.datetime = datetime;
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
}
