package com.study.controller;

import com.study.config.Audience;
import com.study.model.TokenBean;
import com.study.model.User;
import com.study.result.JsonResult;
import com.study.result.ResultCode;
import com.study.service.UserService;
import com.study.util.JwtHelper;
import com.study.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by user on 2018/3/20.
 */
@RestController
public class JsonWebToken {
    @Autowired
    private UserService userService;

    @Autowired
    private Audience audienceEntity;

    @RequestMapping("oauth/token")
    public Object getAccessToken(@RequestBody User user) {
        JsonResult jsonResult;
        if (StringUtils.isEmpty(user.getUsername())) {
            jsonResult = new JsonResult(ResultCode.PARAMS_ERROR, "账号为空", null);
            return jsonResult;
        }
        User userBean = userService.selectByUsername(user.getUsername());
        if (userBean == null) {
            jsonResult = new JsonResult(ResultCode.NOT_LOGIN, "账号错误", null);
            return jsonResult;
        } else {
            String md5Password = MD5Util.encode(user.getPassword());

            if (md5Password.compareTo(userBean.getPassword()) != 0) {
                jsonResult = new JsonResult(ResultCode.NOT_LOGIN, "密码错误", null);
                return jsonResult;
            }
        }
        //拼装accessToken
        String accessToken = JwtHelper.createJWT(user.getUsername(), String.valueOf(user.getId()),
                "", audienceEntity.getClientId(), audienceEntity.getName(),
                audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());
        //返回accessToken
        TokenBean tokenBean = new TokenBean();
        tokenBean.setAccess_token(accessToken);
        tokenBean.setExpires_in(audienceEntity.getExpiresSecond());
        tokenBean.setToken_type("bearer");
        jsonResult = new JsonResult(ResultCode.SUCCESS, "登录成功", tokenBean);
        return jsonResult;
    }
}
