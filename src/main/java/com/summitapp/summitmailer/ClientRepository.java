package com.summitapp.summitmailer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends MongoRepository<Client, String> {

    List<Client> getAllClients();

    Client findByName(@Param("name") String name);

    List<Client> findByCountry(@Param("country") String country);

}
