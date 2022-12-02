package com.ggapp.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product_image")
public class ProductImage implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Version
	private int version;

	@ManyToOne
	@JoinColumn(name = "id_product")
	private Product product;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	@Column(name = "image_path")
	private String imagePath;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "update_date")
	private LocalDateTime updateDate;

	@Column(name = "update_by")
	private String updateBy;

	@Column(name = "delete_date")
	private LocalDateTime deleteDate;

	@Column(name = "delete_by")
	private String deleteBy;
}
