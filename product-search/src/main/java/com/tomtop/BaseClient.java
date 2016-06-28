package com.tomtop;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * 获取查询、创建索引的客户端
 * @author ztiny
 * @Date 2015-12-19
 */
@Service
public class BaseClient {

	@Autowired
	@Qualifier("indexClient")
	private TransportClient indexClient;

	@Autowired
	@Qualifier("transportClient")
	private TransportClient transportClient;

	/**
	 * 获取elastic search 客户端,后续参数会从配置文件读取,单机可用
	 * 
	 * @param ip
	 * @return
	 */
	public Client getClient(String ip) {
		Client client = null;
		try {
			client = TransportClient
					.builder()
					.build()
					.addTransportAddress(
							new InetSocketTransportAddress(InetAddress
									.getByName(ip), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return client;
	}

	public Client getSearchClient() {
		return transportClient;
	}

}
