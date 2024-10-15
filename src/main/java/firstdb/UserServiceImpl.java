package firstdb;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public UserServiceImpl( PasswordEncoder passwordEncoder, UserDao userDao) {
        super();

        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User save(UserDto userDto) {
        User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),
                userDto.getFullname(), userDto.getEmail(), userDto.getRole());
        return userDao.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }


    @Override
    public void deleteUserById(Long id) {
        userDao.deleteUserById(id);
    }

    @Override
    public boolean hasRole(User user, String role) {
        return user.getRole().equals(role);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userDao.getUserById(userId);
    }

}




