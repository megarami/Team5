package com.tim5.demo;

import com.tim5.demo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;

@SpringBootApplication

public class TravelActivityTrackerApplication {

    public static void main(String[] args) {

        SpringApplication.run(TravelActivityTrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx){
        return args->{
            System.out.println("Let's inspect the beans provided by Spring boot");
            String[] beanNames=ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for(String beanName : beanNames){
                // System.out.println(beanName);
            }

        };
    }
}
