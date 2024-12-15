package com.real_property_system_api.real_property_system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.real_property_system_api.real_property_system.responses.JwtError;
import com.real_property_system_api.real_property_system.services.JwtManager;

@SpringBootTest
class RealPropertySystemApplicationTests {

	@Autowired
	private JwtManager jwtManager;

	@Test
	void test_CheckValidToken() 
	{
	}

}
