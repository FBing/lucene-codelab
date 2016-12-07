package com.bytebeats.lucene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import java.io.IOException;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 * @create 2016-12-07 18:31
 */
public class LuceneUtils {

    public static IndexSearcher createIndexSearcher(IndexReader reader) throws IOException {
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    public static IndexSearcher createIndexSearcher(Directory directory) throws IOException {
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    public static IndexWriter createIndexWriter(Analyzer analyzer, Directory directory) throws IOException {

        return createIndexWriter(analyzer, directory, IndexWriterConfig.OpenMode.CREATE);
    }

    public static IndexWriter createIndexWriter(Analyzer analyzer, Directory directory, IndexWriterConfig.OpenMode openMode) throws IOException {

        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(openMode);
        IndexWriter writer = new IndexWriter(directory, iwc);
        return writer;
    }

}
