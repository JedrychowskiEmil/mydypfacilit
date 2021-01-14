package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.Service.DiplomaTopicService;
import pl.jedrychowski.mydypfacilit.Service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mythesis")
public class diplomantController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiplomaTopicService diplomaTopicService;

    @GetMapping("")
    public String mythesis(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        model.addAttribute("loggedUser", user);

        DiplomaTopic diplomaTopic = user.getStudentTopic();
        if (diplomaTopic == null) diplomaTopic = new DiplomaTopic();
        model.addAttribute("diplomaTopic", diplomaTopic);

        List<User> promotersInThatDepartment;
        if (user.getDepartments().size() > 0l) {
            promotersInThatDepartment = userService.getPromotersByDepartmentId(user.getDepartments().get(0).getId());
        }else {
            promotersInThatDepartment = new ArrayList<>();
        }
        model.addAttribute("promoters", promotersInThatDepartment);

        return "mythesis";
    }

    @PostMapping("/save")
    public String saveMyDiplomatopic(@ModelAttribute DiplomaTopic diplomaTopic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);

        diplomaTopicService.saveStudentDiplomaTopic(diplomaTopic, user);

        return "redirect:/mythesis";
    }


    //TODO - no to ma dzialac
    @PostMapping("/apply")
    public String apply(@RequestParam("promoterId") Long promoterId,
                        @RequestParam("content") String content){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);

        diplomaTopicService.applyForPromoter(user.getStudentTopic(), promoterId, content);
        return "redirect:/mythesis";
    }

    @PostMapping("/undoapply")
    public String undoapply(@RequestParam("diplomaId") Long diplomaId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);

        diplomaTopicService.undoapply(diplomaId);
        return "redirect:/mythesis";
    }
}
