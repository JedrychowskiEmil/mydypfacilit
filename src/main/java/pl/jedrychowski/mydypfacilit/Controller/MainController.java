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
    public String login() {
        return "login";
    }

    @GetMapping("/students")
    public String students(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        model.addAttribute("loggedUser", user);

        return "students";
    }

    @GetMapping("/topics")
    public String topics(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        model.addAttribute("loggedUser", user);

        DiplomaTopicDepartmentIdWrapper diplomaTopicDepartmentWrapper = new DiplomaTopicDepartmentIdWrapper(new DiplomaTopic(), 0L);
        model.addAttribute("topic", diplomaTopicDepartmentWrapper);

        Pair<List<DiplomaTopicListDepartmentWrapper>, List<DiplomaTopicListDepartmentWrapper>> promotersTopicStudentsTopics =
         diplomaTopicService.getDiplomaTopicListDepartmentWrapper(user.getId());

        model.addAttribute("promoterTopics", promotersTopicStudentsTopics.getFirst());
        model.addAttribute("studentsTopics", promotersTopicStudentsTopics.getSecond());


        return "topics";
    }

    //TODO - przerzucic do serwisu dodanie usera
    @PostMapping("/topic/save")
    public String savvetopic(@ModelAttribute DiplomaTopicDepartmentIdWrapper diplomaTopicDepartmentIdWrapper,
                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        diplomaTopicDepartmentIdWrapper.getDiplomaTopic().setPromoter(user);
        diplomaTopicService.saveDiplomaTopic(diplomaTopicDepartmentIdWrapper);
        return "redirect:/topics";
    }
}
