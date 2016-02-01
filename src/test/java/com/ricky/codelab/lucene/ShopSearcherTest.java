package com.ricky.codelab.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import com.ricky.codelab.lucene.query.ShopSearcher;

public class ShopSearcherTest {

	private String dirPath = "D:/Lucene_Test/shop/";
	private Analyzer analyzer = new StandardAnalyzer();
	
	@Test
	public void testQuery(){
		
		ShopSearcher searcher = new ShopSearcher(dirPath, analyzer);
		try {
			searcher.query("KFC");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
}
