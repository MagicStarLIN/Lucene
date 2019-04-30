package com.lcl.Query;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class QueryTest {
    @Test
    public void QueryTest1() throws IOException, ParseException {
        //1 创建读取目录对象
        FSDirectory fsDirectory = FSDirectory.open(new File("C:\\indexDir"));
        //2 创建索引读取工具
        IndexReader indexReader = DirectoryReader.open(fsDirectory);
        //3 创建索引搜索工具
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //4 创建查询解析器
        // 创建查询解析器,两个参数：默认要查询的字段的名称，分词器
        QueryParser queryParser = new QueryParser("title", new IKAnalyzer());
        //5 创建查询对象
        Query query = queryParser.parse("谷歌");
        //6 搜索数据
        // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
        // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
        TopDocs topDocs = indexSearcher.search(query, 10);
        //7 各种操作
        //7.1 获取返回的条数
        System.out.println("本次搜索找到"+topDocs.totalHits+"条数据");
        // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 获取内容
        for (ScoreDoc scoreDoc : scoreDocs) {
            //获取ID
            int docID = scoreDoc.doc;
            //根据ID去找文档
            Document doc = indexReader.document(docID);
            System.out.print("文档id："+doc.get("id"));
            System.out.print("文档内容："+doc.get("title"));
            //获取得分
            System.out.println("文档得分："+scoreDoc.score);
        }
        //查询解析器 两个参数 要查询的字段和解析器
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(new String[] {"id","titile"},
        new IKAnalyzer());
        //创建查询对象
        Query query1 = multiFieldQueryParser.parse("1");
        //关闭查询器
        indexReader.close();
        fsDirectory.close();
    }
}


