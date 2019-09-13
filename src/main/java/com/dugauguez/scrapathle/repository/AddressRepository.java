package com.dugauguez.scrapathle.repository;

import com.dugauguez.scrapathle.entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    Address findByNameAndTownAndPostalCode(String name, String town, String postalCode);

    List<Address> findByTypeAndPostalCodeStartsWith(String type, String postalCode);
}
