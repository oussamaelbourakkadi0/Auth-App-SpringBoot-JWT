package AUTHAPP.demo.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
            return "PUBLIC CONTENT";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR') or hasRole('COLLABORATOR')")
    public String userAccess() {
        return "USER CONTENT";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "ADMIN CONTENT";
    }

}
