package com.accurascience.dao;

import java.util.List;
import java.util.Map;

/**
 * 疾病持久层
 * @author zhuchaojie
 *
 */
public interface DiseaseDao {
    /**
     * 返回一个疾病的详细信息
     * @param OMIMId
     * @return
     */
	Map<String, String> disease(String OMIMId);
	/**
	 * 通过一组症状名称，查询疾病id
	 * @param symptoms
	 * @return
	 */
	List<Map<String,String>>  listDisease(String[] symptoms);
}
