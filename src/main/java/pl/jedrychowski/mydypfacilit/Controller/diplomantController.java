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

        if (user.getStudentTopic() == null) {
            model.addAttribute("diplomaTopic", new DiplomaTopic());
            return "mythesisnotopic";
        }
        switch (user.getStudentTopic().getStatus().getName()) {
            case "Brak promotora":
            case "Temat odrzucono":
                DiplomaTopic diplomaTopic = user.getStudentTopic();
                model.addAttribute("diplomaTopic", diplomaTopic);
                return "mythesisbeforeapply";


            case "Zaproponowano temat":
            case "Temat promotora":
                return "mythesisapplied";

            case "W trakcie pisania pracy":
            case "Wymaga poprawy":
                return "mythesisinmiddleofwork";

            case "Praca zatwierdzona przez promotora":
            case "Praca przyjeta i zatwierdzona":
                return "mythesisaccepted";

            default:
                return "error-invalid-status-state";
        }

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
                        @RequestParam("content") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);

        diplomaTopicService.applyForPromoter(user.getStudentTopic(), promoterId, content);
        return "redirect:/mythesis";
    }

    @PostMapping("/undoapply")
    public String undoapply(@RequestParam("diplomaId") Long diplomaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);

        diplomaTopicService.undoapply(diplomaId);
        return "redirect:/mythesis";
    }
}