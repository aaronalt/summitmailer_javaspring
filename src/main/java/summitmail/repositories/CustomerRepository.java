package summitmail.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import summitmail.models.Customer;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Customer findByEmail(@Param("email") String email);

    List<Customer> findByCountry(@Param("country") String country);

}
