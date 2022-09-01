package com.example.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
	private Date timestamp = new Date();
	private int status;
	private String statusname;
	private String message;
	private Object payload;
}
