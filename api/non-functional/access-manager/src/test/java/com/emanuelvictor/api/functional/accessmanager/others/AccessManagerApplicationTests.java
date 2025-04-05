package com.emanuelvictor.api.functional.accessmanager.others;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootTest
class AccessManagerApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(new BCryptPasswordEncoder(12).encode("browser"));
	}

}
