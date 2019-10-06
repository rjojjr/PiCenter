package com.kirchnersolutions.PiCenter.entites;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends CrudRepository<Read, Long> {
}
