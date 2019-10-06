package com.kirchnersolutions.PiCenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.kirchnersolutions.PiCenter.entites")
@EntityScan("com.kirchnersolutions.PiCenter.entites")
@ComponentScan({"com.kirchnersolutions.PiCenter.entites", "com.kirchnersolutions.PiCenter.Configuration", "com.kirchnersolutions.PiCenter.dev", "com.kirchnersolutions.PiCenter.servers.http",
		"com.kirchnersolutions.PiCenter.servers.objects", "com.kirchnersolutions.PiCenter.servers.socket"})
public class PiCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiCenterApplication.class, args);
	}

}
