package com.ggapp.dao.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "ConfirmKey")
@Data
public class ConfirmKey {
	@Field(value = "Email")
	private String email;

	@Field(value = "Key")
	private String key;

	@Field(value = "Expire")
	private LocalDateTime expire;

	@Field(value = "Type")
	private String type;
}
