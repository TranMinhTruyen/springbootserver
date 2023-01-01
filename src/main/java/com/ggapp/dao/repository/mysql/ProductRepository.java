package com.ggapp.dao.repository.mysql;

import com.ggapp.dao.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

	@Query("select p from Product p where p.isDeleted = false and p.brand.id = :brandId")
	List<Product> findAllByBrandIdAndIsDeletedFalse(@Param("brandId") int brandId);

	@Query("select p from Product p " +
			"left join p.brand b " +
			"where p.isDeleted = false and lower(trim(b.name)) like lower(concat('%', trim(:brandName), '%'))")
	List<Product> findAllByBrandNameAndIsDeletedFalse(@Param("brandName") String brandName);

	@Query("select p from Product p where p.isDeleted = false and p.category.id = :categoryId")
	List<Product> findAllByCategoryIdAndIsDeletedFalse(@Param("categoryId") int categoryId);

	@Query("select p from Product p " +
			"left join p.category c " +
			"where p.isDeleted = false and lower(trim(c.name)) like lower(concat('%', trim(:categoryName), '%'))")
	List<Product> findAllByCategoryNameAndIsDeletedFalse(@Param("categoryName") String categoryName);

	@Query("select p from Product p " +
			"left join p.category c " +
			"left join p.brand b " +
			"where (lower(trim(c.name)) like lower(concat('%', trim(:categoryName), '%')) " +
			"and lower(trim(b.name)) like lower(concat('%', trim(:brandName), '%'))) and p.isDeleted = false")
	List<Product> findAllByCategoryNameAndBrandNameAndIsDeletedFalse(@Param("categoryName") String categoryName, @Param("brandName") String brandName);

	@Query("select p " +
			"from Product p " +
			"where (lower(trim(p.name)) like lower(concat('%', trim(:keyWord), '%')) " +
			"or lower(trim(p.productCode)) like lower(concat('%', trim(:keyWord), '%'))) and p.isDeleted = false")
	List<Product> findAllByNameEqualsIgnoreCaseAndIsDeletedFalse(@Param("keyWord") String keyWord);

	@Query("select p from Product p where p.isDeleted = false")
	List<Product> findAllByIsDeletedFalse();

	@Query("select p from Product p where p.deleteBy is null and p.deleteDate is null")
	List<Product> findAllByNewIsTrue();

	@Query("select p from Product p where p.id in :id and p.deleteBy is null and p.deleteDate is null")
	List<Product> findAllByIdIn(@Param("id") int[] id);
}