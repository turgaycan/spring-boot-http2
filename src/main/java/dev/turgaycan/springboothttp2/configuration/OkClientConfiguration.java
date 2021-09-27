package dev.turgaycan.springboothttp2.configuration;

import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static dev.turgaycan.springboothttp2.constant.Constants.SSL_SERVER_PEM_FILE_NAME;

@Log4j2
@Configuration
public class OkClientConfiguration {

    @Value("server.ssl.trust-store-password")
    private String trustStorePassword;

    @Value("server.ssl.trust-store-alias")
    private String trustStoreAlias;

    @Value("${server.ssl.client-ssl-enabled:true}")
    private boolean clientSslEnabled;

    @Bean
    @Qualifier("okRestTemplate")
    public RestTemplate okRestTemplate() throws NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, IOException {
        OkHttpClient.Builder okHttpClientBuilder = clientSslEnabled ? buildSslHttpClient() : buildUnsafeOkHttpClient();

        final OkHttpClient okHttpClient = okHttpClientBuilder
                .protocols(List.of(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .addInterceptor(new LoggingInterceptor())
                .addNetworkInterceptor(new LoggingInterceptor())
                .build();
        final OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        return new RestTemplate(requestFactory);
    }

    private OkHttpClient.Builder buildSslHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, trustStorePassword.toCharArray());
        InputStream certInputStream = new ClassPathResource(SSL_SERVER_PEM_FILE_NAME).getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(certInputStream);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        while (bufferedInputStream.available() > 0) {
            Certificate cert = certificateFactory.generateCertificate(bufferedInputStream);
            keyStore.setCertificateEntry(trustStoreAlias, cert);
        }
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);

        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
        okHttpClientBuilder.hostnameVerifier((hostname, session) -> true);
        return okHttpClientBuilder;
    }

    private OkHttpClient.Builder buildUnsafeOkHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        okHttpClientBuilder.hostnameVerifier((hostname, session) -> true);
        return okHttpClientBuilder;
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            final long start = System.nanoTime();
            LOG.info("Sending request {} on {} {} {}",
                    request.url(), chain.connection(), request.headers());

            Response response = chain.proceed(request);

            final long finish = System.nanoTime();
            LOG.info("Received response for {} in {} ms {}",
                    response.request().url(), TimeUnit.NANOSECONDS.toMillis(finish - start), response.headers());

            return response;
        }
    }

}
