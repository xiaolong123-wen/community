package com.majiang.community.service;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.exception.CustomizeException;
import com.majiang.community.mapper.QuestionExtMapper;
import com.majiang.community.mapper.QuestionMapper;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.Question;
import com.majiang.community.model.QuestionExample;
import com.majiang.community.model.User;
import org.apache.ibatis.session.RowBounds;
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

    @Autowired
    private QuestionExtMapper questionExtMapper;

//业务层调用持久层，取得数据，分别从两个表里取得全部论文数据，拿到所有数据返回.
    public PaginationDTO list(Integer page, Integer size) {

        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
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
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();
//遍历取得数据
        for(Question question:questions){
            User user=userMapper.selectByPrimaryKey(question.getCreator());
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
    public PaginationDTO list(Long id, Integer page, Integer size) {

        Integer totalPage;
//        获得个人的论文数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
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

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(id);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();
//遍历取得数据，根据问题的创建者，与创建者id关联.
        for(Question question:questions){
            User user=userMapper.selectByPrimaryKey(question.getCreator());
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
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
}
//根据问题id查找问题,实现添加`更新
    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        } else {
            // 更新
            Question dbQuestion = questionMapper.selectByPrimaryKey(question.getId());
            if (dbQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            if (dbQuestion.getCreator().longValue() != question.getCreator().longValue()) {
                throw new CustomizeException(CustomizeErrorCode.INVALID_OPERATION);
            }
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
// 浏览数的统计，通过问题id,增加浏览数
    public void incView(Long id) {
        Question question = new Question();
//        这里直接在sql语句中实现了累加，防止多个人访问的错。
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}
