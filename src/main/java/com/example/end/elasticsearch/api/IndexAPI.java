package com.example.end.elasticsearch.api;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 索引API
 */
@SpringBootTest
public class IndexAPI {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 提供的文档来源为 String
     */
    @Test
    public void test() {
        IndexRequest request = new IndexRequest("IndexAPI");
        request.id("1");
        String jsonStr = "{\"name\":\"王明超\",\"age\":\"12\",\"date\":\"2021:09:09\"}";
        request.source(jsonStr, XContentType.JSON);
    }


    /**
     *  map  提供为的文档源，该源Map自动转换为JSON格式
     */
    @Test
    public void test6() {
        IndexRequest request=new IndexRequest("indexApi").id("1");
        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("name","map");
        jsonMap.put("age","12");
        jsonMap.put("birthday","2045.02.09");
        request.source(jsonMap);
    }



    /**
     * 作为Object密钥对提供的文档源，将其转换为JSON格式
     */
    @Test
    public void test8() {
        IndexRequest request=new IndexRequest("indexApi").id("1").source("name","王明超","age","12","message","半缘修道半缘君");
        System.out.println(restHighLevelClient);
    }


    /**
     * 作为XContentBuilder对象提供的文档源，Elasticsearch内置助手可生成JSON内容
     */
    @Ignore
    @Test
    public void test7() throws Exception{

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.timeField("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("1").source(builder);
    }


    /************可选参数***************/
    @Test
    public void test9() {
        IndexRequest request=new IndexRequest("index");
        // 路由值
        request.routing("routing");
        //等待主要分片变为超时的:如果不能立即执行索引操作，则等待超时。默认为{@code 1m}。
        request.timeout(TimeValue.timeValueDays(1));
        //同上
        request.timeout("1");
        //这个请求应该触发刷新吗： 刷新策略作为WriteRequest.RefreshPolicy实例
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        //同上
        request.setRefreshPolicy("wait_for");
        //版本
        request.version(2);
        request.versionType(VersionType.EXTERNAL);
        //作为DocWriteRequest.OpType值提供的操作类型:创建资源。如果存在索引，只需将其添加到索引中
        //文档中使用id，那么它就不会被删除。
        request.opType(DocWriteRequest.OpType.CREATE);
        //同上
        request.opType("create");
        //索引文档之前要执行的摄取管道的名称
        request.setPipeline("pipeline");



    }

    @Test
    public void test10() {
        System.out.println(restHighLevelClient);
    }


    public static void main(String[] args) {


        IndexRequest request = new IndexRequest("posts");
        request.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);


//除了String上面显示的示例之外，还可以通过其他方式提供文档源
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("1").source(jsonMap);


        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("user", "kimchy");
                builder.timeField("postDate", new Date());
                builder.field("message", "trying out Elasticsearch");
            }
            builder.endObject();
            IndexRequest indexRequest2 = new IndexRequest("posts")
                    .id("1").source(builder);

        } catch (Exception e) {

        }
    }


    @Test
    public void test5() {

        for (int i = 0; i < 30; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("@Test\n" +
                    "public void test" + i + "(){\n" +
                    "     System.out.println(restHighLevelClient);\n" +
                    "}");
            System.out.println(stringBuilder.toString());
        }
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test11() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test12() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test13() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test14() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test15() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test16() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test17() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test18() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test19() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test20() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test21() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test22() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test23() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test24() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test25() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test26() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test27() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test28() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test29() {
        System.out.println(restHighLevelClient);
    }

}