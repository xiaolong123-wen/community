package com.majiang.community.service;

import com.majiang.community.dto.PageinationDTO;
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

//业务层调用持久层，取得数据，分别从两个表里取得个人数据，拿到所有数据返回.
    public PageinationDTO list(Integer page, Integer size) {

        PageinationDTO pageinationDTO = new PageinationDTO();
        Integer totalCount = questionMapper.count();
        pageinationDTO.setPaginaton(totalCount,page,size);

        if (page <1){
            page = 1 ;
        }
        if(page >pageinationDTO.getTotalPage()){
            page = pageinationDTO.getTotalPage();
        }

//        求得当前页码
        Integer offset =size*(page-1);
        List<Question> questions =  questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question:questions){
            User user=userMapper.findById(question.getCreator());
//          把所有的list对象放到DTO对象中。返回的是DTO的一个list对象
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pageinationDTO.setQuestions(questionDTOList);
        return pageinationDTO;
    }
}
