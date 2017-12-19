package com.mmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author liliang
 * @date 2017/11/30.
 */
@Getter
@Setter
@ToString
public class SearchLogDto {

    /**
     * log type
     */
    private Integer type;

    private String beforeSeq;

    private String afterSeq;

    private String operator;

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private Date fromTime;

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private Date toTime;

}
