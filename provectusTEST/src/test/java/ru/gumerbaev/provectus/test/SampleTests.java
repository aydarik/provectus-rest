package ru.gumerbaev.provectus.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(locations = {"classpath:rest-spring-context.xml", "classpath:test-spring-context.xml"})
public class SampleTests {

	private static String ENDPOINT_URI = "/user/";
	private static String USER_NAME = "test_user";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void available() {
		ResponseEntity<String> list = restTemplate.getForEntity(ENDPOINT_URI, String.class);
		assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void add() {
		Long id = Long.parseLong(addForID());
		ResponseEntity<String> get = restTemplate.getForEntity(ENDPOINT_URI + id, String.class);
		assertThat(get.getBody()).isEqualTo(USER_NAME);
	}

	@Test
	public void del() {
		Long id = Long.parseLong(addForID());
		ResponseEntity<String> get = restTemplate.getForEntity(ENDPOINT_URI + id, String.class);
		assertThat(get.getBody()).isEqualTo(USER_NAME);

		restTemplate.delete(ENDPOINT_URI + id);
		get = restTemplate.getForEntity(ENDPOINT_URI + id, String.class);
		assertThat(get.getBody()).isNotEqualTo(USER_NAME);
	}

	private String addForID() {
		ResponseEntity<String> add = restTemplate.postForEntity(ENDPOINT_URI + USER_NAME, null, String.class);
		return add.getBody();
	}
}
