package com.accurascience.dao.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.accurascience.dao.DiseaseDao;
import com.accurascience.util.ClientUtil;
import com.accurascience.util.TxtReadUtil;

import grakn.client.GraknClient;
import grakn.client.answer.ConceptMap;
import graql.lang.Graql;
import graql.lang.query.GraqlGet;
import graql.lang.statement.Variable;
@Repository
public class DiseaseDaoImpl implements DiseaseDao {

	@Autowired
	private ClientUtil cu;
	private String keyspace = "phenotype";//数据容器
	@Override
	public Map<String, String> disease(String OMIMId) {
		// TODO Auto-generated method stub
        GraknClient client = cu.getClient();
        GraknClient.Session session = client.session(keyspace);
        //读取key
      	String[] diseaseAttrArray = TxtReadUtil.listKey("key/attr_list.txt");
    	
        // Read the person using a READ only transaction
        GraknClient.Transaction readTransaction = session.transaction().read();
        StringBuffer gql1 = new StringBuffer("match $disease isa disease, has OMIM_id '"+OMIMId+"'");
        StringBuffer gql2 = new StringBuffer("get");
        //String gql = "match $disease (traveler: $per) isa disease, has disease_id '"+diseaseId+"';get";
        //显示所有字段(除了id)
        for (int i = 1; i < diseaseAttrArray.length; i++) {
        	if(i != diseaseAttrArray.length-1) {
        		gql1.append(",has "+diseaseAttrArray[i]+" $" + diseaseAttrArray[i]);
        		gql2.append(" $" + diseaseAttrArray[i]+",");
        	}else {
        		gql1.append(",has "+diseaseAttrArray[i]+" $" + diseaseAttrArray[i]+";");
        		gql2.append(" $" + diseaseAttrArray[i]+";");
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
        //关闭
        readTransaction.close();
        session.close();
        //client.close();
        return result;
	}
	public static void main(String[] args) {
		//DiseaseDaoImpl ddl = new DiseaseDaoImpl();

	}
	@Override
	public List<Map<String,String>>  listDisease(String[] symptoms) {
		// TODO Auto-generated method stub
		GraknClient client = cu.getClient();
        GraknClient.Session session = client.session(keyspace);

        // Read the person using a READ only transaction
        GraknClient.Transaction readTransaction = session.transaction().read();
        //逐个症状查询，然后将结果合并
        List<Map<String,String>> result = new LinkedList<Map<String,String>>();
        for (int i = 0; i < symptoms.length; i++) {
        	String gql = "match $ds (has-disease: $dis, has-symptom: $sym) isa disease-symptom-has; "
            		+ "$dis has disease_name $name;$dis has OMIM_id $id; $sym has name "+symptoms[i]+"; get $name,$id;";
        	//查询
        	List<ConceptMap> answers = readTransaction.execute((GraqlGet) Graql.parse(gql));
        	//对结果进行抽取
      		for (ConceptMap conceptMap : answers) {
      			Map<String,String> singleTransition = new HashMap<String, String>(2);
      			for(Variable key : conceptMap.map().keySet()) {
					  
					  String value = conceptMap.map().get(key).asAttribute().value().toString();
					  singleTransition.put(key.name(), value);
      			}
      			//存入过渡list
      			result.add(singleTransition);
      			
      		}
		}
        //统计数量
        Map<Map<String, String>, Long> map = result.stream().
        	    collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
        //去掉数量小于symptoms长度的disid，即获得包含所有症状的疾病id
        for (Map<String, String> key: map.keySet()) {
			if(map.get(key) < symptoms.length) {
				result.remove(key);
			}
		}
        //去掉重复的元素
		List list = result.stream().distinct().collect(Collectors.toList());
        //关闭
        readTransaction.close();
        session.close();
        //client.close();
        return list;
	}
	

}
