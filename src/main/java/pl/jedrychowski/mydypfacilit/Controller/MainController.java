package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.News;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.UserDepartmentListWrapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private DAOHibernate daoHibernate;

    public MainController(DAOHibernate daoHibernate) {
        this.daoHibernate = daoHibernate;
    }

    //TODO ikona duza po lewej na kazdej karcie
    @GetMapping("/")
    public String home(Model model) {
        List<News> news = daoHibernate.getNews();
        news.sort(Comparator.comparing(News::getDate, Comparator.reverseOrder()));
        model.addAttribute("allNews", news);

        return "home";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        User user = daoHibernate.getUserById(3L);
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping("/settings/report")
    public String report(@RequestParam("userId") Long userId,
                         @RequestParam("content") String content,
                         Model model) {

        System.out.println(userId);
        System.out.println(content);
        return "redirect:/settings";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
