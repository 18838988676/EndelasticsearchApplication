package com.example.end.elasticsearch.utils;

import com.example.end.elasticsearch.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlParseUtil {

    public static void main(String[] args)throws Exception {
        new HtmlParseUtil().parseJd("心理学").forEach(System.out::println);
    }
    public List<Content> parseJd(String keywords)throws Exception{
        String url = "https://search.jd.com/Search?keyword=" + keywords;

        //解析网页 （jsoup返回的document就是浏览器返回的Document对象）
        Document document = Jsoup.parse(new URL(url), 3000);

        //所有在js中的方法，都可以用
        Element element = document.getElementById("J_goodsList");

        //获取所有的li元素
        Elements elements = element.getElementsByTag("li");

        List<Content> goodList =new ArrayList<>();

        for (Element el: elements){

            String img = el.getElementsByTag("img").eq(0).attr("src");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            Content content = new Content();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(title);
            goodList.add(content);

        }
        return goodList;


    }


}
