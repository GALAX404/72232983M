package com.model;

import java.time.ZonedDateTime;

public class StockPrice {
    private double price;
    private ZonedDateTime lastUpdatedAt;
	public StockPrice(double price, ZonedDateTime lastUpdatedAt) {
		super();
		this.price = price;
		this.lastUpdatedAt = lastUpdatedAt;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public ZonedDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}
	public void setLastUpdatedAt(ZonedDateTime lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}


    
}
