package co.edu.escuelaing.techcup.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUsersApplication.class, args);
    }
}
