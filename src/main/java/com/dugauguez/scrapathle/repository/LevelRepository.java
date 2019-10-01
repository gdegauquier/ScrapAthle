package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.LevelEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends CrudRepository<LevelEntity, Integer> {

    LevelEntity findByCode(String code);

}
