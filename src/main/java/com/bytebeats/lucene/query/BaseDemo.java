package com.bytebeats.lucene.query;

import com.bytebeats.lucene.model.Shop;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 * @create 2016-12-07 18:59
 */
public abstract class BaseDemo {

    public static void main(String[] args) {

        new BooleanQueryDemo().start();
    }

    public abstract void start();

    public void addDoc(IndexWriter iwriter, Shop shop) throws IOException {
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
