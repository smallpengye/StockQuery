package cn.edu.hfuu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableScheduling
public class WebMain {


    public static void main(String[] args) {
        SpringApplication.run(WebMain.class, args);
    }

}
