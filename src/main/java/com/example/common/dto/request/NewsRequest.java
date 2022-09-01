package com.example.common.dto.request;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class NewsRequest {
	private String title;
	private String content;
}
