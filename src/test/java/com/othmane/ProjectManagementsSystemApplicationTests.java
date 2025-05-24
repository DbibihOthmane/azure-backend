package com.othmane;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest(
		webEnvironment = WebEnvironment.NONE,
		properties = {
				"spring.profiles.active=test",
				"spring.main.allow-bean-definition-overriding=true",
				"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration"
		}
)
@ComponentScan(basePackages = {"com.othmane"})
class ProjectManagementsSystemApplicationTests {

	@Test
	void contextLoads() {
	}
}