package com.saber.orderservice.config;

import jakarta.validation.Valid;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${spring.restTemplate.connectTimeout}")
    private Long connectTimeout;
    @Value("${spring.restTemplate.readTimeout}")
    private Long readTimeout;
    @Value("${spring.restTemplate.totalConnection}")
    private Integer totalConnection;
    @Value("${spring.restTemplate.connectionPerRoute}")
    private Integer connectionPerRoute;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        //https://howtodoinjava.com/spring-boot2/resttemplate/resttemplate-httpclient-java-config/
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
//                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(totalConnection);
        connectionManager.setDefaultMaxPerRoute(connectionPerRoute);

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectionRequestTimeout(Duration.ofMillis(connectTimeout));
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();

        restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }
}
