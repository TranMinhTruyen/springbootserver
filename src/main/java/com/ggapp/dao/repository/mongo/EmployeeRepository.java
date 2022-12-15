package com.ggapp.dao.repository.mongo;

import com.ggapp.dao.document.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Tran Minh Truyen on 09/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
public interface EmployeeRepository extends MongoRepository<Employee, Long> {
}
