package pl.jedrychowski.mydypfacilit;

import pl.jedrychowski.mydypfacilit.Entity.User;

public class UserDepartmentWrapper {
    private User user;
    private Long departmentId;

    public UserDepartmentWrapper() {
    }

    public UserDepartmentWrapper(User user, Long departmentId) {
        this.user = user;
        this.departmentId = departmentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
