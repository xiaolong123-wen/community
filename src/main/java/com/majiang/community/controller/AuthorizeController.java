package com.majiang.community.controller;

import com.majiang.community.dto.AccessTokenDto;
import com.majiang.community.dto.GithubUser;
import com.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
*
* */
@Controller
public class AuthorizeController {
//    因为登录后返回callback,所以要有这个方法,携带需要的参数.表现层调用这个方法,直接获取到.
    @Autowired
    private GithubProvider githubProvider;
//原理时spring启动的时候,回去读取properties配置文件,将k-v对读到一个map里,等到用的时候直接赋值.
    @Value("${github.client_id}")
    private  String client_id;
    @Value("${github.Client_secret}")
    private  String client_secert;
    @Value("${github.Redirect_uri}")
    private  String client_Redirect_uri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state)
                           {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(client_id);
        accessTokenDto.setClient_secret(client_secert);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(client_Redirect_uri);
        accessTokenDto.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
//        得到我们实际的accesstoken后去获取用户信息.
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
