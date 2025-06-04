package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.model.StockPrice;
import com.service.StockService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Map<String, Object> getAveragePrice(@PathVariable String ticker,
                                               @RequestParam int minutes,
                                               @RequestParam String aggregation) {
        List<StockPrice> prices = stockService.fetchStockPrices(ticker, minutes);
        double average = stockService.calculateAveragePrice(prices);

        Map<String, Object> response = new HashMap<>();
        response.put("averageStockPrice", average);
        response.put("priceHistory", prices);
        return response;
    }

    @GetMapping("/stockcorrelation")
    public Map<String, Object> getStockCorrelation(
            @RequestParam int minutes,
            @RequestParam List<String> ticker) {

        if (ticker.size() != 2) {
            return Map.of("error", "Exactly 2 tickers required for correlation.");
        }

        Map<String, Object> stocksData = new HashMap<>();

        List<StockPrice> prices1 = stockService.fetchStockPrices(ticker.get(0), minutes);
        List<StockPrice> prices2 = stockService.fetchStockPrices(ticker.get(1), minutes);

        List<Double> xPrices = prices1.stream().map(StockPrice::getPrice).collect(Collectors.toList());
        List<Double> yPrices = prices2.stream().map(StockPrice::getPrice).collect(Collectors.toList());

        double correlation = stockService.calculateCorrelation(xPrices, yPrices);

        stocksData.put(ticker.get(0), Map.of(
                "averagePrice", stockService.calculateAveragePrice(prices1),
                "priceHistory", prices1
        ));

        stocksData.put(ticker.get(1), Map.of(
                "averagePrice", stockService.calculateAveragePrice(prices2),
                "priceHistory", prices2
        ));

        return Map.of(
                "correlation", correlation,
                "stocks", stocksData
        );
    }

}
