package pl.jedrychowski.mydypfacilit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;

import java.io.*;
import java.util.Scanner;

@Controller
public class MainController {

    private DAOHibernate daoHibernate;
    @Autowired
    public MainController(DAOHibernate daoHibernate){
        this.daoHibernate = daoHibernate;
    }

    @GetMapping("/")
    public String slash(Model model) throws IOException {
        return "admintools";
    }

}
