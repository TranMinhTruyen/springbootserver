package com.example.dao.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "ConfirmKey")
@Data
public class ConfirmKey {
	@Field(value = "Email")
	private String email;

	@Field(value = "Key")
	private String key;

	@Field(value = "Expire")
	private Date expire;

	@Field(value = "Type")
	private String type;

	@Override
	public String toString() {
		return "ConfirmKey{" +
				"email='" + email + '\'' +
				", key='" + key + '\'' +
				", expire=" + expire +
				'}';
	}
}
