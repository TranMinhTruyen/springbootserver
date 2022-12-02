package com.ggapp.dao.repository.specification;

import com.ggapp.dao.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public final class ProductSpecification{
	public Specification<Product> nameLike(String name) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (name != null){
					predicates.add(cb.like(cb.lower(root.get("name").as(String.class)), name.concat("%")));
				}
				Predicate search = null;
				if (!predicates.isEmpty()){
					search = cb.or(predicates.toArray(new Predicate[] {}));
				}
				return search;
			}
		};
	}
}
