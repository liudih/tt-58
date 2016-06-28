package com.tomtop.services;

import com.tomtop.entity.HomeNewest;

public interface IHomeNewestService {

	HomeNewest getCustomersVoices(Integer client,Integer lang, Integer website);

}
