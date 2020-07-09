package summitmail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import summitmail.models.Client;
import summitmail.repositories.ClientRepository;

import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository repository;

    @Before
    public void setUp() {
        Client test1 = new Client("infoTech", "Testland", "www.info.tech", "test@info.tech");
        assertNull(test1.getId());
        Client test2 = new Client("TestTech", "Testland", "www.info-tech.com", "testtech@gmail.com");
        Client test3 = new Client("WebTesters", "Testistan", "webtesters.com", "webtest@testers.com");
        Client test4 = new Client("Coderz", "Testistan", "coderz.co", "info@coderz.co");
        this.repository.save(test1);
        this.repository.save(test2);
        this.repository.save(test3);
        this.repository.save(test4);
        assertNotNull(test1.getId());
        assertNotNull(test2.getName());
        assertNotNull(test1.getCountry());
        assertNotNull(test1.getWebsite());
    }

    @Test
    public void testDataRetrieve() {
        Client client2 = repository.findByName("infoTech");
        assertNotNull((client2));
        assertEquals("infoTech", client2.getName());
    }

    @Test
    public void testDataUpdate() {
        Client clientUpdate = repository.findByName("infoTech");
        assertEquals("test@info.tech", clientUpdate.getEmail());
        clientUpdate.setEmail("hello@info.tech");
        assertEquals("hello@info.tech", clientUpdate.getEmail());
    }

    @Test
    public void testFindAllByCountry() {
        List<Client> allTestland = repository.findAllByCountry("Testland");
        assertNotNull(allTestland);
    }

    @After
    public void tearDown() throws Exception {
        this.repository.deleteAll();
    }

}
