package com.majiang.community.service;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.mapper.QuestionMapper;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.Question;
import com.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

//业务层调用持久层，取得数据，分别从两个表里取得全部论文数据，拿到所有数据返回.
    public PaginationDTO list(Integer page, Integer size) {

        Integer totalCount = questionMapper.count();
        Integer totalPage;
//        求得总页数并进行越界处理
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
//        页面对象参数设置，逻辑处理。
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(totalPage,page);
//        求得当前页码
        Integer offset =size*(page-1);
        List<Question> questions =  questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
//遍历取得数据
        for(Question question:questions){
            User user=userMapper.findById(question.getCreator());
//          把所有的list对象放到DTO对象中。返回的是DTO的一个list对象
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
//根据id取得个人数据
    public PaginationDTO list(Integer id, Integer page, Integer size) {

        Integer totalPage;
//        获得个人的论文数
        Integer totalCount = questionMapper.countById(id);
//        求得总页数并进行越界处理
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1; }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
//        页面对象参数设置，逻辑处理。
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(totalPage,page);
//        求得当前页码
        Integer offset =size*(page-1);
        List<Question> questions =  questionMapper.listById(id,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
//遍历取得数据，根据问题的创建者，与创建者id关联.
        for(Question question:questions){
            User user=userMapper.findById(question.getCreator());
//          把所有的list对象放到DTO对象中。返回的是DTO的一个list对象
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
//        paginationDTO带着页面的信息list及页码信息返回前端
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;

    }
//根据id获取当前问题
    public QuestionDTO getByID(Integer id) {
        Question question = questionMapper.getByID(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
