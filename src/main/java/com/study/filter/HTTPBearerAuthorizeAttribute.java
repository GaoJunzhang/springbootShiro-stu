package com.study.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.config.Audience;
import com.study.result.JsonResult;
import com.study.result.ResultCode;
import com.study.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 2018/3/20.
 */
public class HTTPBearerAuthorizeAttribute implements Filter {

    @Autowired
    private Audience audienceEntity;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
    * class_name: 初始化过滤器
    * param: 
    * describe: TODO
    * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
    * creat_date: 2018/3/21
    * creat_time: 9:30
    **/
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());

    }

    /**
    * class_name: 实现过滤器
    * param: 
    * describe: TODO
    * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
    * creat_date: 2018/3/21
    * creat_time: 9:30
    **/
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub

        JsonResult jsonResult;
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String auth = httpRequest.getHeader("Authorization");
        if ((auth != null) && (auth.length() > 7))
        {
            String HeadStr = auth.substring(0, 6).toLowerCase();
            if (HeadStr.compareTo("bearer") == 0)
            {

                auth = auth.substring(6, auth.length());
                Claims claims = JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret());
                if ( claims!= null)
                {
                    request.setAttribute("username",claims.get("username"));
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();

//        jsonResult = new ResultMsg(ResultStatusCode.INVALID_TOKEN.getErrcode(), ResultStatusCode.INVALID_TOKEN.getErrmsg(), null);
        jsonResult = new JsonResult(ResultCode.INVALID_AUTHCODE,"invalid token",null);
        httpResponse.getWriter().write(mapper.writeValueAsString(jsonResult));
        return;
    }

    /**
    * class_name: 销毁过滤器
    * param: 
    * describe: TODO
    * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
    * creat_date: 2018/3/21
    * creat_time: 9:30
    **/
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        log.info("" + getClass() + " destroy");
    }
}
