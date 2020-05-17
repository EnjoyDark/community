package life.majiang.community.community.controller;

import life.majiang.community.community.dto.PaginationDTO;
import life.majiang.community.community.model.User;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(name="page", defaultValue = "1")Integer page,
            @RequestParam(name="size", defaultValue = "2")Integer size ){

        User user = (User)request.getSession().getAttribute("user");
        PaginationDTO pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
