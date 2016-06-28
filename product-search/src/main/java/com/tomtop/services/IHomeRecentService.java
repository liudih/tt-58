package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.HomeRecentOrders;

public interface IHomeRecentService {

	
	public List<HomeRecentOrders> getHomeRecentOrders(Integer lang,Integer client,Integer website,String currency); 
	public List<HomeRecentOrders> getHomeRecentOrders(Integer lang,Integer client, Integer website, String currency,String depotName);
}
