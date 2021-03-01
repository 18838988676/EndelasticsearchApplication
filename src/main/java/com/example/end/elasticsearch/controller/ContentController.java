package com.example.end.elasticsearch.controller;

import com.example.end.elasticsearch.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    //搜索京东的网站  并将数据存入数据库中
    //http://localhost:8080/parse/电视
    @GetMapping("/parse/{key}")
    public Boolean parse(@PathVariable("key") String keys)throws Exception{
        return contentService.parse(keys);
    }

    //http://localhost:8080/search/机/0/10
    @GetMapping("/search/{key}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable("key") String key,
                                           @PathVariable("pageNo")int pageNo,
                                           @PathVariable("pageSize")int pageSize) throws Exception{

        return contentService.search(key,pageNo,pageSize);
    }


    //http://localhost:8080/searchhigh/机/0/10
    @GetMapping("/searchhigh/{key}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> searchHign(@PathVariable("key") String key,
                                           @PathVariable("pageNo")int pageNo,
                                           @PathVariable("pageSize")int pageSize) throws Exception{

        return contentService.searchHign(key,pageNo,pageSize);
    }





}
