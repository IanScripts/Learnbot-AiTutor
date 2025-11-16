package cs3220.aitutor.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    // username -> password  (for homework/demo; later use hashing + DB)
    private final Map<String, String> users = new ConcurrentHashMap<>();

    public UserService() {
        // Optional: seed a demo user
        users.put("demo", "password");
    }

    public boolean register(String username, String password) {
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            return false;
        }
        if (users.containsKey(username)) {
            return false; // already taken
        }
        users.put(username, password);
        return true;
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        String stored = users.get(username);
        return stored != null && stored.equals(password);
    }
}
