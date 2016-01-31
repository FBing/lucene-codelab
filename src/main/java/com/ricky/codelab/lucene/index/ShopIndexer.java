package com.ricky.codelab.lucene.index;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.ricky.codelab.lucene.model.Shop;

public class ShopIndexer {
	private String indexPath;
	private String docsPath;
	
	public ShopIndexer(String dirPath){
		this.indexPath = new File(dirPath, "/index/").getPath();
		this.docsPath = new File(dirPath, "/docs/").getPath();
	}
	
	public void index(List<Shop> shop_list, boolean create) throws IOException{
		
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

		if (create) {
			iwc.setOpenMode(OpenMode.CREATE);
		} else {
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		
		Path docDir = Paths.get(docsPath);
		if (!Files.isReadable(docDir)) {
			System.out.println("Document directory '" + docDir.toAbsolutePath()
					+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		
		IndexWriter writer = new IndexWriter(dir, iwc);
		try {
			for (Shop shop : shop_list) {
				indexDoc(writer, docDir, shop);
			}
			// writer.forceMerge(1);
		}finally{
			writer.close();
		}
	}
	
	private void indexDoc(IndexWriter writer, Path file, Shop shop) throws IOException {
		
		Document doc = new Document();
		
		doc.add(new LongField("id", shop.getId(), Field.Store.NO));
		doc.add(new StringField("third_id", shop.getThirdId(), Field.Store.YES));
		doc.add(new StringField("name", shop.getName(), Field.Store.YES));
		doc.add(new StringField("address", shop.getAddress(), Field.Store.YES));
		doc.add(new StringField("phone", StringUtils.join(shop.getPhone(), ","), Field.Store.YES));
		doc.add(new IntField("city_id", shop.getCityId(), Field.Store.YES));
		doc.add(new DoubleField("lat", shop.getLat(), Field.Store.YES));
		doc.add(new DoubleField("lng", shop.getLng(), Field.Store.YES));

		if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
			writer.addDocument(doc);
		} else {
			writer.updateDocument(new Term("third_id", file.toString()), doc);
		}
	}
}
