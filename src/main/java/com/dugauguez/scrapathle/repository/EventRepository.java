package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, String> {

}
