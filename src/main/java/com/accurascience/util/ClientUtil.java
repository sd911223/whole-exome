package com.accurascience.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import grakn.client.GraknClient;

/**
 * 创建连接grakn的客户端
 * @author zhuchaojie
 *
 */

@Component
public class ClientUtil {
	 @Value("${grakn.server.address}")
	 private String address; 
     private static GraknClient client;
     
     //返回客户端
     public GraknClient getClient() {
    	if(client == null) synchronized (this)  {
    		
    		client = new GraknClient(address);
    	}
    	return client;

     }
     
     
}
