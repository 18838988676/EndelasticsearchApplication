package com.example.end.elasticsearch.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamPI {

    private int pageNum;
    private int pageSize;
private String keyword;
private String price;
private String logtype;
private Date startdate;
    private Date enddate;
    private String sip1;
    private String dip1;
    private String sip2;
    private String dip2;
 private int total;
}