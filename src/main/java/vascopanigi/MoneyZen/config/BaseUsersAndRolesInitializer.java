package vascopanigi.MoneyZen.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vascopanigi.MoneyZen.entities.Role;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.repositories.RoleRepository;
import vascopanigi.MoneyZen.repositories.UserRepository;
import vascopanigi.MoneyZen.services.RoleService;

@Configuration
public class BaseUsersAndRolesInitializer {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @PostConstruct
    public void initializeRoles() {
        try {
            if (roleRepository.findByRoleName("ADMIN").isEmpty()) {
                roleRepository.save(new Role("ADMIN"));
            }
            if (roleRepository.findByRoleName("USER").isEmpty()) {
                roleRepository.save(new Role("USER"));
            }

        } catch (BadRequestException e) {
            System.out.println("Roles initialization failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during database initialization: " + e.getMessage());
        }

    }
}

