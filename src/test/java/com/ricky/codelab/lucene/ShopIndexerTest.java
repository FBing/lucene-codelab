package com.ricky.codelab.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import com.ricky.codelab.lucene.index.ShopIndexer;
import com.ricky.codelab.lucene.model.Shop;

public class ShopIndexerTest {

	private String dirPath = "D:/Lucene_Test";
	
	@Test
	public void testIndex(){
		
		ShopIndexer indexer = new ShopIndexer(dirPath);
		
		List<Shop> shop_list = createData();
		
		try {
			indexer.index(shop_list, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Shop> createData(){
		
		List<Shop> shop_list = new ArrayList<>();
		
		for(int i=0;i<1000;i++){
			Shop shop = new Shop();
			shop.setId(i);
			shop.setThirdId("999_"+i);
			shop.setName("Monkey_"+i);
			shop.setAddress("北京市朝阳区静安中心"+i+"室");
			shop.setPhone(Arrays.asList("010-5762"+i));
			shop.setCityId(2);
			
			shop_list.add(shop);
		}
		
		return shop_list;
	}
}
