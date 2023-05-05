package com.example.shedlock;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S") // the default amount of time the lock should be kept in case the executing node dies
public class HelloShedlockApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloShedlockApplication.class, args);
	}



}
