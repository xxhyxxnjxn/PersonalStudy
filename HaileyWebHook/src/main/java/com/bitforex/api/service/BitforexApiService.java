package com.bitforex.api.service;


public interface BitforexApiService {
	
	public String getSymbol();
	public String getTicker(String market);
	public String getDepth(String market);
	public void getBalance(String currency);
	public void tradeOrder(String currency);
	
}
