package com.lcl.Update;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class IndexUpdateTest {
    @Test
    public void TestUpdate() throws IOException {
        /* 测试：修改索引
         * 注意：
         * 	A：Lucene修改功能底层会先删除，再把新的文档添加。
         * 	B：修改功能会根据Term进行匹配，所有匹配到的都会被删除。这样不好
         * 	C：因此，一般我们修改时，都会根据一个唯一不重复字段进行匹配修改。例如ID
         * 	D：但是词条搜索，要求ID必须是字符串。如果不是，这个方法就不能用。
         * 如果ID是数值类型，我们不能直接去修改。可以先手动删除deleteDocuments(数值范围查询锁定ID)，再添加。
         */
        //1 创建文档存储目录
        FSDirectory fsDirectory = FSDirectory.open(new File("C:\\indexDir"));
        //2 创建索引写入器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        //3 创建索引写入器
        IndexWriter indexWriter = new IndexWriter(fsDirectory,indexWriterConfig);
        //4 创建文档数据
        //[1] 创建新的文档数据
        Document doc = new Document();
        doc.add(new StringField("id","1", Field.Store.YES));
        doc.add(new TextField("title","谷歌地图之父跳槽Alibaba ", Field.Store.YES));
        //5 修改
        /* 修改索引。参数：
         * 	词条：根据这个词条匹配到的所有文档都会被修改
         * 	文档信息：要修改的新的文档数据
         */
        indexWriter.updateDocument(new Term("id","1"),doc);
        //6 提交
        indexWriter.commit();
        //7 关闭
        indexWriter.close();

    }

}
