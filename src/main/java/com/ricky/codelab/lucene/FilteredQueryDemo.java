package com.ricky.codelab.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ricky.codelab.lucene.model.Article;

public class FilteredQueryDemo {

	public static void main(String[] args) {

		// Lucene Document的域名
		String fieldName = "title";

		// 实例化IKAnalyzer分词器
		Analyzer analyzer = new IKAnalyzer(true);

		Directory directory = null;
		IndexWriter iwriter = null;
		IndexReader ireader = null;
		IndexSearcher isearcher = null;
		try {
			// 建立内存索引对象
			directory = new RAMDirectory();

			// 配置IndexWriterConfig
			IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
			iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(directory, iwConfig);
			// 写入索引
			addDoc(iwriter, new Article("Spark Tungsten-sort Based Shuffle 分析", "2016-01-02 12:27", "朱威廉"));
			addDoc(iwriter, new Article("Spark UI (基于Yarn) 分析与定制", "2016-01-01 12:27", "祝威廉 "));
			addDoc(iwriter, new Article("Spark动态资源分配-Dynamic Resource Allocation", "2016-01-01 16:27", "lxw1234@qq.com"));
			addDoc(iwriter, new Article("HBase高可用原理与实践", "2015-01-01 16:27", "jiang hongxiang"));
			
			iwriter.close();

			// 搜索过程**********************************
			// 实例化搜索器
			ireader = DirectoryReader.open(directory);
			isearcher = new IndexSearcher(ireader);

			String keyword = "Spark";
			Query query = new TermQuery (new Term (fieldName, keyword));
			System.out.println("query:"+query);
			// 搜索相似度最高的5条记录
			TopDocs topDocs = isearcher.search(query, 5);
			System.out.println("命中：" + topDocs.totalHits);
			// 输出结果
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int i = 0; i < topDocs.totalHits; i++) {
				Document targetDoc = isearcher.doc(scoreDocs[i].doc);
				System.out.println("内容：" + targetDoc.toString());
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ireader != null) {
				try {
					ireader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (directory != null) {
				try {
					directory.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void addDoc(IndexWriter iwriter, Article article) throws IOException {
		// 写入索引
		Document doc = new Document();
		doc.add(new TextField("title", article.getTitle(), Field.Store.YES));
		doc.add(new StringField("author", article.getAuthor(), Field.Store.YES));
		doc.add(new StringField("datetime", article.getDatetime(), Field.Store.YES));
		iwriter.addDocument(doc);
	}

}
