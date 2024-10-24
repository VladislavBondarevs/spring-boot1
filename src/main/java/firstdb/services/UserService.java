package firstdb.services;


import firstdb.model.User;
import firstdb.services.user.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    User save(UserDto userDto);
    List<User> getAllUsers();

    boolean hasRole(User user, String role);
    Optional<User> getUserById(Long userId);
    void deleteUserAndDependencies(Long userId);

    void deleteUserById(Long id);
    User findById(Long id);
}