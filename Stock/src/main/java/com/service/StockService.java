package com.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.model.StockPrice;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockService {

	    private final RestTemplate restTemplate = new RestTemplate();
	    private final String BASE_URL = "http://20.244.56.144/evaluation-service/stocks";
	    private final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiZXhwIjoxNzQ5MDE4NzMxLCJpYXQiOjE3NDkwMTg0MzEsImlzcyI6IkFmZm9yZG1lZCIsImp0aSI6ImE3ZDY0YmZkLTQzZDUtNDE2Zi1iNTQ4LTJmZTk0OTNiNDcxOSIsInN1YiI6InBhbnNlbW9oaXQyMDA1QGdtYWlsLmNvbSJ9LCJlbWFpbCI6InBhbnNlbW9oaXQyMDA1QGdtYWlsLmNvbSIsIm5hbWUiOiJtb2hpdCBwYW5zZSIsInJvbGxObyI6IjcyMjMyOTgzbSIsImFjY2Vzc0NvZGUiOiJLUmpVVVUiLCJjbGllbnRJRCI6ImE3ZDY0YmZkLTQzZDUtNDE2Zi1iNTQ4LTJmZTk0OTNiNDcxOSIsImNsaWVudFNlY3JldCI6IkNrWXFkSFJxUHZjSFduQ1oifQ.YnytWwOZusyFEB7U_PThO61jxn5R0ui0khOIyLPdH_E";  // your full token

	    public List<StockPrice> fetchStockPrices(String ticker, int minutes) {
	        String url = BASE_URL + "/" + ticker + "?minutes=" + minutes;

	        // Create HTTP Headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(BEARER_TOKEN);

	        // Create HttpEntity with headers
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        try {
	            // Make the GET request with headers
	            ResponseEntity<StockPrice[]> response = restTemplate.exchange(
	                    url,
	                    HttpMethod.GET,
	                    entity,
	                    StockPrice[].class
	            );

	            return Arrays.asList(Objects.requireNonNull(response.getBody()));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Collections.emptyList();
	        }
	    }
	

    public double calculateAveragePrice(List<StockPrice> prices) {
        return prices.stream()
                .mapToDouble(StockPrice::getPrice)
                .average()
                .orElse(0.0);
    }

    public double calculateCorrelation(List<Double> xPrices, List<Double> yPrices) {
        if (xPrices.size() != yPrices.size() || xPrices.isEmpty()) return 0.0;

        int n = xPrices.size();
        double sumX = xPrices.stream().mapToDouble(Double::doubleValue).sum();
        double sumY = yPrices.stream().mapToDouble(Double::doubleValue).sum();
        double sumXY = 0, sumX2 = 0, sumY2 = 0;

        for (int i = 0; i < n; i++) {
            double x = xPrices.get(i);
            double y = yPrices.get(i);
            sumXY += x * y;
            sumX2 += x * x;
            sumY2 += y * y;
        }

        double numerator = (n * sumXY) - (sumX * sumY);
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        return denominator == 0 ? 0.0 : numerator / denominator;
    }


}