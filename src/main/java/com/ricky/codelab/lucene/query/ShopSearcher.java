package com.ricky.codelab.lucene.query;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class ShopSearcher {

	private String indexPath;

	public ShopSearcher(String dirPath) {
		this.indexPath = new File(dirPath, "/index/").getPath();
		System.out.println("indexPath:"+indexPath);
	}

	public void query(String field, String keyword) throws IOException, ParseException {

		System.out.println("field:"+field+",keyword:"+keyword);
		
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();

		QueryParser parser = new QueryParser(field, analyzer);
		Query query = parser.parse(keyword);
		System.out.println("Searching for: " + query.toString(field));
		
//		Query query = new TermQuery(new Term(field, keyword));
		TopDocs results = searcher.search(query, 100);
//		searcher.search(query, filter, n);
		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");
		
		ScoreDoc[] hits = results.scoreDocs;
        for (ScoreDoc sd : hits) {
            Document d = searcher.doc(sd.doc);   
            System.out.println(d.get("third_id") + ":["+d.get(field)+"]");   
        }  
        
        reader.close();
	}
}
