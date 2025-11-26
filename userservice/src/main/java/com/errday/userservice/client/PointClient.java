package com.errday.userservice.client;

import com.errday.userservice.dto.AddPointRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PointClient {

    private final RestClient restClient;

    public PointClient(@Value("${client.point-service.url}") String url) {
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    public void addPoints(long userId, int amount) {
        AddPointRequestDto addPointRequestDto = new AddPointRequestDto(userId, amount);

        restClient.post()
                .uri("/points/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(addPointRequestDto)
                .retrieve()
                .toBodilessEntity();
    }

    public void deductPoints(long userId, int amount) {
        AddPointRequestDto addPointRequestDto = new AddPointRequestDto(userId, amount);

        restClient.post()
                .uri("/points/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .body(addPointRequestDto)
                .retrieve()
                .toBodilessEntity();
    }
}
