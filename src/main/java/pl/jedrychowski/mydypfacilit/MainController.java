package pl.jedrychowski.mydypfacilit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;

@Controller
public class MainController {

    DAOHibernate daoHibernate;
    @Autowired
    public MainController(DAOHibernate daoHibernate){
        this.daoHibernate = daoHibernate;
    }

    @GetMapping("/")
    public String slash(Model model){
        //daoHibernate.getabc();
        model.addAttribute("data","test123");
        return "helloworld";
    }

    @GetMapping("/admin/students")
    public String adminStudents(Model model){
        return "adminStudents";

    }

    @GetMapping("/admin/groups")
    public String adminGrupy(Model model){
        return "admingroups";

    }
}
