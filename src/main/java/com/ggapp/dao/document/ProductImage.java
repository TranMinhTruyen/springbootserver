package com.ggapp.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ProductImage")
@Data
public class ProductImage implements Serializable {

	private int id;

	@Version
	private int version;

	@Field(value = "Images")
	private List<Image> images;

	@Field(name = "CreatedDate")
	private LocalDateTime createdDate;

	@Field(name = "CreatedBy")
	private String createdBy;

	@Field(name = "UpdateDate")
	private LocalDateTime updateDate;

	@Field(name = "UpdateBy")
	private String updateBy;

	@Field(name = "DeleteDate")
	private LocalDateTime deleteDate;

	@Field(name = "DeleteBy")
	private String deleteBy;
}
