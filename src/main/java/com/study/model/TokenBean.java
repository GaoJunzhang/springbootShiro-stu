package com.study.model;

import lombok.Data;

/**
 * Created by user on 2018/3/20.
 */
@Data
public class TokenBean {
    private String access_token;
    private String token_type;
    private long expires_in;
}
