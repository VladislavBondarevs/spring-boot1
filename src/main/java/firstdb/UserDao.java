package firstdb;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User findByUsername(String username);
    User save(User user);
    List<User> getAllUsers();
    void deleteUserById(Long id);
    Optional<User> getUserById(Long userId);
}
