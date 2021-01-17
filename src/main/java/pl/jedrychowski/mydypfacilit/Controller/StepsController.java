package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.Service.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Controller
@RequestMapping("/steps")
public class StepsController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String steps(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User loggedUser = userService.getUserByemail(currentPrincipalEmail);
        model.addAttribute("leftPanelInfo", userService.getLeftPanelInformations(loggedUser));

        String content;
        try {
            File file = new File("");
            content = new Scanner(new File(file.getAbsolutePath()+"\\src\\main\\resources\\steps.txt")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            content = "Pliku nie odnaleziono";
        }
        model.addAttribute("content", content);
        return "steps";
    }
}
