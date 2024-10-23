package firstdb;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DutyScheduleService scheduleService;

    public UserServiceImpl( PasswordEncoder passwordEncoder, UserRepository userRepository, DutyScheduleService scheduleService) {
        super();

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.scheduleService = scheduleService;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null); // Реализуем метод
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(UserDto userDto) {
        User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),
                userDto.getFullname(), userDto.getEmail(), userDto.getPhonenumber(), userDto.getRole());
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean hasRole(User user, String role) {
        return user.getRole().equals(role);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    public void deleteUserAndDependencies(Long userId) {
        List<DutySchedule> schedules = scheduleService.getAllSchedules();
        for (DutySchedule schedule : schedules) {
            scheduleService.deleteById(schedule.getId());
        }
        userRepository.deleteById(userId);
    }
}




