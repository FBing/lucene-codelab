package com.bytebeats.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import com.bytebeats.lucene.demo.ShopSearch;

public class ShopQueryTest {

	private String dirPath = "D:/Lucene_Test/shop/";
	private Analyzer analyzer = new StandardAnalyzer();
	
	@Test
	public void testQuery(){
		
		ShopSearch searcher = new ShopSearch(dirPath, analyzer);
		try {
			searcher.query("火锅");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
}
