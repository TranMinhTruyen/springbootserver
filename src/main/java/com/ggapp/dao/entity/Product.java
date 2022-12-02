package com.ggapp.dao.entity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Entity
@Data
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Version
	private int version;

	@Column(name = "product_code")
	private String productCode;

	@Column
	private String name;

	@Column
	private BigDecimal price;

	@Column
	private String type;

	@Column
	private float discount;

	@Column(name = "unit_in_stock")
	private long unitInStock;

	@ManyToOne
	@JoinColumn(name = "id_brand")
	private Brand brand;

	@ManyToOne
	@JoinColumn(name = "id_category")
	private Category category;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
	private List<ProductReview> productReviewList;

	@OneToMany(fetch = FetchType.LAZY ,mappedBy = "product")
	private List<ProductVoucher> productVoucherList;

	@OneToMany(fetch = FetchType.LAZY ,mappedBy = "product")
	private List<ProductImage> imagesPath;

	@Column(name = "is_deleted")
	private boolean isDeleted;

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

	@Column(name = "is_new")
	private boolean isNew;
}
