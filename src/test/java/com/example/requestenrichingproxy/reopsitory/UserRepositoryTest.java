package com.example.requestenrichingproxy.reopsitory;

import com.example.requestenrichingproxy.entity.AppUser;
import com.example.requestenrichingproxy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Use in-memory database
@TestPropertySource(locations = "classpath:application-test.properties") // Optional: use this if you have test-specific properties
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindById() {
        // Given
        AppUser user = AppUser.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthPlace("USA, Ohio")
                .sex("Female")
                .build();
        user = userRepository.save(user);

        // When
        Optional<AppUser> foundUser = userRepository.findById(user.getId());

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("Jane");
        assertThat(foundUser.get().getLastName()).isEqualTo("Smith");
    }

    @Test
    public void testFindByFirstNameAndLastName() {
        // Given
        AppUser user = AppUser.builder()
                .firstName("John")
                .lastName("Doe")
                .birthPlace("USA, Ohio")
                .sex("Male")
                .build();
        userRepository.save(user);

        // When
        Optional<AppUser> foundUser = userRepository.findByFirstNameAndLastName("John", "Doe");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testFindByFirstNameAndLastName_NotFound() {
        // When
        Optional<AppUser> foundUser = userRepository.findByFirstNameAndLastName("Nonexistent", "User");

        // Then
        assertThat(foundUser).isNotPresent();
    }
}