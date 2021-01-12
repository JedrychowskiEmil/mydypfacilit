package pl.jedrychowski.mydypfacilit.Wrapper;

import pl.jedrychowski.mydypfacilit.Entity.Department;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;

import java.util.List;

public class DiplomaTopicListDepartmentWrapper {
    private List<DiplomaTopic> diplomaTopics;
    private Department department;

    public DiplomaTopicListDepartmentWrapper(List<DiplomaTopic> diplomaTopics, Department departmentName) {
        this.diplomaTopics = diplomaTopics;
        this.department = departmentName;
    }

    public List<DiplomaTopic> getDiplomaTopics() {
        return diplomaTopics;
    }

    public void setDiplomaTopics(List<DiplomaTopic> diplomaTopics) {
        this.diplomaTopics = diplomaTopics;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
