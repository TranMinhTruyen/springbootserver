package com.ggapp.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {
	private LocalDateTime timestamp = LocalDateTime.now();
	private int status;
	private String statusname;
	private String message;
	private Object payload;
}
