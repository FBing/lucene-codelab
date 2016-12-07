package com.bytebeats.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bytebeats.lucene.model.Shop;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Test;

import com.bytebeats.lucene.demo.ShopIndexer;

public class ShopIndexerTest {

	private String dirPath = "D:/Lucene_Test/shop/";
	private Analyzer analyzer = new StandardAnalyzer();
	
	@Test
	public void testIndex(){
		
		ShopIndexer indexer = new ShopIndexer(dirPath, analyzer);
		
		List<Shop> shop_list = createData();
		
		try {
			indexer.index(shop_list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Shop> createData(){
		
		List<Shop> shop_list = new ArrayList<>();
		
		for(int i=0;i<2000;i++){
			Shop shop = new Shop();
			shop.setId(i);
			shop.setThirdId("999"+i);
			shop.setName((i%2==0 ? "肯德基(国展店)":"海底捞火锅(太阳宫店)")+" "+i);
			shop.setAddress("北京市朝阳区静安中心"+i+"室");
			shop.setPhone(Arrays.asList("010-5762"+i));
			shop.setCityId(i%2==0 ?1:2);
			
			shop_list.add(shop);
		}
		
		return shop_list;
	}
}
