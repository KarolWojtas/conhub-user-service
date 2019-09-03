package com.karol.model.domain;

public enum Roles {
	USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
	private String value;
	Roles(String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
