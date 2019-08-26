package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.Address;
import com.dugauguez.scrapathle.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

}
