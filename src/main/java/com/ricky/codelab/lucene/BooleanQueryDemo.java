package com.ricky.codelab.lucene;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
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
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ricky.codelab.lucene.model.Shop;

public class BooleanQueryDemo {

	public static void main(String[] args) {

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
			addDoc(iwriter, new Shop("1", "肯德基(国展店)", "北京市朝阳区国际展览中心", Arrays.asList("010-9999"), 2, 0, 0));
			addDoc(iwriter, new Shop("2", "肯德基(太阳宫店)", "北京市朝阳区太阳宫", Arrays.asList("010-8888"), 2, 0, 0));
			addDoc(iwriter, new Shop("10", "肯德基(国展店)", "上海市宝安区", Arrays.asList("021-5555"), 1, 0, 0));
			
			iwriter.close();

			// 搜索过程**********************************
			// 实例化搜索器
			ireader = DirectoryReader.open(directory);
			isearcher = new IndexSearcher(ireader);

			String keyword = "肯德基(国展店)";
			QueryParser qp = new QueryParser("name", analyzer);
//			qp.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query name_query = qp.parse(keyword);
			System.out.println("name_query:"+name_query);
			
			Query city_query = new TermQuery (new Term ("city_id", 2+""));
			System.out.println("city_query:"+city_query);
			
			BooleanQuery booleanQuery = new BooleanQuery.Builder()
				.add(name_query, Occur.MUST)
				.add(city_query, Occur.MUST)
				.build();
			
			// 搜索相似度最高的5条记录
			TopDocs topDocs = isearcher.search(booleanQuery, 5);
			System.out.println("命中：" + topDocs.totalHits);
			// 输出结果
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int i = 0; i < topDocs.totalHits; i++) {
				Document doc = isearcher.doc(scoreDocs[i].doc);
				System.out.println("third_id:" + doc.get("third_id")+""+doc.get("name")+
						",address:"+doc.get("address")+",phone:"+doc.get("phone")+",city_id:"+doc.get("city_id"));
				
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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

	public static void addDoc(IndexWriter iwriter, Shop shop) throws IOException {
		// 写入索引
		Document doc = new Document();
		doc.add(new StringField("third_id", shop.getThirdId(), Field.Store.YES));
		doc.add(new TextField("name", shop.getName(), Field.Store.YES));
		doc.add(new StringField("address", shop.getAddress(), Field.Store.YES));
		doc.add(new StringField("phone", StringUtils.join(shop.getPhone(), ","), Field.Store.YES));
		doc.add(new StringField("city_id", String.valueOf(shop.getCityId()), Field.Store.YES));
		
		iwriter.addDocument(doc);
	}

}
