package com.example.end.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.example.end.elasticsearch.pojo.User;
import org.apache.lucene.search.TermQuery;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EndelasticsearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
        System.out.println("d" + restHighLevelClient);
    }


    //创建索引
    @Test
    public void createIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("idea-create-index01");
        CreateIndexResponse create = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(create);
    }

    //判断索引是否存在
    @Test
    public void isHaveIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest("idea-create-index01");
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //删除索引
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("idea-create-index01");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    //添加文档
    @Test
    public void createDoc() throws IOException {
        User user = new User();
        user.setAge(99);
        user.setName("中华人民共和国");
        IndexRequest indexRequest = new IndexRequest("idea-create-index01");
        indexRequest.id("00002");
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        indexRequest.timeout("1s");
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse.getIndex());
        System.out.println(indexResponse.status());
    }

    //判断文档是否存在
    @Test
    public void isHaveDoc() throws IOException {
        GetRequest getRequest = new GetRequest("idea-create-index01", "001");
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);

    }

    //获得一个文档内容
    @Test
    public void getDoc() throws IOException {
        GetRequest request = new GetRequest("idea-create-index01", "001");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        System.out.println(response);
    }

    //更新文档
    @Test
    public void updateDoc() throws IOException {

        UpdateRequest updateRequest = new UpdateRequest("idea-create-index01", "001");

        User user = new User();
        user.setAge(99);

        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);

        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(update.status());

    }

    //删除文档
    @Test
    public void deleteDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("idea-create-index01", "001");
        User user = new User();
        user.setAge(99);
        deleteRequest.id("001");
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(delete.status());

    }


    //批量增加
    @Test
    public void bulkAdd() throws IOException {

        List<User> userList = new ArrayList<>();

        userList.add(new User("云彩--百度1", 1));
        userList.add(new User("云彩--上上签", 2));
        userList.add(new User("云彩--优乐美", 3));
        userList.add(new User("云彩--方便面", 4));
        userList.add(new User("云彩--狼牙山", 5));
        userList.add(new User("云彩--到王家了", 6));
        userList.add(new User("云彩--百度222", 7));
        userList.add(new User("云彩--百度王43", 8));
        userList.add(new User("云彩--百度df王as", 9));

        BulkRequest bulkRequest = new BulkRequest("idea-create-index01");


        for (int i = 0; i < userList.size(); i++) {

            bulkRequest.add(new IndexRequest().id("0" + i).source(JSON.toJSONString(userList.get(i)), XContentType.JSON));
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.status());
    }

    //查询
    @Test
    public void search() throws IOException {
        SearchRequest searchRequest = new SearchRequest("idea-create-index01");

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();

        TermQueryBuilder term= QueryBuilders.termQuery("name","云彩--百度1");

        searchSourceBuilder.query(term);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));

        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }





}
