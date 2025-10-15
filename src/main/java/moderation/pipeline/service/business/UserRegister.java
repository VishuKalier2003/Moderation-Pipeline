package moderation.pipeline.service.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moderation.pipeline.model.storage.User;
import moderation.pipeline.repository.UserRepo;

@Service
public class UserRegister {

    @Autowired
    private UserRepo userRepo;

    public String registerUser(String username, String password) {
        User user = new User(username, password);
        for(User u : userRepo.findAll()) {
            if(u.getName().equals(username)) {
                return "Username already exists";
            }
        }
        userRepo.save(user);
        return "User registered successfully";
    }
}
