package com.example.common.dto.response;

import com.example.common.dto.request.BrandRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class BrandResponse extends BrandRequest {
	private int id;
	private LocalDateTime createdDate;
	private String createdBy;
	private LocalDateTime updateDate;
	private String updateBy;
	private LocalDateTime deleteDate;
	private String deleteBy;
}
