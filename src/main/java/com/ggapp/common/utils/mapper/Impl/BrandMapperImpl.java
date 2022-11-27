package com.ggapp.common.utils.mapper.Impl;

import com.ggapp.common.dto.request.BrandRequest;
import com.ggapp.common.dto.response.BrandResponse;
import com.ggapp.common.utils.mapper.BrandMapper;
import com.ggapp.dao.entity.Brand;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandMapperImpl implements BrandMapper {
    @Override
    public Brand requestToEntity(BrandRequest brandRequest) {
        if (brandRequest == null)
            return null;
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());
        brand.setImage(brandRequest.getImage());
        return brand;
    }

    @Override
    public BrandResponse entityToResponse(Brand brand) {
        if (brand == null)
            return null;
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(brand.getId());
        brandResponse.setName(brand.getName());
        brandResponse.setDescription(brand.getDescription());
        brandResponse.setImage(brand.getImage());
        brandResponse.setCreatedDate(brand.getCreatedDate());
        brandResponse.setCreatedBy(brand.getCreatedBy());
        brandResponse.setUpdateDate(brand.getUpdateDate());
        brandResponse.setUpdateBy(brand.getUpdateBy());
        brandResponse.setDeleteDate(brand.getDeleteDate());
        brandResponse.setDeleteBy(brand.getDeleteBy());
        return brandResponse;
    }

    @Override
    public List<Brand> requestToEntity(List<BrandRequest> brandRequests) {
        if (brandRequests.isEmpty())
            return null;
        List<Brand> brandList = new ArrayList<>(brandRequests.size());
        for (BrandRequest brandRequest: brandRequests){
            brandList.add(requestToEntity(brandRequest));
        }
        return brandList;
    }

    @Override
    public List<BrandResponse> entityToResponse(List<Brand> brands) {
        if (brands.isEmpty())
            return null;
        List<BrandResponse> brandResponseList = new ArrayList<>(brands.size());
        for(Brand brand: brands){
            brandResponseList.add(entityToResponse(brand));
        }
        return brandResponseList;
    }
}
