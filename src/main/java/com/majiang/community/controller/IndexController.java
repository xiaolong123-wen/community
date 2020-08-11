package com.majiang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*通篇阅读文档即可了解使用的整个过程，spirng官网。
*spring提供了一个model,将信息传递到页面中.map形式存储数据，
* 页面信息存到model中，传递到前端使用。
* 使用github登录
* */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

}
