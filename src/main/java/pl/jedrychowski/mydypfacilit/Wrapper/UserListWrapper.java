package pl.jedrychowski.mydypfacilit.Wrapper;

import pl.jedrychowski.mydypfacilit.Entity.Department;
import pl.jedrychowski.mydypfacilit.Entity.User;

import java.util.Comparator;
import java.util.List;

public class UserListWrapper implements Comparable<UserListWrapper>{
    List<User> users;
    Department department;

    public UserListWrapper(List<User> users, Department department) {
        this.users = users;
        this.department = department;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public int compareTo(UserListWrapper o) {
        return Comparator.comparing((UserListWrapper o1) -> o1.getDepartment().getDepartment())
                .thenComparing((UserListWrapper o1) -> o1.getDepartment().getField())
                .thenComparing((UserListWrapper o1) -> o1.getDepartment().getStudyMode())
                .thenComparing((UserListWrapper o1) -> o1.getDepartment().getStudyGroup())
                .compare(this, o);
    }
}
