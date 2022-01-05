package Model;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Goal {
    private String theGoal, measurement, goalUid;
    private Integer totalNeeded, current;
    private Date deadline;
    private Long timestamp;
    private Boolean saved;

    public Goal(String theGoal, String measurement, Integer totalNeeded, Integer current, Date deadline, Long timestamp, String goalUid,
    Boolean saved) {
        this.theGoal = theGoal;
        this.measurement = measurement;
        this.totalNeeded = totalNeeded;
        this.current = current;
        this.deadline = deadline;
        this.timestamp = timestamp;
        this.goalUid = goalUid;
        this.saved = saved;
    }

    public Goal() {

    }

    public String getTheGoal() {
        return theGoal;
    }

    public void setTheGoal(String theGoal) {
        this.theGoal = theGoal;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public Integer getTotalNeeded() {
        return totalNeeded;
    }

    public void setTotalNeeded(Integer totalNeeded) {
        this.totalNeeded = totalNeeded;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getGoalUid() {
        return goalUid;
    }

    public void setGoalUid(String goalUid) {
        this.goalUid = goalUid;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }
}
