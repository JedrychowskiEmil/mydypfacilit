package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.jedrychowski.mydypfacilit.*;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.*;
import pl.jedrychowski.mydypfacilit.Service.NewsService;
import pl.jedrychowski.mydypfacilit.Service.UserService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//TODO dodac limit znakow w formularzach i pod nimi diva https://da-software.net/en/2020/01/limit-the-number-of-characters-in-the-text-field-of-an-html-form/
//TODO email wprowadzany ma byc malymi, imie naziwso zaczynac z duzych awsze
@Controller
@RequestMapping("/admin")
public class AdminController {

    private DAOHibernate daoHibernate;
    private UserService userService;
    private NewsService newsService;

    @Autowired
    public AdminController(DAOHibernate daoHibernate, UserService userService, NewsService newsService) {
        this.daoHibernate = daoHibernate;
        this.userService = userService;
        this.newsService = newsService;
    }

    @GetMapping("")
    public String adminPanelHome(Model model) {
        return "admintools";
    }


    //#################################### STUDENTS ####################################

    @GetMapping("/students")
    public String adminStudents(@RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "filterDepartmentId", required = false) Long filterDepartmentId,
                                @RequestParam(value = "filterStatusId", required = false) Long filterStatusId,
                                Model model) {
        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);

        //Get users with id and status and role
        List<UserListWrapper> departmentWrapper = userService.getUserListWrapperList(filterDepartmentId, filterStatusId, "ROLE_STUDENT");
        model.addAttribute("departmentsOnlyStudents", departmentWrapper);

        List<Status> statuses = daoHibernate.getStatuses();
        model.addAttribute("statuses", statuses);

        if (id == null) {
            model.addAttribute("newUser", new UserDepartmentWrapper());
        } else {
            User user = daoHibernate.getUserById(id);
            Long departmentId = user.getDepartments().size() > 0 ? user.getDepartments().get(0).getId() : null;
            model.addAttribute("newUser", new UserDepartmentWrapper(user, departmentId));
        }

        model.addAttribute("filterUsers", new FilterUsers(filterDepartmentId, filterStatusId));


        return "adminStudents";
    }


    //TODO - wyprowadzic skrypt z templatek
    @PostMapping("students/save")
    public String saveStudent(@ModelAttribute UserDepartmentWrapper user,
                              @ModelAttribute FilterUsers filterUsers,
                              RedirectAttributes redirectAttributes) {

        userService.createOrUpdateUser(user, "ROLE_STUDENT");
        redirectAttributes.addAttribute("filterDepartmentId", filterUsers.getFilterDepartmentId());
        redirectAttributes.addAttribute("filterStatusId", filterUsers.getFilterStatusId());
        return "redirect:/admin/students";
    }

    @GetMapping("deleteUser")
    public String deleteUser(@RequestParam("id") Long id,
                             @RequestParam(value = "filterDepartmentId", required = false) Long filterDepartmentId,
                             @RequestParam(value = "filterStatusId", required = false) Long filterStatusId,
                             RedirectAttributes redirectAttributes) {
        userService.deleteUserById(id, "ROLE_STUDENT");
        redirectAttributes.addAttribute("filterDepartmentId", filterDepartmentId);
        redirectAttributes.addAttribute("filterStatusId", filterStatusId);
        return "redirect:/admin/students";
    }

    @GetMapping("student/setstatus")
    public String setStatus(@RequestParam("id") Long id,
                            @RequestParam(value = "filterDepartmentId", required = false) Long filterDepartmentId,
                            @RequestParam(value = "filterStatusId", required = false) Long filterStatusId,
                            @RequestParam(value = "setstatusto", required = false) String setstatusto,
                            @RequestParam(value = "newStatusName", required = false) String newStatusName,
                            RedirectAttributes redirectAttributes) {

        userService.setCustomStatus(id, setstatusto, newStatusName);
        redirectAttributes.addAttribute("filterDepartmentId", filterDepartmentId);
        redirectAttributes.addAttribute("filterStatusId", filterStatusId);

        return "redirect:/admin/students";
    }

    //#################################### STAFF ####################################

    @GetMapping("/staff")
    public String adminStaff(@RequestParam(value = "id", required = false) Long id,
                             @RequestParam(value = "show", required = false) boolean showStudents,
                             Model model) {

        //fill form
        User user;
        List<Long> departmentsId;
        if (id == null) {
            user = new User();
            departmentsId = new ArrayList<>();
        } else {
            user = daoHibernate.getUserById(id);
            departmentsId = new ArrayList<>();
            for (Department d : user.getDepartments()) {
                departmentsId.add(d.getId());
            }
        }
        model.addAttribute("newUser", new UserDepartmentListWrapper(user, departmentsId));

        //fill form multi select
        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);

        //add users
        List<PromoterStudentListWrapper> promoters = userService.getPromoterStudentLListWrapperList();
        model.addAttribute("promoters", promoters);

        //set option show students
        model.addAttribute("showStudents", new BooleanWrapper(showStudents));
        return "adminStaff";
    }


    @PostMapping("staff/save")
    public String saveStaff(@ModelAttribute UserDepartmentListWrapper user,
                            @ModelAttribute BooleanWrapper showUsers,
                            RedirectAttributes redirectAttributes) {

        userService.createOrUpdateUser(user, "ROLE_PROMOTER");
        redirectAttributes.addAttribute("show", showUsers.isShow());
        return "redirect:/admin/staff";
    }

    @GetMapping("staff/delete")
    public String deleteStaff(@RequestParam("id") Long id,
                              @RequestParam(value = "show", required = false) boolean showStudents,
                              RedirectAttributes redirectAttributes) {
        userService.deleteUserById(id, "ROLE_PROMOTER");
        redirectAttributes.addAttribute("show", showStudents);
        return "redirect:/admin/staff";
    }


    //#################################### GROUPS ####################################
    //TODO - sortowanie tak aby profesorowie pojawiali się pierwsi
    //TODO - zamienić requesty na POSTY zmieniajac hrefy w button z ukrytymi inputami
    @GetMapping("/groups")
    public String adminGrupy(Model model) {
        model.addAttribute("newDepartment", new Department());

        List<Department> departments = daoHibernate.getDepartments();
        departments.forEach(d -> d.getUsers().sort(User::compareToWithRole));
        model.addAttribute("departments", departments);

        ShowOption showOption = new ShowOption(true, true);

        model.addAttribute("showOption", showOption);

        return "admingroups";
    }

    @PostMapping("groupSave")
    public String saveGroup(@ModelAttribute("department") Department department,
                            @ModelAttribute("showOption") ShowOption showOption,
                            Model model) {
        daoHibernate.saveDepartment(department);

        model.addAttribute("newDepartment", new Department());

        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);

        model.addAttribute("showOption", showOption);

        return "admingroups";
    }

    @PostMapping("groupOptionSwap")
    public String groupOptionSwap(@ModelAttribute("showOption") ShowOption showOption, Model model) {
        model.addAttribute("newDepartment", new Department());

        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);


        model.addAttribute("showOption", showOption);

        return "admingroups";
    }

    @GetMapping("modifyGroup")
    public String modifyGroup(@RequestParam("id") Long id,
                              @RequestParam("showStaff") boolean showStaff,
                              @RequestParam("showStudents") boolean showStudents,
                              Model model) {
        Department department = daoHibernate.getDepartmentById(id);
        model.addAttribute("newDepartment", department);

        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);

        model.addAttribute("showOption", new ShowOption(showStaff, showStudents));

        return "admingroups";
    }

    @GetMapping("deleteGroup")
    public String deleteGroup(@RequestParam("id") Long id,
                              @RequestParam("showStaff") boolean showStaff,
                              @RequestParam("showStudents") boolean showStudents,
                              Model model) {
        daoHibernate.deleteDepartmentById(id);

        model.addAttribute("newDepartment", new Department());

        List<Department> departments = daoHibernate.getDepartments();
        model.addAttribute("departments", departments);

        model.addAttribute("showOption", new ShowOption(showStaff, showStudents));

        return "admingroups";
    }

    //#################################### NEWS ####################################
    //TODO - zwiekszyc limit znakow do 2k w content
    @GetMapping("/news")
    public String adminNews(@RequestParam(value = "id", required = false) Long id,
                            Model model) {

        //get all news and add to model
        List<News> news = daoHibernate.getNews();
        news.sort(Comparator.comparing(News::getDate, Comparator.reverseOrder()));
        model.addAttribute("cards", news);


        //If is new news add empty object if net the get object by id from database
        News newNews;
        if (id == null) {
            newNews = new News();
        } else {
            newNews = daoHibernate.getNewsById(id);
        }

        model.addAttribute("newNews", newNews);
        return "adminnews";
    }

    @PostMapping("news/save")
    public String saveNews(@ModelAttribute News news) {
        newsService.saveOrUpdateNews(news);
        return "redirect:/admin/news";
    }

    @GetMapping("news/delete")
    public String deleteNews(@RequestParam("id") Long id) {
        newsService.deleteNews(id);
        return "redirect:/admin/news";
    }


    //#################################### STEPS ####################################
    @GetMapping("/steps")
    public String adminSteps(Model model) {

        File file = new File("C:\\Users\\jedry\\Downloads\\mydypfacilit (1)\\mydypfacilit\\src\\main\\resources\\steps.txt");
        String content = "";
        try {
            if(file.length() != 0) {
                content = new Scanner(file).useDelimiter("\\Z").next();
            }
            model.addAttribute("content", content);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return "adminsteps";
    }

    @PostMapping("/steps/save")
    public String saveSteps(@RequestParam String content, Model model){
        System.out.println(content);
        File file = new File("C:\\Users\\jedry\\Downloads\\mydypfacilit (1)\\mydypfacilit\\src\\main\\resources\\steps.txt");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/admin/steps";
    }

}
