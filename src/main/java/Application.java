

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.SpringApplication;

@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
//@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}