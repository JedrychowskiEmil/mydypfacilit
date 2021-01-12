package pl.jedrychowski.mydypfacilit.Wrapper;

import pl.jedrychowski.mydypfacilit.Entity.Department;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;

public class DiplomaTopicDepartmentIdWrapper {
    private DiplomaTopic diplomaTopic;
    private Long departmentId;

    public DiplomaTopicDepartmentIdWrapper(DiplomaTopic diplomaTopic, Long departmentId) {
        this.diplomaTopic = diplomaTopic;
        this.departmentId = departmentId;
    }

    public DiplomaTopic getDiplomaTopic() {
        return diplomaTopic;
    }

    public void setDiplomaTopic(DiplomaTopic diplomaTopic) {
        this.diplomaTopic = diplomaTopic;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
