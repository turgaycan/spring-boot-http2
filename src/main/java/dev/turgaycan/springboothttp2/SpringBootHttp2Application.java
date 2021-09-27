package dev.turgaycan.springboothttp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
}
)
public class SpringBootHttp2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootHttp2Application.class, args);
    }

}
