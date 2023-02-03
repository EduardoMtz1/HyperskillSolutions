package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User toSave) {
        if (userRepository.findUserByEmail(toSave.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User already exist!");
        }
        String encodedPassword = passwordEncoder.encode(toSave.getPassword());
        toSave.setPassword(encodedPassword);
        return userRepository.save(toSave);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
