package com.errday.boardservice.client;

import com.errday.boardservice.dto.AddActivityScoreRequestDto;
import com.errday.boardservice.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${client.user-service.url}") String userServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    public Optional<UserResponseDto> fetchUser(Long userId) {

        try {
            UserResponseDto userResponseDto = restClient.get()
                    .uri("/internal/users/{userId}", userId)
                    .retrieve()
                    .body(UserResponseDto.class);
            return Optional.ofNullable(userResponseDto);
        } catch (RestClientException ex) {
            log.error("사용자 정보 조회 실패", ex);
            return Optional.empty();
        }

    }

    public List<UserResponseDto> fetchAllUsersByIds(List<Long> ids) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/internal/users")
                            .queryParam("ids", ids)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException ex) {
            log.error("사용자 정보 조회 실패", ex);
            return Collections.emptyList();
        }
    }

    public void addActivityScore(long userId, int score) {
        AddActivityScoreRequestDto request = new AddActivityScoreRequestDto(userId, score);

        restClient.post()
                .uri("/internal/users/activity-score/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

}
