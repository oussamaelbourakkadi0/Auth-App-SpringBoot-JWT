package AUTHAPP.demo.Services.Interfaces;

import AUTHAPP.demo.Payloads.Requests.LoginRequest;
import AUTHAPP.demo.Payloads.Requests.SignUpRequest;
import AUTHAPP.demo.Payloads.Responses.JwtResponse;
import AUTHAPP.demo.Payloads.Responses.MessageResponse;

import java.io.IOException;

public interface AuthenticationService {
    MessageResponse signUp(SignUpRequest request) throws IOException;
    JwtResponse authenticateUser(LoginRequest request);
}
