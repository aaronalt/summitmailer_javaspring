package com.summitapp.summitmailer;

import org.springframework.data.mongodb.repository.MongoRepository;

// @RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

}
