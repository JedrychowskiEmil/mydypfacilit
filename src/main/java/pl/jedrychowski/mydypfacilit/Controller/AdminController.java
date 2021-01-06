package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.Department;
import pl.jedrychowski.mydypfacilit.ShowOption;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private DAOHibernate daoHibernate;

    @Autowired
    public AdminController(DAOHibernate daoHibernate) {
        this.daoHibernate = daoHibernate;
    }

    @GetMapping("")
    public String adminPanelHome(Model model){
        return "admintools";
    }


    @GetMapping("/students")
    public String adminStudents(Model model){
        return "adminStudents";
    }

    @GetMapping("/staff")
    public String adminStaff(Model model){
        return "adminStaff";
    }


    //TODO - sortowanie tak aby profesorowie pojawiali się pierwsi
    @GetMapping("/groups")
    public String adminGrupy(Model model){
        model.addAttribute("newDepartment", new Department());

        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);

        ShowOption showOption = new ShowOption(true, true);

        model.addAttribute("showOption", showOption);

        return "admingroups";
    }

    @PostMapping("groupSave")
    public String saveGroup(@ModelAttribute("department") Department department){
        daoHibernate.saveGroup(department);
        return "redirect:/admin/groups";
    }

    @PostMapping("groupOptionSwap")
    public String groupOptionSwap(@ModelAttribute("showOption") ShowOption showOption, Model model){
        model.addAttribute("newDepartment", new Department());

        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);


        model.addAttribute("showOption", showOption);

        return "admingroups";
    }

    @GetMapping("modifyGroup")
    public String modifyGroup(@RequestParam("id") Long id, Model model){
        Department department = daoHibernate.getDepartmentById(id);
        model.addAttribute("newDepartment", department);

        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);

        return "admingroups";
    }

    @GetMapping("deleteGroup")
    public String deleteGroup(@RequestParam("id") Long id){
        daoHibernate.deleteDepartmentById(id);
        return "redirect:/admin/groups";
    }

    @GetMapping("/news")
    public String adminNews(Model model){
        return "adminnews";
    }

    @GetMapping("/steps")
    public String adminSteps(Model model){
        return "adminsteps";
    }
}
