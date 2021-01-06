package pl.jedrychowski.mydypfacilit;

import java.util.ArrayList;
import java.util.List;


public class ShowOption {
        private boolean showStaff;
        private boolean showStudents;

    public ShowOption(boolean showStaff, boolean showStudents) {
        this.showStaff = showStaff;
        this.showStudents = showStudents;
    }

    public boolean isShowStaff() {
        return showStaff;
    }

    public void setShowStaff(boolean showStaff) {
        this.showStaff = showStaff;
    }

    public boolean isShowStudents() {
        return showStudents;
    }

    public void setShowStudents(boolean showStudents) {
        this.showStudents = showStudents;
    }
}
