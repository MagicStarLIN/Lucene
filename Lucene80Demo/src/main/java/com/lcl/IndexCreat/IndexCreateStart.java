package com.lcl.IndexCreat;

import com.lcl.Util.ProductUtil;
import com.lcl.ikAnalyzer.MyIkAnalyzer;
import com.lcl.pojo.Product;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class IndexCreateStart {

    /**
     * 创建Document对象
     * @param p
     * @return
     * @throws IOException
     */
    private Document docCreat(Product p) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("id", String.valueOf(p.getId()), Field.Store.YES));
        doc.add(new TextField("name", p.getName(), Field.Store.YES));
        doc.add(new TextField("category", p.getCategory(), Field.Store.YES));
        doc.add(new TextField("price", String.valueOf(p.getPrice()), Field.Store.YES));
        doc.add(new TextField("place", p.getPlace(), Field.Store.YES));
        doc.add(new TextField("code", p.getCode(), Field.Store.YES));
        return doc;
    }

    /**
     * 批量添加集合创建
     * @param document
     */
    private  void docsCreat(List<Document> documents, Document document){
        documents.add(document);

    }
    @Test
    public void indexCreat() throws IOException {
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("C:\\java\\indexDir8"));
//        Analyzer myIkAnalyzer = new IKAnalyzer();
        Analyzer myIkAnalyzer=new MyIkAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(myIkAnalyzer);
        List<Document> documents = new ArrayList<>();
        IndexWriter writer = new IndexWriter(fsDirectory, conf);
        List<Product> products = ProductUtil.listCreat();
        for (Product product : products) {
            docsCreat(documents,docCreat(product));
        }
        System.out.println("documents长度为："+documents.size());
        long t1 = System.currentTimeMillis();
        writer.addDocuments(documents);
        long t2 = System.currentTimeMillis();
        System.out.println("lucene6.0 索引经过"+(t2-t1)+"ms建立成功");
        writer.commit();
        writer.close();

    }
}
