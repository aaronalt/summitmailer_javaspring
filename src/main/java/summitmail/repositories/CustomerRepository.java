package summitmail.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import summitmail.models.Customer;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Customer findByEmail(String email);

}
