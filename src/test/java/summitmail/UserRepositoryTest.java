package summitmail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import summitmail.models.User;
import summitmail.repositories.UserRepository;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User user1 = new User("test@gmail.com", "1q2w3e4r5t6y");
        assertNull(user1.getId());
        this.userRepository.save(user1);
        assertNotNull(user1.getId());
    }

    @Test
    public void testDataRetrieve() {
        User user2 = userRepository.findByEmail("test@gmail.com");
        assertNotNull(user2);
        assertEquals("test@gmail.com", user2.getEmail());
    }

    @Test
    public void testDataUpdate() {
        User user3 = userRepository.findByEmail("test@gmail.com");
        assertEquals("test@gmail.com", user3.getEmail());
        user3.setEmail("info@test.com");
        assertEquals("info@test.com", user3.getEmail());
        assertEquals("1q2w3e4r5t6y", user3.getPassword());
        user3.setPassword("password123");
        assertEquals("password123", user3.getPassword());
    }

    @After
    public void tearDown() throws Exception {
        this.userRepository.deleteAll();
    }

}
