package com.accurascience.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accurascience.dao.DiseaseDao;
import com.accurascience.service.DiseaseService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
@Service
public class DiseaseServiceImpl implements DiseaseService {
	
	@Autowired
    private DiseaseDao dd;
	@Override
	public Map<String, Object> disease(String OMIMId) {
		// TODO Auto-generated method stub
		Map<String, String> result = dd.disease(OMIMId);
		//把结果符合json格式
		Map<String, Object> cleanResult = this.getJsonResult(result);
		return cleanResult;
	}
	@Override
	public List<Map<String,String>>  listDisease(String[] symptoms) {
		// TODO Auto-generated method stub
		List<Map<String,String>>  result = dd.listDisease(symptoms);
		return result;
	}
	/**
	 * 
	 * json格式化方法
	 */
	private Map<String, Object> getJsonResult(Map<String, String> result ){
		Map<String, Object> cleanResult = new HashMap<String, Object>(result.size());
		
		for (String key : result.keySet()) {
            
			String value = result.get(key).replaceAll("\\\\'", "\\'")/*单引号处理*/;
			try {
				if(value.startsWith("[") && value.endsWith("]") ) {//数组
					cleanResult.put(key, JSON.parseArray(value));
					continue;
				}
				if(value.startsWith("{") && value.endsWith("}") ) {//对象
					cleanResult.put(key, JSONObject.parse(value));
					
					continue;
					
				}
			} catch (JSONException e) {//异常，存入字符串
				// TODO Auto-generated catch block
				e.printStackTrace();
				cleanResult.put(key, value);
			}
			//一般字符串
			cleanResult.put(key, value);
			
		}
		return cleanResult;
		
	}

}
