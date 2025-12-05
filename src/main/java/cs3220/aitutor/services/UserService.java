package cs3220.aitutor.services;

import cs3220.aitutor.model.UserAccount;
import cs3220.aitutor.repositories.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserAccountRepository userRepository;

    public UserService(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(String username, String password) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            return false;
        }

        if (userRepository.existsByUsername(username)) {
            return false;
        }

        UserAccount account = new UserAccount(username.trim(), password);
        userRepository.save(account);
        return true;
    }


    public boolean register(String username, String password) {
        return registerUser(username, password);
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;

        return userRepository.findByUsername(username.trim())
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}

