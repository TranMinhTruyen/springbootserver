package com.ggapp.dao.repository.mongo;

import com.ggapp.dao.document.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Tran Minh Truyen on 04/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, Long> {
    @Query(value = "{$and : [{$or : [{account: :#{#account}}, {email: {$regex: :#{#account}, $options: 'i'}}]}, {password: :#{#password}}]}")
    Optional<Account> findByAccountOrEmailEqualsAndPasswordEquals(@Param("account") String account, @Param("password") String password);

    Optional<Account> findUserByAccount(String account);

    Optional<Account> findByEmailEqualsIgnoreCase(String email);
}
