package com.example.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Entity
@Data
public class Brand implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Version
	private int version;

	@Column
	private String name;

	@Column
	private String description;

	@Column(columnDefinition = "LONGTEXT")
	private String image;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy="brand")
	private List<Product> productList;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

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
