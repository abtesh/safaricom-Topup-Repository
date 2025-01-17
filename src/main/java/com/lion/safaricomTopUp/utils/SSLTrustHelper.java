package com.lion.safaricomTopUp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.security.KeyStore;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
public class SSLTrustHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSLTrustHelper.class);

    public static HttpClient trustedClient(String trustStoreLocation, String trustStorePassword) {
        try {
            // Load the trust store (PKCS12 format, or JKS if needed)
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            trustStore.load(new FileInputStream(trustStoreLocation), trustStorePassword.toCharArray());

            // Initialize TrustManager with the trust store
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            X509TrustManager trustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];

            // Create the Netty SslContext using the trust manager
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(trustManager)
                    .sslProvider(SslProvider.OPENSSL)  // OpenSSL provider for better performance
                    .build();

            // Return HttpClient with the SSL context configured
            return HttpClient.create()
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        } catch (Exception e) {
            LOGGER.error("Unable to set SSL Context", e);
            throw new RuntimeException(e);
        }
    }
}


