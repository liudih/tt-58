package com.tomtop.service;

public interface ICurrencyService {

	public Double exchange(String originalCCY, String targetCCY);
	
	public Double exchange(Double money, String originalCCY, String targetCCY);
}
