//package com.example.end.elasticsearch.test;
//
//
//import com.example.end.elasticsearch.pojo.Content;
//import com.example.end.elasticsearch.pojo.ParamPI;
//import org.elasticsearch.action.search.*;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.Scroll;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.springframework.boot.test.context.SpringBootTest;
//import sun.jvm.hotspot.debugger.Page;
//
//import javax.annotation.security.RunAs;
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Map;
//
//
//@SpringBootTest
//public class PageTest {
//
//
//
//    public Page<Content> searchSystemDoc(ParamPI paramPI) throws IOException, ParseException {
//
//        Page page = new Page(paramPI.getPageNum(),paramPI.getPageSize());
//        final Scroll scroll = new Scroll(TimeValue.timeValueSeconds(60));
//
//        // 1、创建search请求
//        SearchRequest searchRequest = new SearchRequest("jd_goods");
//        searchRequest.scroll(scroll);
//
//        // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().trackTotalHits(true);
//
//        //Settings settings = Settings.builder().put("syslog_system.max_result_window", 500000000).build();
//        //match分析器，模糊匹配  term精准  multi_match多个字段同时进行匹配
//        // bool包含四种操作符，分别是must,should,must_not,query filter查询查询对结果进行缓存
//        //sourceBuilder.query(QueryBuilders.termQuery("age", 24));
//        //分页设置  浅分页  //深分页 scroll
////        int from  = (paramPI.getPageNum()-1)*paramPI.getPageSize();
////        sourceBuilder.from(from);
//        //sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//        sourceBuilder.size(paramPI.getPageSize());
//
//        //搜索条件
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        if(paramPI.getKeyword() != null){
//            QueryBuilder queryBuilder =  QueryBuilders.matchPhraseQuery("title", paramPI.getKeyword());
//            boolQueryBuilder.should(queryBuilder);
//        }
//        if(paramPI.getPrice() != null){
//            QueryBuilder queryBuilder = QueryBuilders.termQuery("price",paramPI.getPrice());
//            boolQueryBuilder.must(queryBuilder);
//        }
//        if(paramPI.getLogtype() != null ){
//            //elasticsearch 里默认的IK分词器是会将每一个中文都进行了分词的切割，所以你直接想查一整个词  加上.keyword
//            QueryBuilder queryBuilder = QueryBuilders.termQuery("type",paramPI.getLogtype());
//            boolQueryBuilder.must(queryBuilder);
//        }
//        QueryBuilder queryBuilder1  = QueryBuilders.rangeQuery("hapeen_time_stamp").from(paramPI.getStartdate().getTime()).to(paramPI.getEnddate().getTime());
//        boolQueryBuilder.must(queryBuilder1);
//
//        QueryBuilder queryBuilder2  = QueryBuilders.rangeQuery("s_ip_num").from(paramPI.getSip1()).to(paramPI.getSip2());
//        boolQueryBuilder.must(queryBuilder2);
//
//        QueryBuilder queryBuilder3  = QueryBuilders.rangeQuery("d_ip_num").from(paramPI.getDip1()).to(paramPI.getDip2());
//        boolQueryBuilder.must(queryBuilder3);
////        if(paramPI.getNetStruct() != null && !paramPI.getNetStruct().isEmpty()){
////            List<NetStruct> list = paramPI.getNetStruct();
////            for(NetStruct netStruct : list){
////                QueryBuilder queryBuilder  = QueryBuilders.rangeQuery("d_ip_num").from(netStruct.getStartIp()).to(netStruct.getEndIp());
////                boolQueryBuilder.must(queryBuilder);
////            }
////        }
//        sourceBuilder.query(boolQueryBuilder);
//        //将请求体加入到请求中
//        searchRequest.source(sourceBuilder);
//
//        //3、发送请求
//        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        //处理搜索命中文档结果
//        SearchHits hits = searchResponse.getHits();
//        long totalHits = hits.getTotalHits().value;
//
//        List<SyslogOriginalSystem> list = new ArrayList<>();
//        String scrollId = null;
//        int pageNum = paramPI.getPageNum();
//        int i = 1;
//        while (searchResponse.getHits().getHits().length != 0){
//            if(i == pageNum){
//                //业务
//                for (SearchHit hit : hits.getHits()) {
//                    Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
//                    setDataSystem(sourceAsMap,list);
//                }
//                System.out.println("ES分页查询成功");
//                break;
//            }
//
//            i++;
//            //每次循环完后取得scrollId,用于记录下次将从这个游标开始取数
//            scrollId = searchResponse.getScrollId();
//            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
//            scrollRequest.scroll(scroll);
//            searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
//        }
//
//        if(scrollId != null){
//            //清除滚屏
//            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
//            //也可以选择setScrollIds()将多个scrollId一起使用
//            clearScrollRequest.addScrollId(scrollId);
//            ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest,RequestOptions.DEFAULT);
//        }
//
//        page.setTotal(totalHits);
//        return (Page<SyslogOriginalSystem>) page.setRecords(list);
//    }
//
//}
