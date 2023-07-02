package AUTHAPP.demo.Services;

import AUTHAPP.demo.Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Aspect
@Component
public class UserServiceImpAspect {
    @After("execution(* AUTHAPP.demo.Services.UserServiceImp.addNewUser(AUTHAPP.demo.Models.User))")
    public void afterAddingNewUser() {
        String usersConfigFilePath = "./src/main/resources/users.conf.json";
        try (FileReader fileReader = new FileReader(usersConfigFilePath)) {
            Gson gson = new Gson();
            Object json = gson.fromJson(fileReader, Object.class);

            Gson formattedGson = new GsonBuilder().setPrettyPrinting().create();
            String formattedJson = formattedGson.toJson(json);

            try (FileWriter fileWriter = new FileWriter(usersConfigFilePath)) {
                fileWriter.write(formattedJson);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
