package pl.jedrychowski.mydypfacilit.Entity;

import javassist.compiler.CompileError;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "user")
public class User implements Comparable<User>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "academic_title")
    private String academicTitle;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private DiplomaTopic studentTopic;

    @OneToMany(mappedBy = "promoter", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<DiplomaTopic> promoterTopic;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "department_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    private List<Department> departments;


    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public DiplomaTopic getStudentTopic() {
        return studentTopic;
    }

    public void setStudentTopic(DiplomaTopic studentTopic) {
        this.studentTopic = studentTopic;
    }

    public List<DiplomaTopic> getPromoterTopic() {
        return promoterTopic;
    }

    public void setPromoterTopic(List<DiplomaTopic> promoterTopic) {
        this.promoterTopic = promoterTopic;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(academicTitle != null && academicTitle.length() > 0){
            stringBuilder.append(academicTitle).append(" ");
        }
        stringBuilder.append(firstName).append(" ").append(lastName);

        return stringBuilder.toString();
    }

    @Override
    public int compareTo(User o) {
        return Comparator.comparing((User o1) -> o1.getFirstName().toUpperCase())
                .thenComparing((User o1) -> o1.getLastName().toUpperCase())
                .compare(this, o);
    }

    public int compareToWithRole(User o){
        return Comparator.comparing((User o1) -> o1.getRoles().get(0).getName().toUpperCase()).thenComparing(this::compareTo).compare(this, o);
    }
}
