package pl.jedrychowski.mydypfacilit.DAO;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.jedrychowski.mydypfacilit.Entity.Department;
import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;
import pl.jedrychowski.mydypfacilit.Entity.Role;
import pl.jedrychowski.mydypfacilit.Entity.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class DAOHibernate {
    private EntityManager entityManager;

    @Autowired
    public DAOHibernate(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional
    public void getabc(){
        Session session = entityManager.unwrap(Session.class);
/*        Query<Role> query =
                session.createQuery("from Role", Role.class);


        List<Role> roles = query.getResultList();
        for(Role r:roles){
            System.out.println(r);
        }

        Query<Department> query2 =
                session.createQuery("from Department", Department.class);


        List<Department> roles2 = query2.getResultList();
        for(Department r:roles2){
            System.out.println(r);
        }

        Query<DiplomaTopic> query3 =
                session.createQuery("from DiplomaTopic", DiplomaTopic.class);


        List<DiplomaTopic> roles3 = query3.getResultList();
        for(DiplomaTopic r:roles3){
            System.out.println(r);
        }

        Query<User> query4 =
                session.createQuery("from User", User.class);


        List<User> roles4 = query4.getResultList();
        for(User r:roles4){
            System.out.println(r);
        }*/
/*        DiplomaTopic diplomaTopic = new DiplomaTopic();
        diplomaTopic.setStatus("Test");
        diplomaTopic.setSubject("testTemat");
        diplomaTopic.setPromoter(roles4.get(2));
        diplomaTopic.setStudent(roles4.get(0));
        session.saveOrUpdate(diplomaTopic);
        session.remove(diplomaTopic);*/

/*        Department department = new Department();
        department.setDepartment("a");
        department.setField("b");
        department.setStudyGroup("d");
        department.setStudyMode("c");
        department.setUsers(roles4);
        session.saveOrUpdate(department);*/
/*        Query<Department> theQuery = session.createQuery("from Department where department_id=4", Department.class);
        Department department = theQuery.getSingleResult();*/

/*        Department department2 = session.get(Department.class, 3L);
        System.out.println("wypisane");
        System.out.println(department2);
        session.delete(department2);*/
    }

    @Transactional
    public void saveGroup(Department department){
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate(department);
    }

    @Transactional
    public List<Department> getDepartments(){
        Session session = entityManager.unwrap(Session.class);
        Query<Department> query = session.createQuery("from Department", Department.class);
        return query.getResultList();
    }

    @Transactional
    public Department getDepartmentById(Long id){
        Session session = entityManager.unwrap(Session.class);
        Department department = new Department();
        department = session.get(Department.class, id);
        return department;
    }

    @Transactional
    public void deleteDepartmentById(Long id){
        Session session = entityManager.unwrap(Session.class);
        Department department = getDepartmentById(id);
        session.delete(department);
    }

}
