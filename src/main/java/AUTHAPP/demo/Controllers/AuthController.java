package AUTHAPP.demo.Controllers;

import AUTHAPP.demo.Payloads.Requests.LoginRequest;
import AUTHAPP.demo.Payloads.Requests.SignUpRequest;
import AUTHAPP.demo.Payloads.Responses.JwtResponse;
import AUTHAPP.demo.Payloads.Responses.MessageResponse;
import AUTHAPP.demo.Services.Interfaces.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private AuthenticationService authenticationService;
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest request) {
        try {
            MessageResponse response = authenticationService.signUp(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("signIn")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            JwtResponse response = authenticationService.authenticateUser(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
