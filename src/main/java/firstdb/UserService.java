package firstdb;


import java.util.List;
import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    User save(UserDto userDto);
    List<User> getAllUsers();


    void assignRole(User user, String role);


    boolean hasRole(User user, String role);
    Optional<User> getUserById(Long userId);


    void deleteUserById(Long id);

}