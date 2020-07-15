package com.accurascience.dao.impl;

import java.util.List;

import grakn.client.GraknClient;
import grakn.client.answer.ConceptMap;
import graql.lang.Graql;
import graql.lang.query.GraqlInsert;

public class TestDaoImpl {

	public static void main(String[] args) {
		GraknClient gc = new GraknClient("localhost:48555");
		GraknClient.Session session = gc.session("disease");
        // Read the person using a READ only transaction
        GraknClient.Transaction readTransaction = session.transaction().read();
        StringBuffer gql1 = new StringBuffer("insert $disease isa disease, has disease_id '"+729+"';");
       
        List<ConceptMap> answers = readTransaction.execute((GraqlInsert) Graql.parse(gql1.toString()));
        readTransaction.commit();

	}
}
