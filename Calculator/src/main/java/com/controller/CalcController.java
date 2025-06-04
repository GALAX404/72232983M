package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.CalcService;

@RestController
@RequestMapping("/numbers")
public class CalcController {

	@Autowired
    private CalcService calcService;
	
	@GetMapping("/{numberId}")
	public ResponseEntity<?> getNumbers(@PathVariable String numberId){
		if(!List.of("p","f","e","r").contains(numberId)) {
			return ResponseEntity.badRequest().body("Invalid ID");
		}
		Map<String, Object> result = calcService.getNumbersAndCalculateAverage(numberId);
        return ResponseEntity.ok(result);
	}
}
