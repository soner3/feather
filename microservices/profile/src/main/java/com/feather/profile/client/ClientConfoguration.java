package com.feather.profile.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfoguration {

    @Bean
    protected UserClientService userClientService() {
        RestClient restClient = RestClient
                .builder()
                .baseUrl("http://localhost:9090")
                .build();
        RestClientAdapter clientAdapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory
                .builderFor(clientAdapter)
                .build()
                .createClient(UserClientService.class);
    }

}
