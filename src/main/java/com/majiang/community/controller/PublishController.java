package com.majiang.community.controller;

import com.majiang.community.mapper.QuestionMapper;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.Question;
import com.majiang.community.model.User;
import org.apache.catalina.mbeans.UserMBean;
import org.apache.commons.lang3.StringUtils;
import org.h2.api.UserToRolesMapper;
import org.h2.util.ScriptReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.rmi.MarshalledObject;

@Controller
public class PublishController {

    @Autowired
    private  QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public  String publish(){
//        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public  String doPublish(
//            这里对应前端页面中的输入的信息，提交后在这获取去这三个参数。
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Long id,
            HttpServletRequest request,
            Model model){
//        一般在前端来做逻辑判断，这里在服务层，为model添加属性，判断后在前端提示错误信息，前端页面中"${}"来获取s的值，表示有错误信息传到了前端页面。
//        addattribute的作用就是前端提交信息。
        model.addAttribute("title" ,title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title==null || title =="") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description =="") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag==null || tag =="") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

//        从cookie中获取登录用户信息。
        Cookie[] cookies=request.getCookies();
        User user=null;
        if (cookies !=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {  //得到了名字token
                    String token = cookie.getValue();//获取cookie中的token.
                    user = userMapper.findByToken(token);//使用该token在从数据库中查找到相应的user
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                        //有了银行卡cookie就能获得token,添加到session中.这里的作用是把账户信息存到里面，相当于缓存。
//                        因此在登录页面的时候就不用再重复登陆了.
                    }
                    break;
                }
            }
        }
//        用户未登录情况
        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
//        为question对象set进设置去获取的前端的内容
        Question question=new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
//        question.setId(id);
        questionMapper.create(question);
//        model.addAttribute("保存成功！！！");
        return "redirect:/";
    }
}
