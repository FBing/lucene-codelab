package com.bytebeats.lucene.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import com.bytebeats.lucene.model.Shop;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class ShopIndexer {
	private String indexPath;
	private Analyzer analyzer;
	
	public ShopIndexer(String indexPath, Analyzer analyzer){
		
		File indexDir = new File(indexPath);
		if(!indexDir.exists()){
			indexDir.mkdirs();
		}
		this.indexPath = indexPath;
		this.analyzer = analyzer;
	}
	
	public void index(List<Shop> shop_list) throws IOException{
		
		System.out.println("Indexing path:"+indexPath);
		
		Directory dir = FSDirectory.open(Paths.get(indexPath));

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(dir, iwc);

		for (Shop shop : shop_list) {
			
			addDoc(writer, shop);
		}

		writer.close();
	}
	
	private void addDoc(IndexWriter writer, Shop shop) throws IOException {
		
		Document doc = new Document();
		
		doc.add(new LongField("id", shop.getId(), Field.Store.YES));
		doc.add(new StringField("third_id", shop.getThirdId(), Field.Store.YES));
		doc.add(new TextField("name", shop.getName(), Field.Store.YES));
		doc.add(new StringField("address", shop.getAddress(), Field.Store.YES));
		
		doc.add(new StringField("phone", StringUtils.join(shop.getPhone(), ","), Field.Store.YES));
		doc.add(new StringField("city_id", String.valueOf(shop.getCityId()), Field.Store.YES));
		doc.add(new DoubleField("lat", shop.getLat(), Field.Store.YES));
		doc.add(new DoubleField("lng", shop.getLng(), Field.Store.YES));
		
		writer.addDocument(doc);
	}
}
