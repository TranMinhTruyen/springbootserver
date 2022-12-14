package com.ggapp.dao.repository.mongo;

import com.ggapp.dao.document.ConfirmKey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfirmKeyRepository extends MongoRepository<ConfirmKey, String> {
	Optional<ConfirmKey> findByEmailEqualsAndTypeEquals(String email, String type);
	Optional<ConfirmKey> findByKeyEqualsAndEmailEqualsAndTypeEquals(String key, String email, String type);
	void deleteByEmailEqualsAndTypeEquals(String email, String type);
	void deleteByEmailEquals(String email);
}
