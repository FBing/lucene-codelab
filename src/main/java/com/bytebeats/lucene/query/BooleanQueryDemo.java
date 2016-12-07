package com.bytebeats.lucene.query;

import com.bytebeats.lucene.model.Shop;
import com.bytebeats.lucene.util.IoUtils;
import com.bytebeats.lucene.util.LuceneUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.util.Arrays;

public class BooleanQueryDemo extends BaseDemo {

	@Override
	public void start() {

		// 实例化IKAnalyzer分词器
		Analyzer analyzer = new IKAnalyzer(true);

		Directory directory = null;
		IndexWriter writer = null;
		IndexReader reader = null;
		IndexSearcher searcher = null;
		try {
			// 建立内存索引对象
			directory = new RAMDirectory();

			writer = LuceneUtils.createIndexWriter(analyzer, directory, OpenMode.CREATE_OR_APPEND);
			// 写入索引
			addDoc(writer, new Shop("1", "肯德基(国展店)", "北京市朝阳区国际展览中心", Arrays.asList("010-9999"), 2, 0, 0));
			addDoc(writer, new Shop("2", "肯德基(太阳宫店)", "北京市朝阳区太阳宫", Arrays.asList("010-8888"), 2, 0, 0));
			addDoc(writer, new Shop("10", "肯德基(国展店)", "上海市宝安区", Arrays.asList("021-5555"), 1, 0, 0));

			IoUtils.closeQuietly(writer);

			// 搜索过程**********************************
			// 实例化搜索器
			reader = DirectoryReader.open(directory);
			searcher = new IndexSearcher(reader);

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
			TopDocs topDocs = searcher.search(booleanQuery, 5);
			System.out.println("命中：" + topDocs.totalHits);
			// 输出结果
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int i = 0; i < topDocs.totalHits; i++) {
				Document doc = searcher.doc(scoreDocs[i].doc);
				System.out.println("third_id:" + doc.get("third_id")+""+doc.get("name")+
						",address:"+doc.get("address")+",phone:"+doc.get("phone")+",city_id:"+doc.get("city_id"));
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IoUtils.closeQuietly(reader);
		}
	}

}
