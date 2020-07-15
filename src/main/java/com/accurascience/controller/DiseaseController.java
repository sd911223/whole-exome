package com.accurascience.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accurascience.service.DiseaseService;

/**
 * 疾病控制器
 * @author zhuchaojie
 *
 */
@RestController
public class DiseaseController {
    @Autowired
    private DiseaseService ds;
    /**
     * 根据疾病id查询疾病的详细信息
     * @param OMIMId
     * @return
     */
	@GetMapping("/diseaseInformation")
	public Map<String, Object> diseaseInformation(@RequestParam(value = "OMIM_id",required = true) String OMIMId){
		
		Map<String, Object> result = ds.disease(OMIMId);
		return result;

	}
	/**
	 * 根据多个症状查询对应的疾病列表
	 * @param symptoms
	 * @return
	 */
	@PostMapping("/symptom2disease")
	public List<Map<String,String>>  symptom2disease(@RequestParam(value = "symptoms",required = true) String symptoms){
		//此时前台传来的是一个数组的字符串,去掉中括号，生成数组
		String[] symptomArray = symptoms.substring(1, symptoms.length()-1).split(",");
		List<Map<String, String>> result = ds.listDisease(symptomArray);
		return result;
	}
	
	
}
