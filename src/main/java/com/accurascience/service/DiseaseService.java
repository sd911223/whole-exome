package com.accurascience.service;

import java.util.List;
import java.util.Map;

/**
 * 疾病业务逻辑层
 * @author zhuchaojie
 *
 */
public interface DiseaseService {
	 /**
     * 返回一个疾病的详细信息
     * @param OMIMId
     * @return
     */
	Map<String, Object> disease(String OMIMId);
	/**
	 * 通过一组症状名称，查询疾病列表
	 * @param symptomArray
	 * @return
	 */
	List<Map<String,String>>  listDisease(String[] symptomArray);
}
