package Model;

import android.text.BoringLayout;

import java.util.Date;

public class User {
    private String email, password, username,age, height, weight, moneySpent, timeAddicted, imageUri;
//    private Long totalTimeSober, lastEndTime;
    private Date soberSince;
   // private Boolean buttonPressed;


    public User(String username, String password, String age, String height, String weight, String moneySpent, String timeAddicted,
                String imageUri, Date soberSince) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.moneySpent = moneySpent;
        this.timeAddicted = timeAddicted;
//        this.totalTimeSober = totalTimeSober;
//        this.lastEndTime = lastEndTime;
        ///this.buttonPressed = buttonPressed;
        this.imageUri = imageUri;
        this.soberSince = soberSince;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

//    public Long getTotalTimeSober() {
//        return totalTimeSober;
//    }
//
//    public void setTotalTimeSober(Long totalTimeSober) {
//        this.totalTimeSober = totalTimeSober;
//    }
//
//    public Long getLastEndTime() {
//        return lastEndTime;
//    }
//
//    public void setLastEndTime(Long lastEndTime) {
//        this.lastEndTime = lastEndTime;
//    }

//    public Boolean getButtonPressed() {
//        return buttonPressed;
//    }
//
//    public void setButtonPressed(Boolean buttonPressed) {
//        this.buttonPressed = buttonPressed;
//    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Date getSoberSince() {
        return soberSince;
    }

    public void setSoberSince(Date soberSince) {
        this.soberSince = soberSince;
    }
}
