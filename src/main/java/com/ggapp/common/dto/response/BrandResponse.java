package com.ggapp.common.dto.response;

import com.ggapp.common.dto.request.BrandRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class BrandResponse extends BrandRequest {
	private long id;
	private boolean isDeleted;
	private LocalDateTime createdDate;
	private String createdBy;
	private LocalDateTime updateDate;
	private String updateBy;
	private LocalDateTime deleteDate;
	private String deleteBy;
}
