package com.crashcourse.kickoff.tms.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import com.crashcourse.kickoff.tms.security.JwtTokenProvider;
import com.crashcourse.kickoff.tms.club.ClubProfile;

@Component
public class ClubServiceClient {

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    /*
     * should this go into .env?
     */
    private final String clubUrl = "http://localhost:8082/api/v1/clubs/";

    public ClubServiceClient(RestTemplate restTemplate, JwtTokenProvider jwtTokenProvider) {
        this.restTemplate = restTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ClubProfile getClubProfileById (Long clubId, String token) {
        String url = clubUrl + clubId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtTokenProvider.getToken(token));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ClubProfile> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    ClubProfile.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to retrieve ClubProfile for ID: " + clubId);
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error retrieving ClubProfile for ID: " + clubId + ". Error: " + e.getMessage());
        }
    }
}
