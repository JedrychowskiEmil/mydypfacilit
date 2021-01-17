package pl.jedrychowski.mydypfacilit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.Department;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;
import pl.jedrychowski.mydypfacilit.Entity.Status;
import pl.jedrychowski.mydypfacilit.Entity.User;
import pl.jedrychowski.mydypfacilit.Wrapper.DiplomaTopicDepartmentIdWrapper;
import pl.jedrychowski.mydypfacilit.Wrapper.DiplomaTopicListDepartmentWrapper;
import pl.jedrychowski.mydypfacilit.Wrapper.UserDiplomaTopicListWrapper;

import javax.mail.MessagingException;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiplomaTopicService {

    @Autowired
    DAOHibernate daoHibernate;

    @Autowired
    EmailService emailService;

    @Autowired
    SimpleMailMessage templatePromoterChangeDiplomaTopicStatus;

    public void saveDiplomaTopic(DiplomaTopicDepartmentIdWrapper diplomaTopicDepartmentIdWrapper) {

        //unwrap
        DiplomaTopic diplomaTopic = diplomaTopicDepartmentIdWrapper.getDiplomaTopic();
        Long departmentId = diplomaTopicDepartmentIdWrapper.getDepartmentId();

        //check if new
        if (diplomaTopic.getId() == 0) {
            Status status = daoHibernate.getStatusByName("Temat promotora");
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

    public void saveStudentDiplomaTopic(DiplomaTopic diplomaTopic, User user) {
        //new if 0
        if (diplomaTopic.getId() == 0) {
            diplomaTopic.setStudent(user);
            if (user.getDepartments().size() > 0) {
                diplomaTopic.setDepartment(user.getDepartments().get(0));
            }
            Status status = daoHibernate.getStatusByName("Brak promotora");
            diplomaTopic.setStatus(status);
            daoHibernate.saveOrUpdateDiplomaTopic(diplomaTopic);
        } else {
            DiplomaTopic oldDiplomaTopic = daoHibernate.getDiplomatopicById(diplomaTopic.getId());
            oldDiplomaTopic.setSubject(diplomaTopic.getSubject());
            oldDiplomaTopic.setDescription(diplomaTopic.getDescription());
            daoHibernate.saveOrUpdateDiplomaTopic(oldDiplomaTopic);
        }
    }


    public Pair<List<DiplomaTopicListDepartmentWrapper>, List<DiplomaTopicListDepartmentWrapper>> separateByStatus(List<DiplomaTopicListDepartmentWrapper> list, String statusName, String statusName2) {
        List<DiplomaTopic> withStatus;
        List<DiplomaTopic> withStatus2;
        Status status = daoHibernate.getStatusByName(statusName);
        Status status2 = daoHibernate.getStatusByName(statusName2);

        List<DiplomaTopicListDepartmentWrapper> withStatusWrappedList = new ArrayList<>();
        List<DiplomaTopicListDepartmentWrapper> withStatusWrappedList2 = new ArrayList<>();

        for (DiplomaTopicListDepartmentWrapper d : list) {
            for (DiplomaTopic t : d.getDiplomaTopics()) {
                withStatus = new ArrayList<>();
                withStatus2 = new ArrayList<>();
                if (t.getStatus().equals(status)) {
                    withStatus.add(t);
                } else if (t.getStatus().equals(status2)) {
                    withStatus2.add(t);
                }
                if (withStatus.size() > 0) {
                    withStatusWrappedList.add(new DiplomaTopicListDepartmentWrapper(withStatus, d.getDepartment()));
                }
                if (withStatus2.size() > 0) {
                    withStatusWrappedList2.add(new DiplomaTopicListDepartmentWrapper(withStatus2, d.getDepartment()));
                }
            }
        }
        return Pair.of(withStatusWrappedList, withStatusWrappedList2);
    }


    public List<DiplomaTopicListDepartmentWrapper> wrapTopicListWithDepartment(List<DiplomaTopic> topics) {
        Set<Department> departments = new HashSet<>();

        topics.forEach(t -> departments.add(t.getDepartment()));

        List<DiplomaTopicListDepartmentWrapper> diplomaTopicListDepartmentWrappers = new ArrayList<>();


        //for each department create new wrapper, look for topic with same department and wrap them all together
        for (Department d : departments) {
            DiplomaTopicListDepartmentWrapper diplomaTopicListDepartmentWrapper =
                    new DiplomaTopicListDepartmentWrapper(new ArrayList<>(), d);
            for (DiplomaTopic t : topics) {
                if (t.getDepartment().equals(d)) {
                    diplomaTopicListDepartmentWrapper.getDiplomaTopics().add(t);
                }
            }
            diplomaTopicListDepartmentWrappers.add(diplomaTopicListDepartmentWrapper);
        }
        return diplomaTopicListDepartmentWrappers;
    }


    public Pair<List<DiplomaTopic>, List<DiplomaTopic>> splitDiplomaListByStatus(List<DiplomaTopic> listToSplit, List<String> statusNames) {

        List<Status> statuses = new ArrayList<>(statusNames.size());
        statusNames.forEach(s -> statuses.add(daoHibernate.getStatusByName(s)));

        List<DiplomaTopic> with = listToSplit.stream().filter(diplomaTopic -> statuses.contains(diplomaTopic.getStatus())).collect(Collectors.toList());
        List<DiplomaTopic> without = listToSplit.stream().filter(diplomaTopic -> !statuses.contains(diplomaTopic.getStatus())).collect(Collectors.toList());

        return Pair.of(with, without);
    }

    public Pair<List<DiplomaTopic>, List<DiplomaTopic>> splitDiplomaListStudentNullandNotNull(List<DiplomaTopic> listToSplit) {
        List<DiplomaTopic> studentNull = listToSplit.stream().filter(diplomaTopic -> diplomaTopic.getStudent() == null).collect(Collectors.toList());
        List<DiplomaTopic> studentNotNull = listToSplit.stream().filter(diplomaTopic -> diplomaTopic.getStudent() != null).collect(Collectors.toList());

        return Pair.of(studentNull, studentNotNull);
    }


    public List<DiplomaTopic> getDiplomaTopicsByPromoterId(Long id) {
        return daoHibernate.getDiplomaTopicsByPromotorId(id);
    }

    //TODO - mail
    public void changeDiplomaStatus(Long diplomaId, String statusName) {
        DiplomaTopic diplomaTopic = daoHibernate.getDiplomatopicById(diplomaId);
        Status status = daoHibernate.getStatusByName(statusName);
        diplomaTopic.setStatus(status);
        daoHibernate.saveOrUpdateDiplomaTopic(diplomaTopic);
    }

    //TODO -mail
    public void refuseTopic(Long diplomaId) {
        DiplomaTopic diplomaTopic = daoHibernate.getDiplomatopicById(diplomaId);
        Status status = daoHibernate.getStatusByName("Zaproponowano temat");

        if (diplomaTopic.getStatus().equals(status)) {
            status = daoHibernate.getStatusByName("Temat odrzucono");
            diplomaTopic.setStatus(status);
            diplomaTopic.setPromoter(null);
            daoHibernate.saveOrUpdateDiplomaTopic(diplomaTopic);
        } else if (diplomaTopic.getStatus().equals(daoHibernate.getStatusByName("Temat promotora"))) {
            status = daoHibernate.getStatusByName("Temat odrzucono");
            DiplomaTopic newDiplomaTopic = new DiplomaTopic();
            newDiplomaTopic.setStudent(diplomaTopic.getStudent());
            newDiplomaTopic.setStatus(status);
            newDiplomaTopic.setSubject("Aplikacja o temat odrzucona | " + diplomaTopic.getSubject());
            newDiplomaTopic.setDescription(diplomaTopic.getPromoter().toString() + "Odrzucił twoją prośbę o uznanie tematu: \"" + diplomaTopic.getSubject() + "\"");
            daoHibernate.saveOrUpdateDiplomaTopic(newDiplomaTopic);

            diplomaTopic.setStudent(null);
            daoHibernate.saveOrUpdateDiplomaTopic(diplomaTopic);
        }

    }

    public void resignFromTopic(Long diplomaId) {
        DiplomaTopic diplomaTopic = daoHibernate.getDiplomatopicById(diplomaId);
        Status status = daoHibernate.getStatusByName("Brak promotora");
        diplomaTopic.setPromoter(null);
        diplomaTopic.setStatus(status);
        daoHibernate.saveOrUpdateDiplomaTopic(diplomaTopic);
    }

    public void deleteTopicById(Long id) {
        DiplomaTopic diplomaTopic = daoHibernate.getDiplomatopicById(id);
        daoHibernate.deleteDiplomatopic(diplomaTopic);
    }

    public DiplomaTopic getDiplomaTopicById(Long id) {
        if (id == null) {
            return new DiplomaTopic();
        } else {
            return daoHibernate.getDiplomatopicById(id);
        }
    }

    //TODO - mail
    public void applyForPromoter(DiplomaTopic studentTopic, Long promoterId, String content) {
        User user = daoHibernate.getUserById(promoterId);
        studentTopic.setPromoter(user);

        Status status = daoHibernate.getStatusByName("Zaproponowano temat");
        studentTopic.setStatus(status);
        daoHibernate.saveOrUpdateDiplomaTopic(studentTopic);
    }

    public void undoapply(Long diplomaId) {
        DiplomaTopic diplomaTopic = daoHibernate.getDiplomatopicById(diplomaId);
        Status status = daoHibernate.getStatusByName("Zaproponowano temat");

        //if it was users topic then change promotor to null and roll back previous status
        if (diplomaTopic.getStatus().equals(status)) {
            status = daoHibernate.getStatusByName("Brak promotora");
            diplomaTopic.setStatus(status);
            diplomaTopic.setPromoter(null);
            daoHibernate.saveOrUpdateDiplomaTopic(diplomaTopic);

            //if it was promoters topic then just set student field to null
        } else if (diplomaTopic.getStatus().equals(daoHibernate.getStatusByName("Temat promotora"))) {
            diplomaTopic.setStudent(null);
            daoHibernate.saveOrUpdateDiplomaTopic(diplomaTopic);
        }
    }

    public List<DiplomaTopic> getProposedTopicsForDepartmentId(long departmentID) {
        return daoHibernate.getDiplomatopicsByDepartmentId(departmentID);
    }

    public List<DiplomaTopic> filterDiplomaListByStatusName(List<DiplomaTopic> diplomaTopics, String statusName) {
        Status status = daoHibernate.getStatusByName(statusName);
        diplomaTopics.removeIf(t -> !t.getStatus().equals(status));
        return diplomaTopics;
    }

    public List<UserDiplomaTopicListWrapper> wrapPromoterWithHisDiplomaTopics(List<DiplomaTopic> diplomaTopics) {
        Set<User> promoters = new HashSet<>();
        diplomaTopics.forEach(t -> promoters.add(t.getPromoter()));

        List<UserDiplomaTopicListWrapper> userDiplomaTopicListWrappers = new ArrayList<>();
        for (User p : promoters) {
            UserDiplomaTopicListWrapper userDiplomaTopicListWrapper = new UserDiplomaTopicListWrapper(new ArrayList<>(), p);
            for (DiplomaTopic t : diplomaTopics) {
                if (t.getPromoter().equals(p)) {
                    userDiplomaTopicListWrapper.getDiplomaTopics().add(t);
                }
            }
            userDiplomaTopicListWrappers.add(userDiplomaTopicListWrapper);
        }
        return userDiplomaTopicListWrappers;
    }

    public void setDiplomaTopicStatusToNeedCorrection(Long id, String content, MultipartFile file) {
        changeDiplomaStatus(id, "Wymaga poprawy");

        DiplomaTopic diplomaTopic = daoHibernate.getDiplomatopicById(id);

        //send mail
        if (file.isEmpty()) {
            emailService.sendEmail(
                    templatePromoterChangeDiplomaTopicStatus.getFrom(),
                    diplomaTopic.getStudent().getEmail(),
                    templatePromoterChangeDiplomaTopicStatus.getSubject(),
                    String.format(templatePromoterChangeDiplomaTopicStatus.getText(), diplomaTopic.getStatus().getName(), "Wymaga poprawy", "")
            );
        } else {
            try {
                emailService.sendMessageWithAttachment(
                        templatePromoterChangeDiplomaTopicStatus.getFrom(),
                        diplomaTopic.getStudent().getEmail(),
                        templatePromoterChangeDiplomaTopicStatus.getSubject(),
                        String.format(templatePromoterChangeDiplomaTopicStatus.getText(), diplomaTopic.getStatus().getName(), "Wymaga poprawy", content),
                        file
                );
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}