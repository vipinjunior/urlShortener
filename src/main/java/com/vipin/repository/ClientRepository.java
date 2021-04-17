package com.vipin.repository;

import com.vipin.entity.ClientConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository  extends CrudRepository<ClientConfiguration, Long> {
    Optional<ClientConfiguration> findByHostName(String name);

}
