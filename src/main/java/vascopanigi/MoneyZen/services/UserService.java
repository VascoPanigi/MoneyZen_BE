package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.Role;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.payloads.user.NewUserDTO;
import vascopanigi.MoneyZen.payloads.user.NewUserResponseDTO;
import vascopanigi.MoneyZen.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCrypt;

    public User findById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found!"));
    }

    public User saveUser(NewUserDTO body){
        this.userRepository.findByEmail(body.email()).ifPresent(utente -> {
            throw new BadRequestException("The user with email: " + body.email() + ", already exist.");
        });
        User user = new User(body.name(), body.surname(), body.username(), body.email(), bCrypt.encode(body.password()));
//        (String name, String surname, String username, String email, String password)
        user.setAvatarURL("https://ui-avatars.com/api/?name=" + user.getName() + "+" + user.getSurname());

        List<Role> roleList = new ArrayList<>();
        user.setRolesList(roleList);
        return this.userRepository.save(user);
    }
}
