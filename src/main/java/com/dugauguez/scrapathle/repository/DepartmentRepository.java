package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.DepartmentEntity;
import com.dugauguez.scrapathle.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Integer> {

    DepartmentEntity findByCode(String code);
}
