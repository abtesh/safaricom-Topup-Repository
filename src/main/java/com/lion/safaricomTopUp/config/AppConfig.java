//package com.lion.safaricomTopUp.config;
//
//import io.netty.channel.ChannelOption;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;
//
//import javax.net.ssl.SSLException;
//import java.time.Duration;
//
//public class WebClientConfig {
//
//    @Bean
//    public WebClient.Builder webClientBuilder() {
//
//        HttpClient httpClient = HttpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)  // Set connection timeout to 15 seconds
//                .responseTimeout(Duration.ofSeconds(15))
//                .secure(sslContextSpec -> {
//                    try {
//                        sslContextSpec.sslContext(
//                                SslContextBuilder.forClient()
//                                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                                        .build());
//                    } catch (SSLException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//
//        return WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient));
//    }
//
//}


























package com.lion.safaricomTopUp.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
public class AppConfig {

    @Value("${api.username}")
    private String username;

    @Value("${api.password}")
    private String password;

    @Value("${api.base-url}")
    private String baseUrl;

    @Value("${dxl.trust-store}")
    private String trustStoreLocation;

    @Value("${dxl.password}")
    private String trustStorePassword;

    @Bean
    public WebClient.Builder webClientBuilder() throws Exception {
        // Create the HttpClient with SSL configuration
        HttpClient httpClient = createHttpClientWithTrustStore();

        // Return the WebClient builder with the custom HTTP client
        return WebClient.builder()
                .baseUrl(baseUrl) // Set your base URL here
                .clientConnector(new ReactorClientHttpConnector(httpClient)) // Use custom HttpClient with SSL
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(ExchangeFilterFunctions.basicAuthentication(username, password)); // Optional basic auth filter
//                .defaultHeader("x-source-system", "STEP")
//                .defaultHeader("x-source-identity-token", "U2FmYXJpY29tOmUyZTplc2JldDpBdXRvbWF0aW9u");
    }

    // Method to create HttpClient with the TrustStore
    private HttpClient createHttpClientWithTrustStore() throws Exception {
        // Load the trust store (PKCS12 format)
        File trustStoreFile = new File(trustStoreLocation);
        char[] trustStorePasswordNew = trustStorePassword.toCharArray();

        // Load the certificates from the trust store
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream trustStoreStream = new FileInputStream(trustStoreFile)) {
            keyStore.load(trustStoreStream, trustStorePasswordNew);
        }

        // Create TrustManagerFactory with the loaded TrustStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        // Create SSLContext using the TrustManagerFactory
        javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        // Use SslContextBuilder to build SslContext for Netty
        SslContext nettySslContext = SslContextBuilder.forClient()
                .trustManager(trustManagerFactory)  // Use the TrustManagerFactory
                .build();

        // Create and return the HttpClient with the SSLContext
        return HttpClient.create()
                .secure(ssl -> ssl.sslContext(nettySslContext));  // Pass the Netty SslContext to HttpClient
    }
}
