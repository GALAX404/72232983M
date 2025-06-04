package com.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

	@Bean
	public RestTemplate restTemp() {
		HttpComponentsClientHttpRequestFactory factory= new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(500);
		factory.setReadTimeout(500);
		return new RestTemplate(factory);
	}
}
