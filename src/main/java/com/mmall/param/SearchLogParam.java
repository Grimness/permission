package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liliang
 * @date 2017/11/30.
 */
@Getter
@Setter
@ToString
public class SearchLogParam {

    private Integer type;

    private String beforeSeq;

    private String afterSeq;

    private String operator;

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String fromTime;

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String toTime;
}
