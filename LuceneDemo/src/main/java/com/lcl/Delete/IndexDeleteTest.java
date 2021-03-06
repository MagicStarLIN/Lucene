package com.lcl.Delete;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class IndexDeleteTest {
    /*
     * 演示：删除索引
     * 注意：
     * 	一般，为了进行精确删除，我们会根据唯一字段来删除。比如ID
     * 	如果是用Term删除，要求ID也必须是字符串类型！
     */
    @Test
    public void testDelete() throws Exception {
        // 创建目录对象
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        // 创建索引写出工具
        IndexWriter writer = new IndexWriter(directory, conf);

        // 根据词条进行删除
        //		writer.deleteDocuments(new Term("id", "1"));

        // 根据query对象删除,如果ID是数值类型，那么我们可以用数值范围查询锁定一个具体的ID
        //		Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true);
        //		writer.deleteDocuments(query);

        // 删除所有
        writer.deleteAll();
        // 提交
        writer.commit();
        // 关闭
        writer.close();
    }

}
