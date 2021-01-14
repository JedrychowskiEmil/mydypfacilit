package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;
import pl.jedrychowski.mydypfacilit.Entity.News;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.Service.DiplomaTopicService;
import pl.jedrychowski.mydypfacilit.Service.UserService;
import pl.jedrychowski.mydypfacilit.Wrapper.DiplomaTopicDepartmentIdWrapper;
import pl.jedrychowski.mydypfacilit.Wrapper.DiplomaTopicListDepartmentWrapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private DAOHibernate daoHibernate;

    @Autowired
    private UserService userService;

    @Autowired
    private DiplomaTopicService diplomaTopicService;

    //TODO ikona duza po lewej na kazdej karcie
    @GetMapping("/")
    public String home(Model model) {
        List<News> news = daoHibernate.getNews();
        news.sort(Comparator.comparing(News::getDate, Comparator.reverseOrder()));
        model.addAttribute("allNews", news);

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
