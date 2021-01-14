package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.Service.DiplomaTopicService;
import pl.jedrychowski.mydypfacilit.Service.UserService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiplomaTopicService diplomaTopicService;


    @GetMapping("")
    public String students(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        model.addAttribute("loggedUser", user);

        //Get only diploma topics with assigned student
        List<DiplomaTopic> diplomaTopicsWithAssignedStudents =
                diplomaTopicService.splitDiplomaListStudentNullandNotNull(user.getPromoterTopic()).getSecond();

        //get only diploma that were accepted by excluding some statuses
        List<DiplomaTopic> diplomaTopicList = diplomaTopicService.splitDiplomaListByStatus(diplomaTopicsWithAssignedStudents,
                Arrays.asList(
                        "Nie wybrano tematu pracy",
                        "Brak promotora",
                        "Temat odrzucono",
                        "Zaproponowano temat",
                        "Temat promotora"
                )).getSecond();

        model.addAttribute("assignedDiplomaTopics", diplomaTopicService.wrapTopicListWithDepartment(diplomaTopicList));
        return "students";
    }

    @GetMapping("/acceptTopicMarkFinished")
    public String markTopicAsFinished(@RequestParam("id") Long id) {
        diplomaTopicService.changeDiplomaStatus(id, "Praca zatwierdzona przez promotora");
        return "redirect:/students";
    }

    @GetMapping("/needCorrection")
    public String markTopicAsneedCorrections(@RequestParam("id") Long id) {
        diplomaTopicService.changeDiplomaStatus(id, "Wymaga poprawy");
        return "redirect:/students";
    }

    @GetMapping("/resign")
    public String resignFromTopic(@RequestParam("id") Long id) {
        diplomaTopicService.resignFromTopic(id);
        return "redirect:/students";
    }
}
