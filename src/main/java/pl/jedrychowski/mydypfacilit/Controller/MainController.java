package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.News;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.Service.DiplomaTopicService;
import pl.jedrychowski.mydypfacilit.Service.UserService;

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

    //TODO ikona duza po lewej na kazdej karcie z newsem
    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        model.addAttribute("leftPanelInfo", userService.getLeftPanelInformations(user));

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
