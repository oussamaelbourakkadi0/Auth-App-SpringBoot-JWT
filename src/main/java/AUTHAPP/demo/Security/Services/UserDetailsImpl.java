package AUTHAPP.demo.Security.Services;

import AUTHAPP.demo.Models.ERole;
import AUTHAPP.demo.Models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String username;

    private String email;

    @JsonIgnore
    public String password;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UserDetailsImpl(String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        int roleId = user.getRoleId();
        System.out.println("ROLE ID DURING THE BUILD: " + roleId);
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (roleId == 1) {
            System.out.println("PASSED HERE");
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name());
            authorities.add(simpleGrantedAuthority);
        }
        else if (roleId == 2) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(ERole.ROLE_DIRECTOR.name());
            authorities.add(simpleGrantedAuthority);
        }
        else {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(ERole.ROLE_MEMBER.name());
            authorities.add(simpleGrantedAuthority);
        }

        return new UserDetailsImpl(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }

        UserDetailsImpl user = (UserDetailsImpl) obj;
        return Objects.equals(username, user.username);
    }
}
