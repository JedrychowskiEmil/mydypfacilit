package pl.jedrychowski.mydypfacilit.Wrapper;

import pl.jedrychowski.mydypfacilit.Entity.User;

import java.util.Comparator;
import java.util.List;

public class PromoterStudentListWrapper implements Comparable<PromoterStudentListWrapper>{
    User promoter;
    List<User> students;

    public PromoterStudentListWrapper(User promoter, List<User> students) {
        this.promoter = promoter;
        this.students = students;
    }

    public User getPromoter() {
        return promoter;
    }

    public void setPromoter(User promoter) {
        this.promoter = promoter;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    @Override
    public int compareTo(PromoterStudentListWrapper o) {

        return Comparator.comparing((PromoterStudentListWrapper p) -> p.promoter.getFirstName().toUpperCase())
                .thenComparing((PromoterStudentListWrapper p) -> p.promoter.getLastName().toUpperCase()).compare(this, o);
    }
}
