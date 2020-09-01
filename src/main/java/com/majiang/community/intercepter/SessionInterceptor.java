package com.majiang.community.intercepter;

import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.User;
import com.majiang.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
//加上注解，让spring管理，有注入.
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

//    拦截器，前端的操作都要验证是否在登录状态。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies=request.getCookies();
        if (cookies !=null && cookies.length !=0 ){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {  //得到了名字token
                    String token = cookie.getValue();//获取cookie中的token.

                    UserExample userExample =new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token); //可以拼接sql语句
                    List<User> user = userMapper.selectByExample(userExample);//使用该token在从数据库中查找到相应的user

                    if (user.size() != 0) {
                        request.getSession().setAttribute("user", user.get(0));
                        //有了银行卡cookie就能获得token,添加到session中.这里的作用是把账户信息存到里面，相当于缓存。
//                        因此在登录页面的时候就不用再重复登陆了.
                    }
                    break;
                }
            }
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
