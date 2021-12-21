package Model;

public class User {
    private String username, password, age, height, weight, moneySpent, timeAddicted, timeSober;

    public User(String username, String password, String age, String height, String weight, String moneySpent, String timeAddicted, String timeSober) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.moneySpent = moneySpent;
        this.timeAddicted = timeAddicted;
        this.timeSober = timeSober;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(String moneySpent) {
        this.moneySpent = moneySpent;
    }

    public String getTimeAddicted() {
        return timeAddicted;
    }

    public void setTimeAddicted(String timeAddicted) {
        this.timeAddicted = timeAddicted;
    }

    public String getTimeSober() {
        return timeSober;
    }

    public void setTimeSober(String timeSober) {
        this.timeSober = timeSober;
    }
}
