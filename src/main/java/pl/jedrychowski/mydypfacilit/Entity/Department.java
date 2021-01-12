package pl.jedrychowski.mydypfacilit.Entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "department")
    private String department;

    @Column(name = "field")
    private String field;

    @Column(name = "study_mode")
    private String studyMode;

    @Column(name = "study_group")
    private String studyGroup;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "department_user",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    public Department(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStudyMode() {
        return studyMode;
    }

    public void setStudyMode(String studyMode) {
        this.studyMode = studyMode;
    }

    public String getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(String studyGroup) {
        this.studyGroup = studyGroup;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(department);

        if(!field.isEmpty() && !field.isBlank()){
            stringBuilder.append(", ").append(field);
        }

        if(!studyMode.isEmpty() && !studyMode.isBlank()){
            stringBuilder.append(", ").append(studyMode);
        }

        if(!studyGroup.isEmpty() && !studyGroup.isBlank()){
            stringBuilder.append(", ").append(studyGroup);
        }
        return stringBuilder.toString();
    }
}
