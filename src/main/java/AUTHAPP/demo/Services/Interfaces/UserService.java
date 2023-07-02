package AUTHAPP.demo.Services.Interfaces;

import AUTHAPP.demo.Models.User;

import java.io.IOException;

public interface UserService {
    boolean isUserAlreadyRegisteredByUsernameOrEmail(String username, String email) throws IOException;
    User getUserByUsername(String username) throws IOException;
    boolean checkRoleValidity(int roleId);
    void addNewUser(User user) throws IOException;
    String getRoleAsString(int roleId);

}
