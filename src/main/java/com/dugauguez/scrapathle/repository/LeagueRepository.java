package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.LeagueEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends CrudRepository<LeagueEntity, Integer> {

    LeagueEntity findByCode(String code);

}
