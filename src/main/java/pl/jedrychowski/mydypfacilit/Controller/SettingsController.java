package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.Service.UserService;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String settings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        model.addAttribute("loggedUser", user);
        return "settings";
    }

    //ma wysylac maila na adres admina
    //informacja zwrotna
    @PostMapping("/report")
    public String report(@RequestParam("userId") Long userId,
                         @RequestParam("content") String content,
                         Model model) {

        System.out.println(userId);
        System.out.println(content);
        return "redirect:/settings";
    }

    //TODO zwrot errora jak hasla zle, mail po zmianie hasla
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("password") String password,
                                 @RequestParam("password2") String password2,
                                 Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);

        if(userService.changePassword(user, password, password2)){
            return "redirect:/settings";
        }else{
            System.out.println("hasla nie takie same");
        }
        System.out.println(user.getFirstName());
        System.out.println(password);
        System.out.println(password2);
        return "redirect:/settings";
    }
}
