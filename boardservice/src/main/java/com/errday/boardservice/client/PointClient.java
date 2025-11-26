package com.errday.boardservice.client;

import com.errday.boardservice.dto.DeductPointRequestDto;
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

    public void deductPoints(long userId, int amount) {
        DeductPointRequestDto request = new DeductPointRequestDto(userId, amount);

        restClient.post()
                .uri("/points/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }


}
