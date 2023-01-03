package com.ggapp.services.implement;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.common.utils.mapper.CategoryMapper;
import com.ggapp.dao.entity.Category;
import com.ggapp.dao.entity.Product;
import com.ggapp.common.dto.request.CategoryRequest;
import com.ggapp.common.dto.response.CategoryResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
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

import static com.ggapp.common.enums.MessageResponse.BRAND_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.CATEGORY_IS_EXIST;
import static com.ggapp.common.enums.MessageResponse.CATEGORY_NOT_FOUND;

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

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public CategoryResponse createCategory(CategoryRequest categoryRequest, CustomUserDetail customUserDetail) throws ApplicationException{
		if (categoryRequest != null && !isExists(categoryRequest.getName())){
			Category newCategory = new Category();
			newCategory.setName(categoryRequest.getName());
			newCategory.setDescription(categoryRequest.getDescription());
			newCategory.setCreatedDate(LocalDateTime.now());
			newCategory.setCreatedBy(customUserDetail.getAccountDetail().getFullName());
			Category result = categoryRepository.save(newCategory);
			return categoryMapper.entityToResponse(result);
		} else throw new ApplicationException(CATEGORY_IS_EXIST);
	}

	@Override
	public CommonResponsePayload getAllCategory(int page, int size) {
		List<Category> result = categoryRepository.findAll();
		List<CategoryResponse> categoryList = new ArrayList<>();
		result.forEach(item -> {
			CategoryResponse categoryResponse = new CategoryResponse();
			categoryResponse.setId(item.getId());
			categoryResponse.setName(item.getName());
			categoryResponse.setDescription(item.getDescription());
			categoryList.add(categoryResponse);
		});

		if (!categoryList.isEmpty()){
			return new CommonResponsePayload().getCommonResponse(page, size, categoryList);
		}
		return null;
	}

	@Override
	public CommonResponsePayload getCategoryByKeyword(int page, int size, String keyword) {
		List<Category> result = categoryRepository.findAll(new CategorySpecification(keyword));
		return new CommonResponsePayload().getCommonResponse(page, size, result);
	}

	@Override
	public CategoryResponse updateCategory(int id, CategoryRequest categoryRequest, CustomUserDetail customUserDetail)
			throws ApplicationException {
		Optional<Category> category = categoryRepository.findById(id);
		Category update = category.orElseThrow(() -> new ApplicationException(CATEGORY_NOT_FOUND));
		update.setName(categoryRequest.getName());
		update.setDescription(categoryRequest.getDescription());
		update.setUpdateDate(LocalDateTime.now());
		update.setUpdateBy(customUserDetail.getAccountDetail().getFullName());
		Category result = categoryRepository.save(update);
		return categoryMapper.entityToResponse(result);
	}

	@Override
	public CategoryResponse logicDeleteCategory(int id, CustomUserDetail customUserDetail) throws ApplicationException {
		Optional<Category> category = categoryRepository.findById(id);
		Category update = category.orElseThrow(() -> new ApplicationException(CATEGORY_NOT_FOUND));
		List<Product> products = productRepository.findAllByCategoryIdAndIsDeletedFalse(update.getId());
		if (products != null && !products.isEmpty()){
			products.forEach(items -> {
				items.setCategory(null);
				productRepository.save(items);
			});
		}
		update.setDeleted(true);
		update.setDeleteBy(customUserDetail.getAccountDetail().getFullName());
		update.setDeleteDate(LocalDateTime.now());
		Category result = categoryRepository.save(update);
		return categoryMapper.entityToResponse(result);
	}

	@Override
	public boolean physicalDeleteBrand(int id) throws ApplicationException {
		Optional<Category> category = categoryRepository.findById(id);
		Category result = category.orElseThrow(() -> new ApplicationException(CATEGORY_NOT_FOUND));
		List<Product> products = productRepository.findAllByCategoryIdAndIsDeletedFalse(result.getId());
		if (products != null && !products.isEmpty()){
			products.forEach(items -> {
				items.setCategory(null);
			});
			productRepository.saveAll(products);
		}
		categoryRepository.deleteById(result.getId());
		return true;
	}

	@Override
	public boolean isExists(String categoryName) throws ApplicationException {
		return !categoryRepository.findAll(new CategorySpecification(categoryName)).isEmpty();
	}
}
