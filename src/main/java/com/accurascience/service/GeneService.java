package com.accurascience.service;

import java.util.Map;

/**
 * 基因业务逻辑层
 * @author zhuchaojie
 *
 */
public interface GeneService {

	  /**
     * 通过基因OMIM id获取基因详细信息
     * @param geneId
     * @return
     */
	Map<String, Object> getGeneByOmimId(String omimId);
	   /**
     * 通过基因entrez id获取基因详细信息
     * @param geneId
     * @return
     */
	Map<String, Object> getGeneByEntrezId(String EntrezId);
	   /**
     * 通过基因symbol获取基因详细信息
     * @param geneId
     * @return
     */
	Map<String, Object> getGeneByGeneSymbol(String geneSymbol);
}
