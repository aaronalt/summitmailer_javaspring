package summitmail.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import summitmail.models.Client;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends MongoRepository<Client, String> {

    Client findByName(@Param("name") String name);

    List<Client> findAllByCountry(@Param("country") String country);

}
