package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.exceptions.UnauthorizedException;
import vascopanigi.MoneyZen.payloads.user.UserLoginDTO;
import vascopanigi.MoneyZen.security.JWTTools;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bCrypt;

    public String authenticateUtenteAndGenerateToken(UserLoginDTO payload) {
        User user = this.userService.findByEmail(payload.email());
        if (bCrypt.matches(payload.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Login failed, try again!");
        }
    }
}
