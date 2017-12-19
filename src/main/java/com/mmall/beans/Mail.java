package com.mmall.beans;

import lombok.*;

import java.util.Set;

/**
 * @author liliang
 * @date 2017/11/21.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    private String subject;

    private String message;

    private Set<String> receivers;
}
