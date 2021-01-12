package pl.jedrychowski.mydypfacilit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.Department;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;
import pl.jedrychowski.mydypfacilit.Entity.Status;
import pl.jedrychowski.mydypfacilit.Wrapper.DiplomaTopicDepartmentIdWrapper;
import pl.jedrychowski.mydypfacilit.Wrapper.DiplomaTopicListDepartmentWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DiplomaTopicService {

    @Autowired
    DAOHibernate daoHibernate;

    public void saveDiplomaTopic(DiplomaTopicDepartmentIdWrapper diplomaTopicDepartmentIdWrapper) {

        //unwrap
        DiplomaTopic diplomaTopic = diplomaTopicDepartmentIdWrapper.getDiplomaTopic();
        Long departmentId = diplomaTopicDepartmentIdWrapper.getDepartmentId();

        //check if new
        if (diplomaTopic.getId() == 0) {
            Status status = daoHibernate.getStatusByName("Nie wybrano tematu pracy");
            diplomaTopic.setStatus(status);

            Department department = daoHibernate.getDepartmentById(departmentId);
            diplomaTopic.setDepartment(department);
            daoHibernate.saveOrUpdateDiplomaTopic(diplomaTopic);
        } else {
            DiplomaTopic oldDiplomaTopic = daoHibernate.getDiplomatopicById(diplomaTopic.getId());
            oldDiplomaTopic.setSubject(diplomaTopic.getSubject());
            oldDiplomaTopic.setDescription(diplomaTopic.getDescription());
            daoHibernate.saveOrUpdateDiplomaTopic(oldDiplomaTopic);
        }
    }

    public Pair<List<DiplomaTopicListDepartmentWrapper>, List<DiplomaTopicListDepartmentWrapper>> getDiplomaTopicListDepartmentWrapper(Long userId) {
        List<DiplomaTopic> diplomaTopics = daoHibernate.getDiplomaTopicsByPromotorId(userId);

        List<DiplomaTopic> promoterTopics = new ArrayList<>();
        List<DiplomaTopic> studentsList = new ArrayList<>();

        //split into topics created by promoter and by student
        //if field student is not null that means topic was created by student
        diplomaTopics.forEach(t -> {
            if (t.getStudent() == null) {
                promoterTopics.add(t);
            } else {
                studentsList.add(t);
            }
        });

        List<DiplomaTopicListDepartmentWrapper> promoterTopicsWrapped = wrapTopicListWithDepartment(promoterTopics);
        List<DiplomaTopicListDepartmentWrapper> studentsTopicWrapped = wrapTopicListWithDepartment(studentsList);


        return Pair.of(promoterTopicsWrapped, studentsTopicWrapped);
    }

    private List<DiplomaTopicListDepartmentWrapper> wrapTopicListWithDepartment(List<DiplomaTopic> topics) {
        Set<Department> departments = new HashSet<>();

        topics.forEach(t -> departments.add(t.getDepartment()));

        List<DiplomaTopicListDepartmentWrapper> diplomaTopicListDepartmentWrappers = new ArrayList<>();


        //for each department create new wrapper, look for topic with same department and wrap them all together
        for (Department d : departments) {
            DiplomaTopicListDepartmentWrapper diplomaTopicListDepartmentWrapper =
                    new DiplomaTopicListDepartmentWrapper(new ArrayList<>(), d);
            for (DiplomaTopic t : topics) {
                if(t.getDepartment().equals(d)){
                    diplomaTopicListDepartmentWrapper.getDiplomaTopics().add(t);
                }
            }
            diplomaTopicListDepartmentWrappers.add(diplomaTopicListDepartmentWrapper);
        }
        return diplomaTopicListDepartmentWrappers;
    }
}
