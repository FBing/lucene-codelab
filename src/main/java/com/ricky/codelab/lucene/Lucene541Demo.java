package com.ricky.codelab.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Lucene541Demo {

	private String indexPath = "D:/Lucene_Test/day01/";
	private Analyzer analyzer = new StandardAnalyzer();

	public void search() throws IOException, ParseException {

		String field = "title";
		String querystr = "Lucene";

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(indexPath)));
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser(field, analyzer);
		Query query = parser.parse(querystr);
		
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

	public void index() throws IOException {

		File indexDir = new File(indexPath);
		if (!indexDir.exists()) {
			indexDir.mkdirs();
		}

		System.out.println("index path:"+indexPath);
		
		Directory dir = FSDirectory.open(Paths.get(indexPath));

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(dir, iwc);

		addDoc(writer, "Lucene in Action", "193398817");
		addDoc(writer, "Lucene for Dummies", "55320055Z");
		addDoc(writer, "Managing Gigabytes", "55063554A");
		addDoc(writer, "The Art of Computer Science", "9900333X");

		writer.close();
	}

	private void addDoc(IndexWriter writer, String title, String isbn)
			throws IOException {

		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));

		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("isbn", isbn, Field.Store.YES));

		writer.addDocument(doc);
	}
	
	public static void main(String[] args) {
		
		try {
//			new Lucene541Demo().index();
			new Lucene541Demo().search();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
