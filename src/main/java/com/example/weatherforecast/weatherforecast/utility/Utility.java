package com.example.weatherforecast.weatherforecast.utility;


import com.example.weatherforecast.weatherforecast.dto.WeatherForecastResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class Utility {

    @Value("${url}")
    private String url;

    @Value("${appid}")
    private String appId;

    @Autowired
    private RestTemplate restTemplate;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime convertStringLocalDate(String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }


    @CircuitBreaker(name = "myProjectAllRemoteCallsCB", fallbackMethod = "getAPIFallBack")
    public HttpEntity<WeatherForecastResponseDTO> fetchFromAPI(String city) {

        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<WeatherForecastResponseDTO> responseEntity = null;

        HttpEntity<String> entity = new HttpEntity<String>(headers);


        // try {

        String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/data/2.5/forecast")
                .queryParam("q", city)
                .queryParam("appid", appId)
                .queryParam("cnt", 24)
                .encode()
                .toUriString();


        responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WeatherForecastResponseDTO.class);

//        }  catch (HttpClientErrorException | HttpServerErrorException e ) {
//            throw new CityNotFoundException(city);
//        }
        return responseEntity;
    }


    public String  getAPIFallBack(String topicPage, Exception e){
        System.out.println("getAPIFallBack::{}" + e.getMessage() +topicPage);

            return topicPage;
    }
}
