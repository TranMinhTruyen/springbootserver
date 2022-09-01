package com.example.dao.repository.mongo;

import com.example.dao.document.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductImageRepository extends MongoRepository<ProductImage, Integer> {
}
