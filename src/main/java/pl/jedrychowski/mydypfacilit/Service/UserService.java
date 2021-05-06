package pl.jedrychowski.mydypfacilit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
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
    private EmailService emailService;

    @Autowired
    private SimpleMailMessage templateCreateAccount;

    @Autowired
    private SimpleMailMessage templateStatusChange;

    @Autowired
    private SimpleMailMessage templateSendMessageAdmin;

    @Autowired
    SimpleMailMessage templateSendMessageUser;

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


    private void createNewUser(User user, List<Department> departments, String roleName) {

        //set department list
        user.setDepartments(departments);


        //get and set role
        Role role = daoHibernate.getRoleByName(roleName);
        user.setRoles(Collections.singletonList(role));

        //Generate and set password
        String password = generatePassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));

        //Add user to database
        daoHibernate.saveOrUpdateUser(user);

        emailService.sendEmail(
                templateCreateAccount.getFrom(),
                user.getEmail(),
                templateCreateAccount.getSubject(),
                String.format(Objects.requireNonNull(templateCreateAccount.getText()), user.getEmail(), password)
        );
    }

    private void updateUser(User user, List<Department> departments) {

        //get user to update
        User oldUser = daoHibernate.getUserById(user.getId());

        //set changed variables
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setDepartments(departments);
        oldUser.setAcademicTitle(user.getAcademicTitle());
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
            for (DiplomaTopic t : promotorForDiplomas) {
                if (t.getStudent() == null) {
                    daoHibernate.deleteDiplomatopic(t);
                }
            }
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

        //get old status to put in mail
        Status oldStatus;
        if (user.getStudentTopic() != null) {
            oldStatus = user.getStudentTopic().getStatus();
        } else {
            oldStatus = daoHibernate.getStatusByName("Nie wybrano tematu pracy");
        }


        Status status;
        if (setstatusto.length() > 0) {
            status = daoHibernate.getStatusByName(setstatusto);
        } else {
            status = new Status();
            status.setName(newStatusName);
        }
        user.getStudentTopic().setStatus(status);
        daoHibernate.saveOrUpdateUser(user);

        //mail
        emailService.sendEmail(
                templateStatusChange.getFrom(),
                user.getEmail(),
                templateStatusChange.getSubject(),
                String.format(templateStatusChange.getText(), oldStatus.getName(), status.getName())
        );

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

        List<DiplomaTopic> diplomaTopics = daoHibernate.getDiplomaTopicsByStatusId(removeStatus.getId());
        diplomaTopics.forEach(t -> {
            setCustomStatus(t.getStudent().getId(), changeToStatusName, null);
        });
        daoHibernate.deleteStatus(removeStatus.getId());
    }

    public String getLeftPanelInformations(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>Zalogowano jako:<br></b>").append(user).append("<br>");
        switch (user.getRoles().get(0).getName()) {
            case "ROLE_STUDENT":
                stringBuilder.append("<hr/>");
                leftPanelStudent(user, stringBuilder);
                break;
            case "ROLE_PROMOTER":
                stringBuilder.append("<hr/>");
                leftPanelPromoter(user, stringBuilder);
                break;
        }

        return stringBuilder.toString();
    }

    private StringBuilder leftPanelPromoter(User user, StringBuilder stringBuilder) {
        stringBuilder.append("<b>Przypisani studenci:<br></b>");
        List<DiplomaTopic> topics = user.getPromoterTopic();
        stringBuilder.append("<ul>");
        for (DiplomaTopic t : topics) {
            if (t.getStudent() != null && t.getStatus().getId() > 5) {
                stringBuilder.append("<li>");
                stringBuilder.append(t.getStudent()).append("<br>");
                stringBuilder.append(t.getStudent().getEmail());
                stringBuilder.append(" <button type=\"button\" class=\"btn btn-outline-primary btn-sm\" data-toggle=\"modal\" data-target=\"#pw\" onclick=\"changeValue('"+t.getStudent().getId()+"');\">" +
                        "<i class=\"fas fa-envelope\"></i>\n" +
                        "</button>").append("<br>");
                stringBuilder.append("</li>").append("<br>");
            }
        }
        stringBuilder.append("</ul>");
        return stringBuilder;
    }

    private StringBuilder leftPanelStudent(User user, StringBuilder stringBuilder) {
        stringBuilder.append("<b>Promotor:<br></b>");
        if (user.getStudentTopic() != null) {
            if (user.getStudentTopic().getPromoter() != null) {
                stringBuilder.append(user.getStudentTopic().getPromoter()).append("<br>");
                stringBuilder.append(user.getStudentTopic().getPromoter().getEmail());
                stringBuilder.append(" <button type=\"button\" class=\"btn btn-outline-primary btn-sm\" data-toggle=\"modal\" data-target=\"#pw\" onclick=\"changeValue('"+user.getStudentTopic().getPromoter().getId()+"');\">" +
                        "<i class=\"fas fa-envelope\"></i>\n" +
                        "</button>").append("<br>");
            } else {
                stringBuilder.append("Brak promotora").append("<br>");
            }
        } else {
            stringBuilder.append("Brak promotora").append("<br>");
        }
        stringBuilder.append("<hr/>");
        stringBuilder.append("<b>Twoja praca dyplomowa:<br></b>");
        if (user.getStudentTopic() != null) {
            stringBuilder.append("<br>Temat:").append("<br>");
            stringBuilder.append("\"<i>").append(user.getStudentTopic().getSubject()).append("</i>\"<br>");
            stringBuilder.append("<br>Status pracy:").append("<br>");
            stringBuilder.append("<span ");
            switch ((int) user.getStudentTopic().getStatus().getId()) {
                case 2:
                case 3:
                    stringBuilder.append("style=\"color: red; font-weight: bold;\">");
                    break;
                case 4:
                case 5:
                case 6:
                    stringBuilder.append("style=\"color: white;\">");
                    break;
                case 7:
                    stringBuilder.append("style=\"color: yellow; font-weight: bold;\">");
                    break;
                case 8:
                case 9:
                    stringBuilder.append("style=\"color: green; font-weight: bold;\">");
                    break;
                default:
                    stringBuilder.append("style=\"color: orange; font-weight: bold;\">");
                    break;
            }
            stringBuilder.append(user.getStudentTopic().getStatus().getName()).append("<br>");
            stringBuilder.append("</span>");
        } else {
            stringBuilder.append("<br>Nie wybrałeś jeszcze tematu swojej pracy dyplomowej").append("<br>");
        }
        return stringBuilder;
    }


    public void sendMail(Long userId, String content) {
        User user = daoHibernate.getUserById(userId);
        emailService.sendEmail(
                templateSendMessageAdmin.getFrom(),
                user.getEmail(),
                templateSendMessageAdmin.getSubject(),
                String.format(templateSendMessageAdmin.getText(), content)
        );
    }

    public void sendGroupMail(Long departmentId, String content, String roleName) {
        List<User> users = daoHibernate.getUsersByDepartmentId(departmentId);
        if (roleName != null) {
            Role role = daoHibernate.getRoleByName(roleName);
            users.removeIf(o -> !o.getRoles().contains(role));
        }
        users.forEach(u ->
                emailService.sendEmail(
                        templateSendMessageAdmin.getFrom(),
                        u.getEmail(),
                        templateSendMessageAdmin.getSubject(),
                        String.format(templateSendMessageAdmin.getText(), content)
                )
        );
    }

    public void sendPw(Long userId, String content) {
        User user = daoHibernate.getUserById(userId);
        emailService.sendEmail(
                templateSendMessageUser.getFrom(),
                user.getEmail(),
                templateSendMessageUser.getSubject(),
                String.format(templateSendMessageUser.getText(), content)
        );
    }
}
