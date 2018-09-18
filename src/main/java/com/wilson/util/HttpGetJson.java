package com.wilson.util;

import org.springframework.stereotype.Component;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.SneakyThrows;

@Component
public class HttpGetJson {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	@SneakyThrows
	public <T> T sendAndReceive(String url, Class<T> dataClass) {
		// simple HTTP client without error handling, e.g. retry or timeout
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory((HttpRequest request) -> {
			request.setParser(new JsonObjectParser(JSON_FACTORY));
		});
		HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(url));
		return request.execute().parseAs(dataClass);
	}

	@SneakyThrows
	public String sendAndReceive(String url) {
		// simple HTTP client without error handling, e.g. retry or timeout
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
		HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(url));
		return request.execute().parseAsString();
	}
}
