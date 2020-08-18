package com.majiang.community.controller;

import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(value = "id",required = false) Integer id,
                           Model model){
        QuestionDTO questionDTO= questionService.getByID(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
