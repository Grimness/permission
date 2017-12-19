package com.mmall.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liliang
 * @date 2017/11/27.
 */
public class StringUtil {

    /**
     * 1,2,3,4,5 --> [1,2,3,4,5]
     * 注意 : a,3,4,5 报错
     * @param str
     * @return
     */
    public static List<Integer> splitToListInt(String str) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return strList.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
    }
}
