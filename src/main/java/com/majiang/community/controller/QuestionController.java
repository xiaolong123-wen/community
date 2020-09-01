package com.majiang.community.controller;

import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(value = "id",required = false) Long id,
                           Model model){
        QuestionDTO questionDTO= questionService.getById(id);
//        添加阅读数的更新（因为里层方法查询和更新都有，因此在外层创建方法。）
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
