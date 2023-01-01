package com.ggapp.services.implement;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.common.utils.mapper.BrandMapper;
import com.ggapp.dao.entity.Brand;
import com.ggapp.dao.entity.Product;
import com.ggapp.common.dto.request.BrandRequest;
import com.ggapp.common.dto.response.BrandResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.dao.repository.mysql.BrandRepository;
import com.ggapp.dao.repository.mysql.ProductRepository;
import com.ggapp.dao.repository.specification.BrandSpecification;
import com.ggapp.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ggapp.common.enums.MessageResponse.BRAND_IS_EXIST;
import static com.ggapp.common.enums.MessageResponse.BRAND_NOT_FOUND;

/**
 * @author Tran Minh Truyen
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE = 200
 */

@Service
public class BrandServiceImp implements BrandService {

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BrandMapper brandMapper;

	@Override
	public BrandResponse createBrand(BrandRequest brandRequest, CustomUserDetail customUserDetail) throws ApplicationException {
		if (isExists(brandRequest.getName())){
			Brand newBrand = new Brand();
			newBrand.setName(brandRequest.getName());
			newBrand.setDescription(brandRequest.getDescription());
			newBrand.setDeleted(false);
			newBrand.setCreatedDate(LocalDateTime.now());
			newBrand.setCreatedBy(customUserDetail.getAccountDetail().getFirstName() + customUserDetail.getAccountDetail().getLastName());
			Brand result = brandRepository.save(newBrand);
			return brandMapper.entityToResponse(result);
		} else throw new ApplicationException(BRAND_IS_EXIST);
	}

	@Override
	public CommonResponsePayload getAllBrand(int page, int size) {
		List<Brand> result = brandRepository.findAll();
		if (!result.isEmpty()){
			return new CommonResponsePayload().getCommonResponse(page, size, result);
		}
		else return null;
	}

	@Override
	public CommonResponsePayload getBrandbyKeyword(int page, int size, String keyword) {
		List<Brand> result = brandRepository.findAll(new BrandSpecification().nameLike(keyword));
		if (!result.isEmpty()){
			return new CommonResponsePayload().getCommonResponse(page, size, result);
		}
		return getAllBrand(page, size);
	}

	@Override
	public BrandResponse updateBrand(int id, BrandRequest brandRequest, CustomUserDetail customUserDetail) throws ApplicationException {
		Optional<Brand> brand = brandRepository.findById(id);
		brand.orElseThrow(() -> new ApplicationException(BRAND_NOT_FOUND));
		Brand update = brand.get();
		update.setName(brandRequest.getName());
		update.setDescription(brandRequest.getDescription());
		update.setUpdateDate(LocalDateTime.now());
		update.setUpdateBy(customUserDetail.getAccountDetail().getFirstName() + customUserDetail.getAccountDetail().getLastName());
		Brand result = brandRepository.save(update);
		return brandMapper.entityToResponse(result);
	}

	@Override
	public BrandResponse logicDeleteBrand(int id, CustomUserDetail customUserDetail) throws ApplicationException {
		Optional<Brand> brand = brandRepository.findById(id);
		brand.orElseThrow(() -> new ApplicationException(BRAND_NOT_FOUND));
		List<Product> products = productRepository.findAllByBrandIdAndIsDeletedFalse(id);
		if (products != null && !products.isEmpty()){
			products.forEach(items -> {
				items.setBrand(null);
			});
			productRepository.saveAll(products);
		}
		brand.get().setDeleted(true);
		brand.get().setCreatedDate(LocalDateTime.now());
		brand.get().setCreatedBy(customUserDetail.getAccountDetail().getFirstName() + customUserDetail.getAccountDetail().getLastName());
		Brand result = brandRepository.save(brand.get());
		return brandMapper.entityToResponse(result);
	}

	@Override
	public boolean physicalDeleteBrand(int id) throws ApplicationException {
		Optional<Brand> brand = brandRepository.findById(id);
		brand.orElseThrow(() -> new ApplicationException(BRAND_NOT_FOUND));
		List<Product> products = productRepository.findAllByBrandIdAndIsDeletedFalse(id);
		if (products != null && !products.isEmpty()){
			products.forEach(items -> {
				items.setBrand(null);
			});
			productRepository.saveAll(products);
		}
		brandRepository.deleteById(brand.get().getId());
		return true;
	}

	@Override
	public boolean isExists(String brandName) {
		return brandRepository.findAll(new BrandSpecification().nameLike(brandName)).isEmpty();
	}
}
