package com.majiang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*通篇阅读文档即可了解使用的整个过程，spirng官网。
*spring提供了一个model,将信息传递到页面中.map形式存储数据，
* 页面信息存到model中，传递到前端使用。
* */
@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name") String name, Model model){
        model.addAttribute("name",name);
        return  "hello";
    }



}
