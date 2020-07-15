package com.accurascience.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accurascience.service.GeneService;

/**
 * 疾病控制器
 * @author zhuchaojie
 *
 */
@RestController
public class GeneController {
    @Autowired
    private GeneService gs;
    
    /**
     * 通过基因OMIM id获取基因详细信息
     * @param omimId
     * @return
     */
    @GetMapping("/gene_information_by_omim_id")
    public Map<String, Object> geneOmimId(@RequestParam(value = "omim_id",required = true) String omimId){
    	
    	Map<String, Object> result = gs.getGeneByOmimId(omimId);
    	return result;
    	
    }
    /**
     * 通过基因entrez id获取基因详细信息
     * @param omimId
     * @return
     */
    @GetMapping("/gene_information_by_entrez_id")
    public Map<String, Object> geneEntrezId(@RequestParam(value = "entrez_id",required = true) String EntrezId){
    	
    	Map<String, Object> result = gs.getGeneByEntrezId(EntrezId);
    	return result;
    	
    }
   
    /**
     * 通过基因symbol获取基因详细信息
     * @param omimId
     * @return
     */
    @GetMapping("/gene_information_by_gene_symbol")
    public Map<String, Object> geneGeneSymbol(@RequestParam(value = "gene_symbol",required = true) String geneSymbol){
    	
    	Map<String, Object> result = gs.getGeneByGeneSymbol(geneSymbol);
    	return result;
    	
    }
	
}
