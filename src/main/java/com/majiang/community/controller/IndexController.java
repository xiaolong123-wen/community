package com.majiang.community.controller;

import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.User;
import org.apache.catalina.mbeans.UserMBean;
import org.h2.api.UserToRolesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*通篇阅读文档即可了解使用的整个过程，spirng官网。
*spring提供了一个model,将信息传递到页面中.map形式存储数据，
* 页面信息存到model中，传递到前端使用。
* 使用github登录
* */
@Controller
public class IndexController {
     @Autowired
    private UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request){
//请求cookie用request的getcookie,获取.
        Cookie[] cookies=request.getCookies();
        if (cookies !=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {  //得到了名字token
                    String token = cookie.getValue();//获取cookie中的token.
                    User user = userMapper.findByToken(token);//使用该token在从数据库中查找到相应的user
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                        //有了银行卡cookie就能获得token,添加到session中.这里的作用是把账户信息存到里面，相当于缓存。
//                        因此在登录页面的时候就不用再重复登陆了.
                    }
                    break;
                }
            }
        }
        return "index";
    }

}
