package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends CrudRepository<Region, Integer> {
    public List<Region> findAllByOrderByTownAsc();;
}
