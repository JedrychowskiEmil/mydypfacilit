package pl.jedrychowski.mydypfacilit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.*;
import pl.jedrychowski.mydypfacilit.Wrapper.PromoterStudentListWrapper;
import pl.jedrychowski.mydypfacilit.Wrapper.UserDepartmentListWrapper;
import pl.jedrychowski.mydypfacilit.Wrapper.UserDepartmentWrapper;
import pl.jedrychowski.mydypfacilit.Wrapper.UserListWrapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private DAOHibernate daoHibernate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean changePassword(User user, String password, String password2) {
        if (!password.equals(password2)) return false;

        user.setPassword(bCryptPasswordEncoder.encode(password));
        daoHibernate.saveOrUpdateUser(user);
        return true;
    }

    public List<UserListWrapper> getUserListWrapperList(Long departmentID, Long statusId, String role) {

        List<User> users;

        //get only users with department id or all of them
        if (departmentID != null) {
            users = daoHibernate.getUsersByDepartmentId(departmentID);
        } else {
            users = daoHibernate.getUsers();
        }

        //retain only users with role
        Role userRole = daoHibernate.getRoleByName(role);
        users.removeIf(u -> !u.getRoles().contains(userRole));

        //retain only user with depratment id and diplomatopic status
        if (statusId != null) {
            if (statusId == 1) {
                users.removeIf(u -> u.getStudentTopic() != null);
            } else {
                List<User> usersWithStatus = daoHibernate.getUsersByStatusId(statusId);
                users.retainAll(usersWithStatus);
            }
        }

        //get unique departments from all users
        Set<Department> departments = new HashSet<>();
        users.forEach(u -> departments.addAll(u.getDepartments()));

        //fill wrapper sorting departments and assigned to it users
        List<UserListWrapper> userListWrappers = new ArrayList<>();
        for (Department d : departments) {
            UserListWrapper userListWrapper = new UserListWrapper(new ArrayList<>(), d);
            for (User u : users) {
                if (u.getDepartments().size() > 0) {
                    if (u.getDepartments().get(0) == d) {
                        userListWrapper.getUsers().add(u);
                    }
                }
            }
            userListWrappers.add(userListWrapper);
        }

        //create new tmp group with users without assigned department
        if (departmentID == null) {
            Department department = new Department();
            department.setDepartment("Nie przypisani użytkownicy");
            department.setField("");
            department.setStudyMode("");
            department.setStudyGroup("");
            UserListWrapper userListWrapper = new UserListWrapper(new ArrayList<>(), department);

            for (User u : users) {
                if (u.getDepartments().size() == 0 || u.getDepartments() == null) {
                    userListWrapper.getUsers().add(u);
                }
            }
            userListWrappers.add(userListWrapper);
        }

        userListWrappers.removeIf(x -> x.getUsers().isEmpty());
        userListWrappers.forEach(x -> x.getUsers().sort(User::compareTo));
        userListWrappers.sort(UserListWrapper::compareTo);
        return userListWrappers;
    }

    public List<PromoterStudentListWrapper> getPromoterStudentLListWrapperList() {

        //get all users with role promoter
        List<User> users = daoHibernate.getUsers();

        List<User> promoters = new ArrayList<>();
        List<User> studentsWithTopics = new ArrayList<>();
        Role promoterRole = daoHibernate.getRoleByName("ROLE_PROMOTER");
        Role studentRole = daoHibernate.getRoleByName("ROLE_STUDENT");
        for (User u : users) {
            if (u.getRoles().contains(promoterRole)) {
                promoters.add(u);
            } else if (u.getRoles().contains(studentRole) && u.getStudentTopic() != null) {
                studentsWithTopics.add(u);
            }
        }

        //Start wrapping
        List<PromoterStudentListWrapper> promoterStudentListWrappers = new ArrayList<>();

        //add to wrapper promoter and his assigned students
        for (User promoter : promoters) {
            PromoterStudentListWrapper promoterStudentListWrapper = new PromoterStudentListWrapper(promoter, new ArrayList<>());
            for (User student : studentsWithTopics) {
                if (student.getStudentTopic().getPromoter() == promoter) {
                    promoterStudentListWrapper.getStudents().add(student);
                }
            }
            promoterStudentListWrappers.add(promoterStudentListWrapper);
        }

        promoterStudentListWrappers.sort(PromoterStudentListWrapper::compareTo);

        return promoterStudentListWrappers;
    }


    public void createOrUpdateUser(UserDepartmentWrapper userDepartmentWrapper, String role) {
        User user = userDepartmentWrapper.getUser();
        if (userDepartmentWrapper.getDepartmentId() != null) {
            Department department = daoHibernate.getDepartmentById(userDepartmentWrapper.getDepartmentId());
            createOrUpdateUser(user, Collections.singletonList(department), role);
        } else {
            createOrUpdateUser(user, null, role);
        }


    }

    public void createOrUpdateUser(UserDepartmentListWrapper userDepartmentListWrapper, String role) {
        User user = userDepartmentListWrapper.getUser();
        List<Department> departments = new ArrayList<>();
        for (Long id : userDepartmentListWrapper.getDepartmentsId()) {
            departments.add(daoHibernate.getDepartmentById(id));
        }

        createOrUpdateUser(user, departments, role);
    }

    public void createOrUpdateUser(User user, List<Department> departments, String role) {

        //check if its a new fresh user
        if (user.getId() == 0) {
            createNewUser(user, departments, role);
        } else {
            updateUser(user, departments);
        }
    }


    //TODO - wyslanie maila z potwierdzeniem
    //TODO - bcrypt
    private void createNewUser(User user, List<Department> departments, String roleName) {

        //set department list
        user.setDepartments(departments);


        //get and set role
        Role role = daoHibernate.getRoleByName(roleName);
        user.setRoles(Collections.singletonList(role));

        //Generate and set password
        /*String password = generatePassword();*/
        String password = generateFun123();
        user.setPassword(bCryptPasswordEncoder.encode(password));

        //Add user to database
        daoHibernate.saveOrUpdateUser(user);

        //TODO - send email with confirmation

    }

    private void updateUser(User user, List<Department> departments) {

        //get user to update
        User oldUser = daoHibernate.getUserById(user.getId());

        //set changed variables
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setDepartments(departments);

        //update in database
        daoHibernate.saveOrUpdateUser(oldUser);
    }

    public void deleteUserById(Long id, String roleName) {
        User user = daoHibernate.getUserById(id);

        Role role = daoHibernate.getRoleByName(roleName);

        //get diplomaTitle and set promotor to null to prevent promot from being deleted
        if (role.getName().equals("ROLE_STUDENT")) {
            DiplomaTopic diplomaTopic = user.getStudentTopic();
            if (diplomaTopic != null) {
                diplomaTopic.setPromoter(null);
                daoHibernate.deleteDiplomatopic(diplomaTopic);
            }

        } else if (role.getName().equals("ROLE_PROMOTER")) {

            //get all diplomaTitles user promotes set promotor field to null, change status and update them
            List<DiplomaTopic> promotorForDiplomas = daoHibernate.getDiplomaTopicsByPromotorId(user.getId());
            promotorForDiplomas.forEach(x -> x.setPromoter(null));
            Status status = daoHibernate.getStatusByName("Brak promotora");
            promotorForDiplomas.forEach(x -> x.setStatus(status));
            promotorForDiplomas.forEach(x -> daoHibernate.saveOrUpdateDiplomaTopic(x));
        }

        //set null to prevent deleting them
        user.setDepartments(null);
        user.setRoles(null);

        daoHibernate.deleteUser(user);

    }


    public String generatePassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 16;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

    private String generateFun123() {
        return "fun123";
    }

    public void setCustomStatus(Long id, String setstatusto, String newStatusName) {
        User user = daoHibernate.getUserById(id);
        Status status;
        if (setstatusto.length() > 0) {
            status = daoHibernate.getStatusByName(setstatusto);
        } else {
            status = new Status();
            status.setName(newStatusName);
        }
        user.getStudentTopic().setStatus(status);
        daoHibernate.saveOrUpdateUser(user);
    }

    public User getUserByemail(String email) {
        return daoHibernate.getUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = daoHibernate.getUserByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }


    public List<User> getPromotersByDepartmentId(Long departmentId) {
        List<User> users = daoHibernate.getUsersByDepartmentId(departmentId);
        Role role = daoHibernate.getRoleByName("ROLE_PROMOTER");
        users.removeIf(t -> !t.getRoles().contains(role));
        return users;
    }

    public Role getRoleByName(String role_promoter) {
        return daoHibernate.getRoleByName(role_promoter);
    }

    public void removeCustomStatus(String removeStatusName, String changeToStatusName) {
        Status removeStatus = daoHibernate.getStatusByName(removeStatusName);
        Status changeToStatus = daoHibernate.getStatusByName(changeToStatusName);
        List<DiplomaTopic> diplomaTopics = daoHibernate.getDiplomaTopicsByStatusId(removeStatus.getId());
        diplomaTopics.forEach(t -> {
            t.setStatus(changeToStatus);
            daoHibernate.saveOrUpdateDiplomaTopic(t);
        });
        daoHibernate.deleteStatus(removeStatus.getId());
    }
}
