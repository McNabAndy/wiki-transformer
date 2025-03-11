package cz.vojtechsika.wiki_transformer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configuration class for setting up a {@link RestClient} bean.
 * Provides a centralized way to configure and manage the REST client used for HTTP communication.
 */
@Configuration
public class RestClientConfig {


    /**
     * Creates and configures a {@link RestClient} bean.
     * This client is used for making HTTP requests in the application.
     *
     * @return a new instance of {@link RestClient}.
     */
    @Bean
    public RestClient configureRestClient() {
        return RestClient.create();
    }
}
