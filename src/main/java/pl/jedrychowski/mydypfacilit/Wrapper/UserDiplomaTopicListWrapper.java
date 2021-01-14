package pl.jedrychowski.mydypfacilit.Wrapper;

import pl.jedrychowski.mydypfacilit.Entity.DiplomaTopic;
import pl.jedrychowski.mydypfacilit.Entity.User;

import java.util.List;

public class UserDiplomaTopicListWrapper {
    List<DiplomaTopic> diplomaTopics;
    User user;

    public UserDiplomaTopicListWrapper(List<DiplomaTopic> diplomaTopics, User user) {
        this.diplomaTopics = diplomaTopics;
        this.user = user;
    }

    public List<DiplomaTopic> getDiplomaTopics() {
        return diplomaTopics;
    }

    public void setDiplomaTopics(List<DiplomaTopic> diplomaTopics) {
        this.diplomaTopics = diplomaTopics;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
