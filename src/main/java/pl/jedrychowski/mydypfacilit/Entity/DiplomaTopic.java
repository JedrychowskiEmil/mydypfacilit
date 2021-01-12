package pl.jedrychowski.mydypfacilit.Entity;

import javax.persistence.*;

@Entity
@Table(name = "diploma_topic")
public class DiplomaTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "promoter_id")
    private User promoter;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "status")
    private Status status;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "departmentId")
    private Department department;

    public DiplomaTopic() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getPromoter() {
        return promoter;
    }

    public void setPromoter(User promoter) {
        this.promoter = promoter;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "DiplomaTopic{" +
                "id=" + id +
                ", student=" + student.getFirstName() + " " + student.getLastName() +
                ", promoter=" + promoter.getFirstName() + " " + promoter.getLastName() +
                ", subject='" + subject + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
