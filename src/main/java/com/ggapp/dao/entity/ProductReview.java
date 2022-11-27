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

/**
 * @author Tran Minh Truyen
 */
@Data
@Entity
@Table(name = "product_review")
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int star;

    @Column
    private String text;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
}
