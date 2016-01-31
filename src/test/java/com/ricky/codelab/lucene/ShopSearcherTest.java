package com.ricky.codelab.lucene;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;
import com.ricky.codelab.lucene.query.ShopSearcher;

public class ShopSearcherTest {

	private String dirPath = "D:/Lucene_Test";
	
	@Test
	public void testQuery(){
		
		ShopSearcher searcher = new ShopSearcher(dirPath);
		try {
			searcher.query("name", "m");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
}
