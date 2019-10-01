package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.LeagueEntity;
import com.dugauguez.scrapathle.entity.TownEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownRepository extends CrudRepository<TownEntity, Integer> {

    TownEntity findByName(String name);

}
