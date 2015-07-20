package org.example.myapp.client.model;

import java.io.Serializable;

public class EducationArticle implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String name;
	private String addtime;
	private String content;
	private String keywords;
	private String sort;
	private String [] keywordArray;
	private String [] SortArray;
	
	
	
	public String[] getSortArray() {
		return SortArray;
	}

	public void setSortArray(String[] sortArray) {
		SortArray = sortArray;
	}

	public String[] getKeywordArray() {
		return keywordArray;
	}

	public void setKeywordArray(String[] keywordArray) {
		this.keywordArray = keywordArray;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) 
	{
		this.keywords = keywords;
	}
	
	
}
