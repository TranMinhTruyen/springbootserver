package com.ggapp.common.utils.mapper.Impl;

import com.ggapp.common.dto.request.ProductRequest;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.common.utils.mapper.ProductMapper;
import com.ggapp.dao.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapperImpl implements ProductMapper {
    @Override
    public Product requestToEntity(ProductRequest productRequest) {
        if (productRequest == null)
            return null;
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDiscount(productRequest.getDiscount());
        product.setPrice(productRequest.getPrice());
        product.setType(productRequest.getType());
        product.setUnitInStock(productRequest.getUnitInStock());
        product.setNew(productRequest.isNew());
        return product;
    }

    @Override
    public ProductResponse entityToResponse(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setBrand(product.getBrand().getName());
        productResponse.setCategory(product.getCategory().getName());
        productResponse.setDiscount(product.getDiscount());
        productResponse.setPrice(product.getPrice());
        productResponse.setType(product.getType());
        productResponse.setUnitInStock(product.getUnitInStock());
        productResponse.setCreatedDate(product.getCreatedDate());
        productResponse.setCreatedBy(product.getCreatedBy());
        productResponse.setUpdateDate(product.getUpdateDate());
        productResponse.setUpdateBy(product.getUpdateBy());
        productResponse.setDeleteDate(product.getDeleteDate());
        productResponse.setDeleteBy(product.getDeleteBy());
        productResponse.setNew(product.isNew());
        return productResponse;
    }

    @Override
    public List<Product> requestToEntity(List<ProductRequest> productRequestList) {
        if (productRequestList.isEmpty()) {
            return null;
        }
        List<Product> productResponseList = new ArrayList<>(productRequestList.size());
        for (ProductRequest productRequest: productRequestList){
            productResponseList.add(requestToEntity(productRequest));
        }
        return productResponseList;
    }

    @Override
    public List<ProductResponse> entityToResponse(List<Product> productList) {
        if (productList.isEmpty()) {
            return null;
        }
        List<ProductResponse> productResponseList = new ArrayList<>(productList.size());
        for (Product product: productList){
            productResponseList.add(entityToResponse(product));
        }
        return productResponseList;
    }
}
