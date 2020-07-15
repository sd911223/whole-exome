package com.accurascience.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.accurascience.dao.GeneDao;
import com.accurascience.util.ClientUtil;
import com.accurascience.util.TxtReadUtil;

import grakn.client.GraknClient;
import grakn.client.answer.ConceptMap;
import graql.lang.Graql;
import graql.lang.query.GraqlGet;
import graql.lang.statement.Variable;
@Repository
public class GeneDaoImpl implements GeneDao {
	@Autowired
	private ClientUtil cu;
	//数据容器
	private String keyspace = "gene_v1";
	@Override
	public Map<String, String> getGeneByOmimId(String omimId) {
		// TODO Auto-generated method stub
		//读取key
		String[] keyArray = TxtReadUtil.listKey("key/all_key.txt");
		// TODO Auto-generated method stub
        GraknClient client = cu.getClient();
        GraknClient.Session session = client.session(keyspace);
        
        // Read the person using a READ only transaction
        GraknClient.Transaction readTransaction = session.transaction().read();
        StringBuffer gql1 = new StringBuffer("match $gene isa gene, has OMIM_id '"+omimId+"'");
        StringBuffer gql2 = new StringBuffer("get");
        //String gql = "match $disease (traveler: $per) isa disease, has disease_id '"+diseaseId+"';get";
        //显示所有字段(除了id)
        for (int i = 1; i < keyArray.length; i++) {
        	if(i != keyArray.length-1) {
        		gql1.append(",has "+keyArray[i]+" $" + keyArray[i]);
        		gql2.append(" $" + keyArray[i]+",");
        	}else {
        		gql1.append(",has "+keyArray[i]+" $" + keyArray[i]+";");
        		gql2.append(" $" + keyArray[i]+";");
        	}
			
		}
        List<ConceptMap> answers = readTransaction.execute((GraqlGet) Graql.parse(gql1.toString()+gql2.toString()));
        Map<String, String> result = new HashMap<String, String>(16);
        //对answers进行包装
  		for (ConceptMap conceptMap : answers) {
  			for(Variable key : conceptMap.map().keySet()) {
  				result.put(key.name(), conceptMap.map().get(key).asAttribute().value().toString());
  			}
  		}
  		//将返回结果中相对与 keyArray 缺少的 key 赋给 result，值是空字符串
		for (int i = 0;i<keyArray.length;i++){
			if(!result.containsKey(keyArray[i])){
				result.put(keyArray[i], "");
			}
		}
        //关闭
        readTransaction.close();
        session.close();
        //client.close();
        return result;
	}
    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
    	
    	GeneDaoImpl gdi = new GeneDaoImpl();
    	Map<String, String> map = gdi.getGeneByOmimId("100690");
    	for (String key : map.keySet()) {
			System.out.println(key+"-------------"+map.get(key));
		}
	}
	@Override
	public Map<String, String> getGeneByEntrezId(String EntrezId) {
		// TODO Auto-generated method stub
		//读取key
				String[] keyArray = TxtReadUtil.listKey("key/all_key.txt");
				// TODO Auto-generated method stub
		        GraknClient client = cu.getClient();
		        GraknClient.Session session = client.session(keyspace);
		        
		        // Read the person using a READ only transaction
		        GraknClient.Transaction readTransaction = session.transaction().read();
		        StringBuffer gql1 = new StringBuffer("match $gene isa gene, has entrez_id '"+EntrezId+"'");
		        StringBuffer gql2 = new StringBuffer("get");
		        //String gql = "match $disease (traveler: $per) isa disease, has disease_id '"+diseaseId+"';get";
		        //显示所有字段(除了entrez_id)
		        for (int i = 0; i < keyArray.length; i++) {
		        	if(!keyArray[i].equals("entrez_id")) {
		        		if(i != keyArray.length-1) {
			        		gql1.append(",has "+keyArray[i]+" $" + keyArray[i]);
			        		gql2.append(" $" + keyArray[i]+",");
			        	}else {
			        		gql1.append(",has "+keyArray[i]+" $" + keyArray[i]+";");
			        		gql2.append(" $" + keyArray[i]+";");
			        	}

		        	}
					
				}
		        List<ConceptMap> answers = readTransaction.execute((GraqlGet) Graql.parse(gql1.toString()+gql2.toString()));
		        Map<String, String> result = new HashMap<String, String>(29);
		        //对answers进行包装
		  		for (ConceptMap conceptMap : answers) {
		  			for(Variable key : conceptMap.map().keySet()) {
		  				result.put(key.name(), conceptMap.map().get(key).asAttribute().value().toString());
		  			}
		  		}
				//将返回结果中相对与 keyArray 缺少的 key 赋给 result，值是空字符串
				for (int i = 0;i<keyArray.length;i++){
					if(!result.containsKey(keyArray[i])){
						result.put(keyArray[i], "");
					}
				}
		        //关闭
		        readTransaction.close();
		        session.close();
		        //client.close();
		        return result;
	}
	@Override
	public Map<String, String> getGeneByGeneSymbol(String geneSymbol) {
		// TODO Auto-generated method stub
		//读取key
				String[] keyArray = TxtReadUtil.listKey("key/all_key.txt");
				// TODO Auto-generated method stub
		        GraknClient client = cu.getClient();
		        GraknClient.Session session = client.session(keyspace);
		        
		        // Read the person using a READ only transaction
		        GraknClient.Transaction readTransaction = session.transaction().read();
		        StringBuffer gql1 = new StringBuffer("match $gene isa gene, has gene_symbol '"+geneSymbol+"'");
		        StringBuffer gql2 = new StringBuffer("get");
		        //String gql = "match $disease (traveler: $per) isa disease, has disease_id '"+diseaseId+"';get";
		        //显示所有字段(除了gene_symbol)
		        for (int i = 0; i < keyArray.length; i++) {
		        	if(!keyArray[i].equals("gene_symbol")) {
		        		if(i != keyArray.length-1) {
			        		gql1.append(",has "+keyArray[i]+" $" + keyArray[i]);
			        		gql2.append(" $" + keyArray[i]+",");
			        	}else {
			        		gql1.append(",has "+keyArray[i]+" $" + keyArray[i]+";");
			        		gql2.append(" $" + keyArray[i]+";");
			        	}

		        	}
					
				}
		        List<ConceptMap> answers = readTransaction.execute((GraqlGet) Graql.parse(gql1.toString()+gql2.toString()));
		        Map<String, String> result = new HashMap<String, String>(29);
		        //对answers进行包装
		  		for (ConceptMap conceptMap : answers) {
		  			for(Variable key : conceptMap.map().keySet()) {
		  				result.put(key.name(), conceptMap.map().get(key).asAttribute().value().toString());
		  			}
		  		}
				//将返回结果中相对与 keyArray 缺少的 key 赋给 result，值是空字符串
				for (int i = 0;i<keyArray.length;i++){
					if(!result.containsKey(keyArray[i])){
						result.put(keyArray[i], "");
					}
				}
		        //关闭
		        readTransaction.close();
		        session.close();
		        //client.close();
		        return result;
	}

}
