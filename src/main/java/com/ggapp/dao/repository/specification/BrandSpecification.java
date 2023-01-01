package com.ggapp.dao.repository.specification;
import com.ggapp.dao.entity.Brand;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class BrandSpecification {
	public Specification<Brand> nameLike(String name) {
		return (root, query, criteriaBuilder) ->  {
			List<Predicate> predicates = new ArrayList<>();
			if (name != null){
				predicates.add(criteriaBuilder.like(root.get("name").as(String.class), name));
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("id").as(String.class)), name));
			}
			Predicate finalPredicate = null;
			if (!predicates.isEmpty()){
				Predicate isDeleted = criteriaBuilder.equal(root.get("isDeleted").as(Boolean.class), false);
				Predicate search = criteriaBuilder.or(predicates.toArray(new Predicate[] {}));
				finalPredicate = criteriaBuilder.and(search, isDeleted);
			}
			return finalPredicate;
		};
	};
}
