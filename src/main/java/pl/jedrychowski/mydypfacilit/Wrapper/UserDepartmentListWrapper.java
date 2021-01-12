package pl.jedrychowski.mydypfacilit.Wrapper;

import pl.jedrychowski.mydypfacilit.Entity.Department;
import pl.jedrychowski.mydypfacilit.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDepartmentListWrapper {
    private User user;
    private List<Long> departmentsId;

    public UserDepartmentListWrapper(User user, List<Long> departmentsId) {
        this.user = user;
        this.departmentsId = departmentsId;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Long> getDepartmentsId() {
        return departmentsId;
    }

    public void setDepartmentsId(List<Long> departmentsId) {
        this.departmentsId = departmentsId;
    }
}
