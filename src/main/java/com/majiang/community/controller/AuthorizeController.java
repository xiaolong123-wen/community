package com.majiang.community.controller;

import com.majiang.community.dto.AccessTokenDto;
import com.majiang.community.dto.GithubUser;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.User;
import com.majiang.community.provider.GithubProvider;
import org.h2.api.UserToRolesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    private UserMapper userMapper;

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
                           HttpServletRequest request){
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
//        System.out.println(githubUser.getName());
        if (githubUser!=null){
//          获取到user信息后存入数据库，使用user对象，
            User user=new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAcccontId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
//          登录成功写cookie,session
            request.getSession().setAttribute("user",githubUser);  //获取session设置账户信息,将user存储到session中.有了银行卡
            return "redirect:/"; //登录成功后重定向到index页面
        }
        else{
//            失败,重新登录;
            return "redirect:/";
        }
    }
}
