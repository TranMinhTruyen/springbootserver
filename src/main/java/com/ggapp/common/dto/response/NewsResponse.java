package com.ggapp.common.dto.response;

import com.ggapp.common.dto.request.NewsRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewsResponse extends NewsRequest {
	private long id;
	private long userCreateId;
	private String userCreateName;
	private Date createDate;
}
