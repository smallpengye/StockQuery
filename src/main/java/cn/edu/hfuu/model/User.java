package cn.edu.hfuu.model;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Repository
@Table(name = "users")
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;

    private String password;

    private String roles;

    private String focusstock;

    public String getFocusstock() {
        return focusstock;
    }

    public void setFocusstock(String focusstock) {
        this.focusstock = focusstock;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        String[] rss = getRoles().split(",");
//        List<GrantedAuthority> gas = new ArrayList<>();
//        for (String rs : rss) {
//            GrantedAuthority ga = new GrantedAuthority2(rs);
//            gas.add(ga);
//        }
//        return gas;
//        String roles = Optional.ofNullable(getRoles()).orElse("");
        String roles = getRoles() == null ? "" : getRoles();
        return Stream.of(roles.split(",")).map(s -> (GrantedAuthority) () -> s).collect(Collectors.toList());

    }

//    public static class GrantedAuthority2 implements GrantedAuthority {
//        private final String authority;
//
//        public GrantedAuthority2(String authority) {
//            this.authority = authority;
//        }
//
//        @Override
//        public String getAuthority() {
//            return authority;
//        }
//    }

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
}
