package org.example.sbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SubscriptionBillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionBillingServiceApplication.class, args);
    }

}
