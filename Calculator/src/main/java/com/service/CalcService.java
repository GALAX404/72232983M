package com.service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.response.CalcResponse;

@Service
public class CalcService {
	private static final int window_size = 10;
	private final RestTemplate restTemplate;
	private final Map<String,Deque<Integer>> windowMap = new ConcurrentHashMap<>();
	
	@Autowired
	public CalcService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public Map<String,Object> getNumbersAndCalculateAverage(String type) {
        String url = getApiUrl(type);
        List<Integer> fetchedNumbers;

        try {
//            ResponseEntity<CalcResponse> response = restTemplate.getForEntity(url, CalcResponse.class);
//            fetchedNumbers = response.getBody().getNumbers();
        	HttpHeaders headers = new HttpHeaders();
        	headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiZXhwIjoxNzQ5MDE4NzMxLCJpYXQiOjE3NDkwMTg0MzEsImlzcyI6IkFmZm9yZG1lZCIsImp0aSI6ImE3ZDY0YmZkLTQzZDUtNDE2Zi1iNTQ4LTJmZTk0OTNiNDcxOSIsInN1YiI6InBhbnNlbW9oaXQyMDA1QGdtYWlsLmNvbSJ9LCJlbWFpbCI6InBhbnNlbW9oaXQyMDA1QGdtYWlsLmNvbSIsIm5hbWUiOiJtb2hpdCBwYW5zZSIsInJvbGxObyI6IjcyMjMyOTgzbSIsImFjY2Vzc0NvZGUiOiJLUmpVVVUiLCJjbGllbnRJRCI6ImE3ZDY0YmZkLTQzZDUtNDE2Zi1iNTQ4LTJmZTk0OTNiNDcxOSIsImNsaWVudFNlY3JldCI6IkNrWXFkSFJxUHZjSFduQ1oifQ.YnytWwOZusyFEB7U_PThO61jxn5R0ui0khOIyLPdH_E");
        	HttpEntity<String> entity = new HttpEntity<>(headers);

        	ResponseEntity<CalcResponse> response = restTemplate.exchange(
        	    url,
        	    HttpMethod.GET,
        	    entity,
        	    CalcResponse.class
        	);

        	fetchedNumbers = response.getBody().getNumbers();

        	
        } catch (Exception e) {
            fetchedNumbers = new ArrayList<>();
        }

        Deque<Integer> window = windowMap.getOrDefault(type, new LinkedList<>());

        List<Integer> prevState = new ArrayList<>(window);

        for (Integer num : fetchedNumbers) {
            if (!window.contains(num)) {
                if (window.size() >= window_size) {
                    window.pollFirst();
                }
                window.addLast(num);
            }
        }

        double avg = window.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);

        windowMap.put(type, window);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("windowPrevState", prevState);
        result.put("windowCurrState", window);
        result.put("numbers", fetchedNumbers);
        result.put("avg", Math.round(avg * 100.0) / 100.0);

        return result;
    }

	private String getApiUrl(String type) {
        switch (type) {
            case "p": return "http://20.244.56.144/evaluation-service/primes";
            case "f": return "http://20.244.56.144/evaluation-service/fibo";
            case "e": return "http://20.244.56.144/evaluation-service/even";
            case "r": return "http://20.244.56.144/evaluation-service/rand";
            default: throw new IllegalArgumentException("Invalid type");
        }
    }
}

