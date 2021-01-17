package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Controller
@RequestMapping("/steps")
public class StepsController {

    @GetMapping("")
    public String steps(Model model) {
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
