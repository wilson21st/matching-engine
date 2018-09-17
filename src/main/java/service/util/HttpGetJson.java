package service.util;

import java.lang.reflect.Type;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.SneakyThrows;

public class HttpGetJson {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	@SneakyThrows
	public static Object sendAndReceive(String url, Type type) {
		// simple HTTP client without error handling, e.g. retry or timeout
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory((HttpRequest request) -> {
			request.setParser(new JsonObjectParser(JSON_FACTORY));
		});
		HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(url));
		return request.execute().parseAs(type);
	}
}
