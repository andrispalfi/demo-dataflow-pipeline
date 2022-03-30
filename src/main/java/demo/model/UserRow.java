package demo.model;

public class UserRow {

  private String userId;
  private String userName;
  private Integer userAge;
  private String userGender;
  private String feedCategory;
  private Integer timeSpentInSec = 0;

  public UserRow() {}

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Integer getUserAge() {
    return userAge;
  }

  public void setUserAge(Integer userAge) {
    this.userAge = userAge;
  }

  public String getUserGender() {
    return userGender;
  }

  public void setUserGender(String userGender) {
    this.userGender = userGender;
  }

  public String getFeedCategory() {
    return feedCategory;
  }

  public void setFeedCategory(String feedCategory) {
    this.feedCategory = feedCategory;
  }

  public Integer getTimeSpentInSec() {
    return timeSpentInSec;
  }

  public void updateTimeSpentInSec(Integer timeSpentInSec) {
    this.timeSpentInSec += timeSpentInSec;
  }

  @Override
  public String toString() {
    return "UserRow{"
        + "userId='"
        + userId
        + '\''
        + ", userName='"
        + userName
        + '\''
        + ", userAge="
        + userAge
        + ", userGender='"
        + userGender
        + '\''
        + ", feedCategory='"
        + feedCategory
        + '\''
        + ", timeSpentInSec="
        + timeSpentInSec
        + '}';
  }
}
