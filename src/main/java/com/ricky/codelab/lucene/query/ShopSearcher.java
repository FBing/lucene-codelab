package com.ricky.codelab.lucene.query;

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class ShopSearcher {

	private String indexPath;
	private Analyzer analyzer;
	
	public ShopSearcher(String indexPath, Analyzer analyzer){
		this.indexPath = indexPath;
		this.analyzer = analyzer;
	}

	public void query(String keyword) throws IOException, ParseException {

		String field = "name";

		System.out.println("field:"+field+",keyword:"+keyword);
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(indexPath)));
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser(field, analyzer);
		Query query = parser.parse(keyword);
		
		System.out.println("Searching for: " + query.toString(field));
		
		TopDocs results = searcher.search(query, 100);
		ScoreDoc[] hits = results.scoreDocs;
		System.out.println("Found " + hits.length + " hits.");

		for (int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc);
			String title = doc.get(field);
			System.out.println("title:" + title+",score:"+hits[i].score);
		}

		reader.close();
	}
}
