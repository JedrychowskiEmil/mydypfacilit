package pl.jedrychowski.mydypfacilit;

public class FilterUsers {
    private Long filterDepartmentId;
    private Long filterStatusId;

    public FilterUsers(Long filterDepartmentId, Long filterStatusId) {
        this.filterDepartmentId = filterDepartmentId;
        this.filterStatusId = filterStatusId;
    }

    public Long getFilterDepartmentId() {
        return filterDepartmentId;
    }

    public void setFilterDepartmentId(Long filterDepartmentId) {
        this.filterDepartmentId = filterDepartmentId;
    }

    public Long getFilterStatusId() {
        return filterStatusId;
    }

    public void setFilterStatusId(Long filterStatusId) {
        this.filterStatusId = filterStatusId;
    }

    @Override
    public String toString() {
        return "FilterUsers{" +
                "departmentId=" + filterDepartmentId +
                ", statusId=" + filterStatusId +
                '}';
    }
}
