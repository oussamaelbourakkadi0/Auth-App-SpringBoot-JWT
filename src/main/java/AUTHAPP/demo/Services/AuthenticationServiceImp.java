package AUTHAPP.demo.Services;

import AUTHAPP.demo.Models.User;
import AUTHAPP.demo.Payloads.Requests.LoginRequest;
import AUTHAPP.demo.Payloads.Requests.SignUpRequest;
import AUTHAPP.demo.Payloads.Responses.JwtResponse;
import AUTHAPP.demo.Payloads.Responses.MessageResponse;
import AUTHAPP.demo.Security.JWT.JwtUtils;
import AUTHAPP.demo.Security.Services.UserDetailsImpl;
import AUTHAPP.demo.Services.Interfaces.AuthenticationService;
import AUTHAPP.demo.Services.Interfaces.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public AuthenticationServiceImp(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public MessageResponse signUp(SignUpRequest request) throws IOException {

        if (userService.isUserAlreadyRegisteredByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            return new MessageResponse("ERROR: USERNAME OR EMAIL ALREADY IN USE.");
        }

        if (!userService.checkRoleValidity(request.getRoleId())) {
            return new MessageResponse("ERROR: roleId IS NOT VALID, SHOULD BE EITHER 1, 2 OR 3.");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRoleId()
        );

        userService.addNewUser(user);
        return new MessageResponse("SUCCESS: USER HAS BEEN REGISTERED SUCCESSFULLY");
    }

    public JwtResponse authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return new JwtResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                role
        );
    }


}
