package firstdb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldCreateUser() {
        // Arrange
        UserDto userDto = new UserDto("testUser", "password", "fullName", "email@example.com", "USER");

        User user = new User(userDto.getUsername(), "encodedPassword", userDto.getFullname(), userDto.getEmail(), userDto.getRole());

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.save(userDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getUsername());
    }
}