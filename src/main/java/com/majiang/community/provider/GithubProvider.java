package com.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import com.majiang.community.dto.AccessTokenDto;
import com.majiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

//使用这个注解,初始化后spring容器中的上下文,不需要实例化对象.
//如果参数超过两个就要封装成对象来做.
@Component
public class GithubProvider {
//    需要做post请求.获取登录成功时返回的accesstoken,来保存当前登录态.
    public  String getAccessToken(AccessTokenDto accessTokenDto){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string= response.body().string();
//                这里时执行登录请求后返回给我们了一个token字符串,分隔得到我们的accesstoken验证.
                String token= string.split("&")[0].split("=")[1];
                return token;
        } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
//    有了登录请求,前端返回accesstoken获取到,得到user信息,请求用户信息在前端中更新登录态.
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string= response.body().string();
//            自动转换为java类对象.
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
