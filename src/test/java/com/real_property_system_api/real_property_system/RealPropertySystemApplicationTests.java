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
		String accessToken = "eyhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGlfc2VydmVyIiwidXNlcm5hbWUiOiJtYW5zdXJjaGlrMTIzIiwiZXhwIjoxNzMxMTU0MjQzLCJpYXQiOjE3MzExNTMzNDN9.BlKjBUCnUroz3KXA5rA24y_w8tdq0bjMTevo9dtkejM";
		var result = jwtManager.checkForValidity(accessToken);

		assertEquals(JwtError.NoError, result);

	}

}
