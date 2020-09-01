package com.majiang.community.controller;

import com.majiang.community.dto.CommentDTO;
import com.majiang.community.dto.ResultDTO;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.model.Comment;
import com.majiang.community.model.User;
import com.majiang.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

//jason的方式来做，可以理解为网络传输的数据结构，使用这种方式来传输前端信息，
//处理后自动转换格式返还到前端。服务端和前端后都使用这种格式。
//通过request接受前端信息，反序列化成java对象，处理后，通过responseBody再自动处理成json对象返还到前端
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public  Object post(@RequestBody CommentDTO commentDTO,
                        HttpServletRequest request){
//       判断登录态
        User user =(User)request.getSession().getAttribute("user");
        if(user ==null){
            return ResultDTO.errorof(CustomizeErrorCode.NO_LOGIN);
        }
//        set前端返还的值
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(1L);
        comment.setLikeCount(0L);
        commentService.insert(comment);
//        Map<Object,Object> objectObjectHashMap =new HashMap<>();
//        objectObjectHashMap.put("message","成功");
        return ResultDTO.okof();
    }
}
