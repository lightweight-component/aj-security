package com.foo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class FooApp {
    public static void main(String[] args) {
        SpringApplication.run(FooApp.class, args);
    }
}
