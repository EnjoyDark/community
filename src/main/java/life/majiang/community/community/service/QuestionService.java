package life.majiang.community.community.service;

import life.majiang.community.community.dto.PaginationDTO;
import life.majiang.community.community.dto.QuestionDTO;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.Question;
import life.majiang.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        // 完成分页数据填充
        Integer totalPage;  // 总页数
        Integer totalCount = questionMapper.count();  // 总数量
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) { page = 1; }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        // 数据库查询，填充question、user数据
        // 5*(1-1)   limit 0, 5
        Integer offset = size * (page - 1);
        ArrayList<Question> questions = questionMapper.list(offset, size);
        ArrayList<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        // 将paginationDTO的属性值全赋好后，return
        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        // 完成分页数据填充
        Integer totalPage;  // 总页数
        Integer totalCount = questionMapper.countByUserId(userId);  // 总数量
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) { page = 1; }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        // 数据库查询，填充question、user数据
        // 5*(1-1)   limit 0, 5
        Integer offset = size * (page - 1);
        ArrayList<Question> questions = questionMapper.listByUserId(userId, offset, size);
        ArrayList<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        // 将paginationDTO的属性值全赋好后，return
        return paginationDTO;
    }
}
