package com.example.end.elasticsearch.api;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationOperation;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.rest.RestStatus;
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
     * map  提供为的文档源，该源Map自动转换为JSON格式
     */
    @Test
    public void test6() {
        IndexRequest request = new IndexRequest("indexApi").id("1");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "map");
        jsonMap.put("age", "12");
        jsonMap.put("birthday", "2045.02.09");
        request.source(jsonMap);
    }


    /**
     * 作为Object密钥对提供的文档源，将其转换为JSON格式
     */
    @Test
    public void test8() {
        IndexRequest request = new IndexRequest("indexApi").id("1").source("name", "王明超", "age", "12", "message", "半缘修道半缘君");
        System.out.println(restHighLevelClient);
    }


    /**
     * 作为XContentBuilder对象提供的文档源，Elasticsearch内置助手可生成JSON内容
     */
    @Ignore
    @Test
    public void test7() throws Exception {

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
        IndexRequest request = new IndexRequest("index");
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

    /************同步执行***************/

    //当以以下方式执行IndexRequest时，客户端等待IndexResponse返回，然后继续执行代码:
    //在高级REST客户端解析REST响应失败、请求超时或没有从服务器返回响应的类似情况下，同步调用可能抛出IOException。
    //在服务器返回4xx或5xx错误代码的情况下，高级客户端尝试解析响应体错误细节，然后抛出一个通用的ElasticsearchException，
    // 并将原始的ResponseException作为一个被抑制的异常添加到其中。
    //Synchronous executionedit
    @Test
    public void test10() throws Exception {
        IndexRequest request = new IndexRequest("Synchronous").id("1").source("name", "mamba", "age", "12");
        restHighLevelClient.index(request, RequestOptions.DEFAULT);

    }

    /************异步执行***************/
    //执行aIndexRequest也可以异步方式完成，以便客户端可以直接返回。用户需要通过将请求和侦听器传递给异步索引方法来指定如何处理响应或潜在的失败：
    @Test
    public void test11() {
        IndexRequest request = new IndexRequest("Synchronous").id("1").source("name", "mamba", "age", "12");
        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                //执行成功完成时调用。
            }

            @Override
            public void onFailure(Exception e) {
                //当整体IndexRequest失败时调用。
            }
        };
        restHighLevelClient.indexAsync(request, RequestOptions.DEFAULT, listener);

        restHighLevelClient.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    //String index = indexResponse.getIndex();
    //String id = indexResponse.getId();
    //if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
    //
    //} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
    //
    //}
    //ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
    //if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
    //
    //}
    //if (shardInfo.getFailed() > 0) {
    //    for (ReplicationResponse.ShardInfo.Failure failure :
    //            shardInfo.getFailures()) {
    //        String reason = failure.reason();
    //    }
    //}

    /************索引响应编辑 ***************/
    @Test
    public void test12() throws Exception {
        IndexRequest request = new IndexRequest("Synchronous").id("1").source("name", "mamba", "age", "12");
        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        String id = indexResponse.getId();
        String index = indexResponse.getIndex();

        //处理（如果需要）首次创建文档的情况
        if(indexResponse.getResult()== DocWriteResponse.Result.CREATED){

        //处理（如果需要）文档已存在而被重写的情况
        }else if(indexResponse.getResult()==DocWriteResponse.Result.UPDATED){
        }

        //处理成功分片数量少于总分片数量的情况
        ReplicationResponse.ShardInfo shardInfo=indexResponse.getShardInfo();
        if(shardInfo.getTotal()!=shardInfo.getSuccessful()){


        }

        //处理潜在的故障
        if(shardInfo.getFailed()>0){
            for (ReplicationResponse.ShardInfo.Failure failure:shardInfo.getFailures()){
                String reason = failure.reason();

            }
        }

        IndexRequest request2 = new IndexRequest("posts")
                .id("1")
                .source("field", "value")
                .setIfSeqNo(10L)
                .setIfPrimaryTerm(20);
        try {
            IndexResponse response = restHighLevelClient.index(request2, RequestOptions.DEFAULT);
        } catch(ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                    //引发的异常表明已返回版本冲突错误
            }
        }

//        如果将opType设置为create，并且已经存在具有相同索引和ID的文档，则会发生相同的情况

        IndexRequest request3 = new IndexRequest("posts")
                .id("1")
                .source("field", "value")
                .opType(DocWriteRequest.OpType.CREATE);
        try {
            IndexResponse response = restHighLevelClient.index(request3, RequestOptions.DEFAULT);
        } catch(ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                //引发的异常表明已返回版本冲突错误

            }
        }

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