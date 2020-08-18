package com.majiang.community.controller;

import com.majiang.community.dto.AccessTokenDto;
import com.majiang.community.dto.GithubUser;
import com.majiang.community.model.User;
import com.majiang.community.provider.GithubProvider;
import com.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/*cookie 和session理解
1.session相当于账户.所有的账户都在数据库中存贮.
2.cookie相当于银行卡,每次去银行卡取钱,拿着银行卡,才能获取账户,操作银行里的余额.
*页面中有登录态的页面也都会存储放问过的cookie,设置时间,因此可以保登录态,服务存放着多个cookie,
拿到后,去数据库/缓存/服务器,去拿到cookie/session.找到i西南西,渲染到页面.
* */
@Controller
public class AuthorizeController {
//    因为登录后返回callback,所以要有这个方法,携带需要的参数.表现层调用这个方法,直接获取到.
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;
//原理时spring启动的时候,回去读取properties配置文件,将k-v对读到一个map里,等到用的时候直接赋值.
    @Value("${github.client_id}")
    private  String client_id;
    @Value("${github.Client_secret}")
    private  String client_secert;
    @Value("${github.Redirect_uri}")
    private  String client_Redirect_uri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
//        spring自动把上下文中的request放到这里面使用
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(client_id);
        accessTokenDto.setClient_secret(client_secert);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(client_Redirect_uri);
        accessTokenDto.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
//        得到我们实际的accesstoken后去获取用户信息.
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser!=null && githubUser.getId()!=null){
//          获取到user信息后存入数据库，使用user对象，
            User user=new User();
            String token=UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
//            上述数据库相当于写入实物session.返回浏览器设置cookie,
            response.addCookie(new Cookie("token",token));
//          登录成功写cookie,session
            return "redirect:/"; //登录成功后重定向到index页面
        }
        else{
//            失败,重新登录;
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie =new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";

    }
}
