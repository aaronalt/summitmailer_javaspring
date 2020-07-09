package summitmail.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import summitmail.models.User;

// @RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

}
