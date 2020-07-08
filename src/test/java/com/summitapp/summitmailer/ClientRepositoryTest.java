package com.summitapp.summitmailer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository repository;

    @Before
    public void setUp() {
        Client client1 = new Client("infoTech", "Testland", "www.info.tech", "test@info.tech");
        assertNull(client1.getId());
        this.repository.save(client1);
        assertNotNull(client1.getId());
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

    @After
    public void tearDown() throws Exception {
        this.repository.deleteAll();
    }

}
