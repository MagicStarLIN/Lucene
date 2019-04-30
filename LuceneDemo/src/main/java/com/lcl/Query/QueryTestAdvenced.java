package com.lcl.Query;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class QueryTestAdvenced {
    //[1]创建基本查询方法
    public void Search(Query query) throws IOException {
        FSDirectory fsDirectory = FSDirectory.open(new File("C:\\indexDir"));
        IndexReader indexReader = DirectoryReader.open(fsDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query, 10);

        System.out.println("本次搜索找到"+topDocs.totalHits+"条数据");
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            int docID = scoreDoc.doc;
            Document doc = indexReader.document(docID);
            System.out.print("文档id：" + doc.get("id"));
            System.out.print("文档内容：" + doc.get("title"));
            System.out.println("文档得分：" + scoreDoc.score);
        }
        indexReader.close();
        fsDirectory.close();
    }
    /*
     * 测试普通词条查询
     * 注意：Term(词条)是搜索的最小单位，不可再分词。值必须是字符串！
     */
    @Test
    public void testTermQuery() throws Exception {
        // 创建词条查询对象
        Query query = new TermQuery(new Term("title", "谷歌"));
        Search(query);
    }
    /*
     * 测试通配符查询
     * 	? 可以代表任意一个字符
     * 	* 可以任意多个任意字符
     */
    @Test
    public void testWildCardQuery() throws Exception {
        // 创建查询对象
        Query query = new WildcardQuery(new Term("title", "*歌*"));
        Search(query);
    }
    /*
     * 测试模糊查询
     */
    @Test
    public void testFuzzyQuery() throws Exception {
        // 创建模糊查询对象:允许用户输错。但是要求错误的最大编辑距离不能超过2
        // 编辑距离：一个单词到另一个单词最少要修改的次数 facebool --> facebook 需要编辑1次，编辑距离就是1
//    Query query = new FuzzyQuery(new Term("title","fscevool"));
        // 可以手动指定编辑距离，但是参数必须在0~2之间
        Query query = new FuzzyQuery(new Term("title","facebool"),1);
        Search(query);
    }
    /*
     * 测试：数值范围查询
     * 注意：数值范围查询，可以用来对非String类型的ID进行精确的查找
     */
    @Test
    public void testNumericRangeQuery() throws Exception{
        // 数值范围查询对象，参数：字段名称，最小值、最大值、是否包含最小值、是否包含最大值
        Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true);
        Search(query);
    }
    /*
     * 布尔查询：
     * 	布尔查询本身没有查询条件，可以把其它查询通过逻辑运算进行组合！
     * 交集：Occur.MUST + Occur.MUST
     * 并集：Occur.SHOULD + Occur.SHOULD
     * 非：Occur.MUST_NOT
     */
    @Test
    public void testBooleanQuery() throws Exception{

        Query query1 = NumericRangeQuery.newLongRange("id", 1L, 3L, true, true);
        Query query2 = NumericRangeQuery.newLongRange("id", 2L, 4L, true, true);
        // 创建布尔查询的对象
        BooleanQuery query = new BooleanQuery();
        // 组合其它查询
        query.add(query1, BooleanClause.Occur.MUST_NOT);
        query.add(query2, BooleanClause.Occur.SHOULD);

        Search(query);
    }


}
