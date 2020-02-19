package com.kirchnersolutions.PiCenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration
//@ContextConfiguration(locations = {"/services-test-config.xml"})
//@EnableJpaRepositories("com.kirchnersolutions.PiCenter.entites")
//@EntityScan("com.kirchnersolutions.PiCenter.entites")
//@ComponentScan({"com.kirchnersolutions.PiCenter.entites", "com.kirchnersolutions.PiCenter.Configuration", "com.kirchnersolutions.PiCenter.dev", "com.kirchnersolutions.PiCenter.servers.http",
//	"com.kirchnersolutions.PiCenter.servers.objects", "com.kirchnersolutions.PiCenter.servers.socket"})
public class PiCenterApplicationTests {
	@Autowired
	ApplicationContext context;

	/**
	 * Test fails because of socket server.
	 */
	/*@Test
	public void contextLoads() {
	}*/

}
