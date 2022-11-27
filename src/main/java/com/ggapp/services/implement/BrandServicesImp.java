package com.ggapp.services.implement;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.dao.entity.Brand;
import com.ggapp.dao.entity.Product;
import com.ggapp.common.dto.request.BrandRequest;
import com.ggapp.common.dto.response.BrandResponse;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.dao.repository.mysql.BrandRepository;
import com.ggapp.dao.repository.mysql.ProductRepository;
import com.ggapp.dao.repository.specification.BrandSpecification;
import com.ggapp.services.BrandServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
public class BrandServicesImp implements BrandServices {

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public boolean createBrand(BrandRequest brandRequest, CustomUserDetail customUserDetail) {
		if (brandRequest != null && !isExists(brandRequest.getName())){
			Brand newBrand = new Brand();
			newBrand.setName(brandRequest.getName());
			newBrand.setDescription(brandRequest.getDescription());
			newBrand.setCreatedDate(LocalDateTime.now());
			newBrand.setCreatedBy(customUserDetail.getUser().getFirstName() + customUserDetail.getUser().getLastName());
			brandRepository.save(newBrand);
			return true;
		}
		return false;
	}

	@Override
	public CommonResponse getAllBrand(int page, int size) {
		List<Brand> result = brandRepository.findAll();
		if (!result.isEmpty()){
			return new CommonResponse().getCommonResponse(page, size, result);
		}
		else return null;
	}

	@Override
	public CommonResponse getBrandbyKeyword(int page, int size, String keyword) {
		List<Brand> result = brandRepository.findAll(new BrandSpecification(keyword));
		if (!result.isEmpty()){
			return new CommonResponse().getCommonResponse(page, size, result);
		}
		return getAllBrand(page, size);
	}

	@Override
	public BrandResponse updateBrand(int id, BrandRequest brandRequest, CustomUserDetail customUserDetail) {
		if (update(id, brandRequest, customUserDetail)){
			Optional<Brand> brand = brandRepository.findById(id);
			Brand result = brand.get();
			BrandResponse brandResponse = new BrandResponse();
			brandResponse.setId(result.getId());
			brandResponse.setName(result.getName());
			brandResponse.setDescription(result.getDescription());
			return brandResponse;
		}
		return null;
	}

	@Override
	public boolean deleteBrand(int id) {
		Optional<Brand> brand = brandRepository.findById(id);
		if (brand.isPresent()){
			List<Product> products = productRepository.findAllByBrandIdAndIsDeletedFalse(id);
			if (products != null && !products.isEmpty()){
				products.forEach(items -> {
					items.setCategory(null);
					productRepository.save(items);
				});
			}
			brandRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean isExists(String brandName) {
		return !brandRepository.findAll(new BrandSpecification(brandName)).isEmpty();
	}

	private boolean update (int id, BrandRequest brandRequest, CustomUserDetail customUserDetail){
		Optional<Brand> brand = brandRepository.findById(id);
		if (brandRequest != null && brand.isPresent()){
			Brand update = brand.get();
			update.setName(brandRequest.getName());
			update.setDescription(brandRequest.getDescription());
			update.setUpdateDate(LocalDateTime.now());
			update.setUpdateBy(customUserDetail.getUser().getFirstName() + customUserDetail.getUser().getLastName());
			brandRepository.save(update);
			return true;
		}
		return false;
	}
}
