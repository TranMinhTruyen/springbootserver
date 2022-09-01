package com.example.common.utils;

import com.example.common.dto.response.ProductImageResponse;
import com.example.common.dto.response.ProductResponse;
import com.example.dao.document.Image;
import com.example.dao.document.ListProduct;
import com.example.dao.document.ProductImage;
import com.example.dao.entity.Product;
import com.example.dao.repository.mongo.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Utils {

    @Autowired
    private ProductImageRepository productImageRepository;

    public List<String> getMethodNameOfClass(Class<?> clazz) {
        List<String> methodName = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            methodName.add(fields[i].getName());
        }
        return methodName;
    }

    public String capitalize(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public BigDecimal calculatePrice(Product product, long amount) {
        BigDecimal discountPrice = product.getPrice().multiply(BigDecimal.valueOf((product.getDiscount() / 100)));
        BigDecimal productPrice = product.getPrice().subtract(discountPrice);
        return productPrice.multiply(BigDecimal.valueOf(amount)).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculatePrice(Product product) {
        BigDecimal discountPrice = product.getPrice().multiply(BigDecimal.valueOf((product.getDiscount() / 100)));
        return product.getPrice().subtract(discountPrice).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculatePrice(ProductResponse productResponse) {
        BigDecimal discountPrice = productResponse.getPrice().multiply(BigDecimal.valueOf((productResponse.getDiscount() / 100)));
        return productResponse.getPrice().subtract(discountPrice).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculatePrice(List<ListProduct> listProduct) {
        BigDecimal totalPrice = new BigDecimal(0);
        for (ListProduct items : listProduct) {
            BigDecimal productPrice = items.getPriceAfterDiscount().multiply(BigDecimal.valueOf(items.getProductAmount()));
            totalPrice = totalPrice.add(productPrice);
        }
        return totalPrice.setScale(0, RoundingMode.HALF_EVEN);
    }

    public List<ProductImageResponse> getProductImage(int productId) {
        Optional<ProductImage> productImageList = productImageRepository.findById(productId);
        if (productImageList.isPresent()) {
            List<ProductImageResponse> productImageResponseList = new ArrayList<>();
            for (Image i : productImageList.get().getImages()) {
                ProductImageResponse productImageResponse = new ProductImageResponse();
                productImageResponse.setImageId(i.getImageId());
                productImageResponse.setImage(i.getImage());
                productImageResponseList.add(productImageResponse);
            }
            return productImageResponseList;
        } else return null;
    }
}
