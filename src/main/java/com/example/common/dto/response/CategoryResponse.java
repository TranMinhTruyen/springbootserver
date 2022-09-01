package com.example.common.dto.response;

import com.example.common.dto.request.CategoryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryResponse extends CategoryRequest {
	private int id;
	private LocalDateTime createdDate;
	private String createdBy;
	private LocalDateTime updateDate;
	private String updateBy;
	private LocalDateTime deleteDate;
	private String deleteBy;
}
