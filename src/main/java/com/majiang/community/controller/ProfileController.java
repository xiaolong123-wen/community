package com.majiang.community.controller;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.User;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

//        User user = (User) request.getSession().getAttribute("user");
        Cookie[] cookies=request.getCookies();
        User user =null;
        if (cookies !=null && cookies.length !=0 ){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {  //得到了名字token
                    String token = cookie.getValue();//获取cookie中的token.
                    user= userMapper.findByToken(token);//使用该token在从数据库中查找到相应的user
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                        //有了银行卡cookie就能获得token,添加到session中.这里的作用是把账户信息存到里面，相当于缓存。
//                        因此在登录页面的时候就不用再重复登陆了.
                    }
                    break;
                }
            }
        }
        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {
//            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
//            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新回复");
        }
        return "profile";
    }
}

