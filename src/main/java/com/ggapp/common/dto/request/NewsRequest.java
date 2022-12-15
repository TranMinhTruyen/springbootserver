package com.ggapp.common.dto.request;

import lombok.Data;

@Data
public class NewsRequest {
	private int version;
	private String title;
	private String content;
}
