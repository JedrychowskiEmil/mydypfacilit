package pl.jedrychowski.mydypfacilit.DAO;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.jedrychowski.mydypfacilit.Entity.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

//TODO - poprawic bezpieczenstwo nulli
@Repository
public class DAOHibernate {
    private EntityManager entityManager;

    @Autowired
    public DAOHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public User getUserById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(User.class, id);
    }

    @Transactional
    public List<User> getUsers() {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery("from User", User.class).getResultList();
    }

    @Transactional
    public List<User> getUsersByDepartmentId(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery("Select u from User u join u.departments d where d.id=:id", User.class).setParameter("id", id);
        return query.getResultList();
    }

    @Transactional
    public List<User> getUsersByStatusId(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery("Select u from User u join u.studentTopic s join s.status t where t.id=:id", User.class).setParameter("id", id);
        return query.getResultList();
    }


    //TODO - to ma byc w servisach
    @Transactional
    public void deleteUser(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(user);
    }

    @Transactional
    public void saveOrUpdateUser(User user) {
        Session session = entityManager.unwrap(Session.class);


        session.saveOrUpdate(user);
    }

    @Transactional
    public void saveDepartment(Department department) {
        Session session = entityManager.unwrap(Session.class);

        //If it isn't a new department then get that department and set new values
        if (department.getId() != 0) {
            Department departmentOld = getDepartmentById(department.getId());
            departmentOld.setDepartment(department.getDepartment());
            departmentOld.setField(department.getField());
            departmentOld.setStudyMode(department.getStudyMode());
            departmentOld.setStudyGroup(department.getStudyGroup());
            session.saveOrUpdate(departmentOld);
        }else{
            session.saveOrUpdate(department);
        }
    }

    @Transactional
    public List<Department> getDepartments() {
        Session session = entityManager.unwrap(Session.class);
        Query<Department> query = session.createQuery("from Department", Department.class);
        List<Department> departments;
        try {
            departments = query.getResultList();
        } catch (NoResultException e) {
            departments = new ArrayList<>();
        }

        return departments;
    }

    @Transactional
    public Department getDepartmentById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Department department = new Department();
        department = session.get(Department.class, id);
        return department;
    }

    @Transactional
    public void deleteDepartmentById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Department department = getDepartmentById(id);
        session.delete(department);
    }

    @Transactional
    public Role getRoleByName(String roleName) {
        Session session = entityManager.unwrap(Session.class);
        Query<Role> query = session.createQuery("from Role where name = :roleName", Role.class)
                .setParameter("roleName", roleName);
        return query.getSingleResult();
    }


    @Transactional
    public List<Status> getStatuses() {
        Session session = entityManager.unwrap(Session.class);
        Query<Status> query = session.createQuery("Select distinct s from Status s", Status.class);

        return query.getResultList();
    }

    @Transactional
    public Status getStatusByName(String name) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Status s where s.name = :name", Status.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Transactional
    public List<DiplomaTopic> getDiplomaTopicsByPromotorId(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("select d from DiplomaTopic d join d.promoter p where  p.id= :id", DiplomaTopic.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Transactional
    public List<DiplomaTopic> getDiplomaTopicsByStudentId(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("select d from DiplomaTopic d join d.student s where  s.id= :id", DiplomaTopic.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Transactional
    public void deleteDiplomatopic(DiplomaTopic diplomaTopic) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(diplomaTopic);
    }

    @Transactional
    public void saveOrUpdateDiplomaTopic(DiplomaTopic diplomaTopic) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(diplomaTopic);
    }

    @Transactional
    public List<News> getNews() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from News", News.class).getResultList();
    }

    @Transactional
    public News getNewsById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(News.class, id);
    }

    @Transactional
    public void saveOrUpdateNews(News news) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(news);
    }

    @Transactional
    public void deleteNewsById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        News news = getNewsById(id);
        if (news != null) {
            session.delete(news);
        }
    }

    @Transactional
    public User getUserByEmail(String email) {
        Session session = entityManager.unwrap(Session.class);
        User user =  session.createQuery("from User u where u.email=:email", User.class).setParameter("email", email).getSingleResult();
        return user != null ? user : new User();
    }

    @Transactional
    public DiplomaTopic getDiplomatopicById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        DiplomaTopic diplomaTopic = session.get(DiplomaTopic.class, id);

        return diplomaTopic == null ? new DiplomaTopic() : diplomaTopic;
    }

    @Transactional
    public List<DiplomaTopic> getDiplomatopicsByDepartmentId(Long departmentId) {
        Session session = entityManager.unwrap(Session.class);
        List<DiplomaTopic> diplomaTopics
                = session.createQuery("select t from DiplomaTopic t join t.department d where d.id=:departmentId", DiplomaTopic.class)
                .setParameter("departmentId", departmentId)
                .getResultList();

        return diplomaTopics != null ? diplomaTopics : new ArrayList<>();
    }

    @Transactional
    public List<DiplomaTopic> getDiplomaTopicsByStatusId(long id) {
        Session session = entityManager.unwrap(Session.class);
        List<DiplomaTopic> diplomaTopics =
                session.createQuery("select t from DiplomaTopic t join t.status s where s.id=:id", DiplomaTopic.class).setParameter("id", id).getResultList();
        return diplomaTopics != null ? diplomaTopics : new ArrayList<>();
    }

    @Transactional
    public void deleteStatus(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Status status = getStatusById(id);
        if (status != null) {
            session.delete(status);
        }
    }

    @Transactional
    private Status getStatusById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Status.class, id);
    }
}
