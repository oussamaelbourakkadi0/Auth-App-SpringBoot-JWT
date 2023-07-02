package AUTHAPP.demo.Models;

import javax.validation.constraints.NotBlank;

public class User {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    private int roleId;

    public User() {

    }

    public User(String username, String email, String password, int roleId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "username: " + username + ", password: " + password + ", email: " + email + ", roleId: " + roleId;
    }
}
