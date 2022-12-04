package com.ggapp.services.implement;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.dao.entity.Category;
import com.ggapp.dao.entity.Product;
import com.ggapp.common.dto.request.CategoryRequest;
import com.ggapp.common.dto.response.CategoryResponse;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.dao.repository.mysql.CategoryRepository;
import com.ggapp.dao.repository.mysql.ProductRepository;
import com.ggapp.dao.repository.specification.CategorySpecification;
import com.ggapp.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class CategoryServiceImp implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public boolean createCategory(CategoryRequest categoryRequest, CustomUserDetail customUserDetail) {
		if (categoryRequest != null && !isExists(categoryRequest.getName())){
			Category newCategory = new Category();
			newCategory.setName(categoryRequest.getName());
			newCategory.setDescription(categoryRequest.getDescription());
			newCategory.setCreatedDate(LocalDateTime.now());
			newCategory.setCreatedBy(customUserDetail.getAccountDetail().getFirstName() + customUserDetail.getAccountDetail().getLastName());
			categoryRepository.save(newCategory);
			return true;
		}
		return false;
	}

	@Override
	public CommonResponse getAllCategory(int page, int size) {
		List<Category> result = categoryRepository.findAll();
		List<CategoryResponse> categoryList = new ArrayList<>();
		result.stream().forEach(item -> {
			CategoryResponse categoryResponse = new CategoryResponse();
			categoryResponse.setId(item.getId());
			categoryResponse.setName(item.getName());
			categoryResponse.setDescription(item.getDescription());
			categoryList.add(categoryResponse);
		});

		if (categoryList != null){
			return new CommonResponse().getCommonResponse(page, size, categoryList);
		}
		return null;
	}

	@Override
	public CommonResponse getCategoryByKeyword(int page, int size, String keyword) {
		List result = categoryRepository.findAll(new CategorySpecification(keyword));
		if (result != null){
			return new CommonResponse().getCommonResponse(page, size, result);
		}
		return getAllCategory(page, size);
	}

	@Override
	public CategoryResponse updateCategory(int id, CategoryRequest categoryRequest, CustomUserDetail customUserDetail) {
		if (update(id, categoryRequest, customUserDetail)){
			Optional<Category> category = categoryRepository.findById(id);
			Category result = category.get();
			CategoryResponse categoryResponse = new CategoryResponse();
			categoryResponse.setId(result.getId());
			categoryResponse.setName(result.getName());
			categoryResponse.setDescription(result.getDescription());
			return categoryResponse;
		}
		return null;
	}

	@Override
	public boolean deleteCategory(int id) {
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isPresent()){
			List<Product> products = productRepository.findAllByCategoryIdAndIsDeletedFalse(id);
			if (products != null && !products.isEmpty()){
				products.stream().forEach(items -> {
					items.setCategory(null);
					productRepository.save(items);
				});
			}
			categoryRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean isExists(String categoryName) {
		return !categoryRepository.findAll(new CategorySpecification(categoryName)).isEmpty();
	}

	private boolean update(int id, CategoryRequest categoryRequest, CustomUserDetail customUserDetail){
		Optional<Category> category = categoryRepository.findById(id);
		if (categoryRequest != null && category.isPresent()){
			Category update = category.get();
			update.setName(categoryRequest.getName());
			update.setDescription(categoryRequest.getDescription());
			update.setUpdateDate(LocalDateTime.now());
			update.setUpdateBy(customUserDetail.getAccountDetail().getFirstName() + customUserDetail.getAccountDetail().getLastName());
			categoryRepository.save(update);
			return true;
		}
		return false;
	}
}
