package AUTHAPP.demo.Services;

import AUTHAPP.demo.Models.User;
import AUTHAPP.demo.Services.Interfaces.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImp implements UserService {
    private final String USER_CONF_FILE_PATH;
    private final ObjectMapper objectMapper;

    public UserServiceImp(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.USER_CONF_FILE_PATH = "./src/main/resources/users.conf.json";
    }
    public boolean isUserAlreadyRegisteredByUsernameOrEmail(String username, String email) throws IOException {
        List<User> userList = getUsersFromConfigFile();
        return userList.stream()
                .anyMatch(user -> user.getUsername().equals(username) || user.getEmail().equals(email));
    }

    public User getUserByUsername(String username) throws IOException {
        List<User> userList = getUsersFromConfigFile();
        User userFetched =
                userList.stream()
                        .filter(user -> user.getUsername().equals(username))
                        .findFirst()
                        .get();

        if (userFetched == null) {
            throw new NoSuchElementException("NO USER FOUND WITH USERNAME: " + username);
        }
        return userFetched;
    }

    public boolean checkRoleValidity(int roleId) {
        switch (roleId) {
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    public void addNewUser(User user) throws IOException {
        List<User> userList = getUsersFromConfigFile();
        userList.add(user);
        writeUsersToConfigFile(userList);
    }

    public String getRoleAsString(int roleId) {
        if (roleId == 1) {
            return "ADMIN";
        }
        else if (roleId == 2) {
            return "DIRECTOR";
        }
        else {
            return "MEMBER";
        }
    }

    private List<User> getUsersFromConfigFile() throws IOException {
        File usersConfFile = new File(USER_CONF_FILE_PATH);
        JsonNode rootNode = objectMapper.readTree(usersConfFile);
        JsonNode usersNode = rootNode.get("users");
        return objectMapper.convertValue(usersNode, new TypeReference<List<User>>() {});
    }

    private void writeUsersToConfigFile(List<User> userList) throws IOException {
        File usersConfFile = new File(USER_CONF_FILE_PATH);
        JsonNode rootNode = objectMapper.readTree(usersConfFile);
        ArrayNode updatedUsersNode = objectMapper.valueToTree(userList);
        ((ObjectNode) rootNode).set("users", updatedUsersNode);
        objectMapper.writeValue(usersConfFile, rootNode);
    }
}
