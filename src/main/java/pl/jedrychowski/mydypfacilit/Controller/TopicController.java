package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;
import pl.jedrychowski.mydypfacilit.Entity.Role;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.Service.DiplomaTopicService;
import pl.jedrychowski.mydypfacilit.Service.UserService;
import pl.jedrychowski.mydypfacilit.Wrapper.DiplomaTopicDepartmentIdWrapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiplomaTopicService diplomaTopicService;

    //TODO - feedback
    @GetMapping("")
    public String topics(@RequestParam(value = "id", required = false) Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        model.addAttribute("loggedUser", user);
        model.addAttribute("leftPanelInfo", userService.getLeftPanelInformations(user));

        Role roleStudent = userService.getRoleByName("ROLE_STUDENT");
        Role rolePromoter = userService.getRoleByName("ROLE_PROMOTER");

        if (user.getRoles().contains(rolePromoter)) {
            //fetch obj to modify or add empty
            DiplomaTopicDepartmentIdWrapper diplomaTopicDepartmentWrapper;
            DiplomaTopic diplomaTopic = diplomaTopicService.getDiplomaTopicById(id);
            diplomaTopicDepartmentWrapper = new DiplomaTopicDepartmentIdWrapper(diplomaTopic, diplomaTopic == null ? 0 : diplomaTopic.getId());
            model.addAttribute("topic", diplomaTopicDepartmentWrapper);


            //split promoter's diploma topics into the one with student field null the the ones that are not
            Pair<List<DiplomaTopic>, List<DiplomaTopic>> studentNullStudentNotNull =
                    diplomaTopicService.splitDiplomaListStudentNullandNotNull(user.getPromoterTopic());
            model.addAttribute("promoterTopics", diplomaTopicService.wrapTopicListWithDepartment(studentNullStudentNotNull.getFirst()));


            //split topics proposed by students into topics created by promoter and by student
            Pair<List<DiplomaTopic>, List<DiplomaTopic>> withStatusWithoutStatus =
                    diplomaTopicService.splitDiplomaListByStatus(studentNullStudentNotNull.getSecond(),
                            Arrays.asList(
                                    "Zaproponowano temat",
                                    "Temat promotora"
                            ));
            withStatusWithoutStatus = diplomaTopicService.splitDiplomaListByStatus(withStatusWithoutStatus.getFirst(), Collections.singletonList("Temat promotora"));

            model.addAttribute("topicsTakenByStudents", diplomaTopicService.wrapTopicListWithDepartment(withStatusWithoutStatus.getFirst()));
            model.addAttribute("topicsProposedByStudent", diplomaTopicService.wrapTopicListWithDepartment(withStatusWithoutStatus.getSecond()));
            return "topics";
        }

        if (user.getRoles().contains(roleStudent)) {
            ;
            //get list of diploma topics in that department
             List<DiplomaTopic> diplomaTopics = diplomaTopicService.getProposedTopicsForDepartmentId(user.getDepartments().get(0).getId());

             //get only the one created by promoter
            diplomaTopics = diplomaTopicService.filterDiplomaListByStatusName(diplomaTopics, "Temat promotora");

            //wrap promoters with diploma topics they created
            model.addAttribute("topicsProposedByPromoters",diplomaTopicService.wrapPromoterWithHisDiplomaTopics(diplomaTopics));


            return "topicsforstudent";
        }

        return "error-no-auth-to-view";

    }

    //TODO - przerzucic do serwisu dodanie usera
    @PostMapping("/save")
    public String savvetopic(@ModelAttribute DiplomaTopicDepartmentIdWrapper diplomaTopicDepartmentIdWrapper,
                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        User user = userService.getUserByemail(currentPrincipalEmail);
        diplomaTopicDepartmentIdWrapper.getDiplomaTopic().setPromoter(user);
        diplomaTopicService.saveDiplomaTopic(diplomaTopicDepartmentIdWrapper);
        return "redirect:/topics";
    }

    //TODO - mail, feedback
    @GetMapping("/acceptTopic")
    public String acceptTopic(@RequestParam("id") Long id) {
        diplomaTopicService.changeDiplomaStatus(id, "W trakcie pisania pracy");
        return "redirect:/topics";
    }

    //TODO - mail z odrzuceniem tresc
    @GetMapping("/refuseTopic")
    public String refuseTopic(@RequestParam("id") Long id) {
        diplomaTopicService.refuseTopic(id);
        return "redirect:/topics";
    }

    //TODO feedback
    @GetMapping("/deleteTopic")
    public String deleteTopic(@RequestParam("id") Long id) {
        diplomaTopicService.deleteTopicById(id);
        return "redirect:/topics";
    }
}
