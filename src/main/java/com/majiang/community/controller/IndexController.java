package com.majiang.community.controller;


import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/*通篇阅读文档即可了解使用的整个过程，spirng官网。
*spring提供了一个model,将信息传递到页面中.map形式存储数据，
* 页面信息存到model中，传递到前端使用。
* 使用github登录
* */
@Controller
public class IndexController {
     @Autowired
     private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size
//                        @RequestParam(name = "search", required = false) String search,
//                        @RequestParam(name = "tag", required = false) String tag,
//                        @RequestParam(name = "sort", required = false) String sort
    ){
//请求cookie用request的getcookie,获取登录信息. 使用拦截器做登录验证操作。
//返回表现层，这里是要取得用户发布的问题的信息.
        PaginationDTO pagination=questionService.list(page,size);
//返回到前端，前端页面循环显示.
        model.addAttribute("pagination",pagination);
        return "index";
    }

}
