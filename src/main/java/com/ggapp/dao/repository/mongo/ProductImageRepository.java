package com.ggapp.dao.repository.mongo;

import com.ggapp.dao.document.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductImageRepository extends MongoRepository<ProductImage, Integer> {
}
