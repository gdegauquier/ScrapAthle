package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.EventTypeEntity;
import com.dugauguez.scrapathle.entity.TownEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends CrudRepository<EventTypeEntity, Integer> {

    EventTypeEntity findByCode(String code);

}
