package com.wilson;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import lombok.SneakyThrows;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class PerformanceTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	@SneakyThrows
	public void setup() {
		mockMvc = webAppContextSetup(wac).build();
		// Delay for data cache synchronized
		Thread.sleep(1000);
	}

	@Test
	@SneakyThrows
	public void shouldReturnOkStatusFromRandomTest() {

		List<String> urls = new ArrayList<>();
		for (int i = 0; i < 500000; i++) {
			int id = new Random().nextInt(50);
			urls.add(String.format("/api/workers/%d/jobs", id));
		}
		urls.parallelStream().forEach(url -> sendAndReceive(url));
	}

	@SneakyThrows
	private void sendAndReceive(String url) {
		mockMvc
			.perform(get(url)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
