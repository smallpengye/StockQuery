package cn.edu.hfuu;

import cn.edu.hfuu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by huzhipeng on 2017/3/8.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter{
    @Autowired
    private UserService userService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userService);
        http
                .authorizeRequests()
                    .antMatchers("/login.html","/register","/test1","/css/**", "/html/**", "/img/**","/js/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                   .loginPage("/login.html")
                   .loginProcessingUrl("/login")
                //.defaultSuccessUrl("/html/index.html")
                  .defaultSuccessUrl("/html/index.html", true)
                  .permitAll()
                .and()
                .logout().logoutSuccessUrl("/login.html")
                .and()
                .httpBasic().and()
                .logout()
                .permitAll();

        http.csrf().disable();
    }



}

