package vascopanigi.MoneyZen.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vascopanigi.MoneyZen.entities.Plan;
import vascopanigi.MoneyZen.entities.Role;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.entities.UserPlan;
import vascopanigi.MoneyZen.enums.plan.PlanDuration;
import vascopanigi.MoneyZen.enums.plan.PlanType;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.payloads.user.ModifyUserDTO;
import vascopanigi.MoneyZen.payloads.user.NewUserDTO;
import vascopanigi.MoneyZen.repositories.UserRepository;
import vascopanigi.MoneyZen.utility.MailgunSender;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder bCrypt;

    @Autowired
    private PlanService planService;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private MailgunSender mailgunSender;

    public User findById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found!"));
    }

    public User saveUser(NewUserDTO body){
        this.userRepository.findByEmail(body.email()).ifPresent(user -> {
            throw new BadRequestException("The user with email: " + body.email() + ", already exist.");
        });
        this.userRepository.findByUsername(body.username()).ifPresent(user -> {
            throw new BadRequestException("The user with username: " + body.username() + ", already exist.");
        });

        User user = new User(body.name(), body.surname(), body.username(), body.email(), bCrypt.encode(body.password()));

        user.setAvatarURL("https://ui-avatars.com/api/?name=" + user.getName() + "+" + user.getSurname());
        List<Role> roleList = new ArrayList<>();
        Role userRole = roleService.findByRoleName("USER");
        roleList.add(userRole);
        user.setRolesList(roleList);
        List<UserPlan> userPlans  = new ArrayList<>();
        Plan freePlan = planService.findByPlanType(PlanType.FREE);
        freePlan.setPlanTypeAndDuration(PlanType.FREE, PlanDuration.MONTHLY);
        UserPlan userPlan = new UserPlan(user, freePlan, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        System.out.println(userPlan);
        userPlans.add(userPlan);
        System.out.println(userPlans);
        user.setUserPlans(userPlans);
        System.out.println(user);

        User saved = userRepository.save(user);
        mailgunSender.sendRegistrationEmail(saved);
        return saved;
    }

    public Page<User> getAllUsers(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User findByIdAndUpdate(UUID id, ModifyUserDTO payload) {
        User found = this.findById(id);
        found.setName(payload.name());
        found.setSurname(payload.surname());
        found.setUsername(payload.username());
        found.setEmail(payload.email());
        return userRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        User found = this.findById(id);
        this.userRepository.delete(found);
    }

    //----- gestione della save nel cloud -----
    public String uploadAvatarImage(MultipartFile file) throws IOException {
        return (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
    }

    public User patchNewAvatar(User currentUser, String avatarUrl) {
        User authenticatedUser = findById(currentUser.getId());
        authenticatedUser.setAvatarURL(avatarUrl);
        return this.userRepository.save(authenticatedUser);
    }
}
