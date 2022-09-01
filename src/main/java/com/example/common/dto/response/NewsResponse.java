package com.example.common.dto.response;

import com.example.common.dto.request.NewsRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewsResponse extends NewsRequest {
	private int id;
	private int userCreateId;
	private String userCreateName;
	private Date createDate;
}
