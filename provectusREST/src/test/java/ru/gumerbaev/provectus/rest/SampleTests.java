package ru.gumerbaev.provectus.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath:rest-spring-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SampleTests {

	@Test
	public void testContext() {
		assertNotNull(ContextProvider.getApplicationContext());
	}
}
