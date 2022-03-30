package demo.model;

import java.io.Serializable;
import java.time.Instant;

public class UserEvent implements Serializable {

  String eventDate;
  String userId;
  String userName;
  Integer userAge;
  String userGender;
  String feedCategory;
  Integer timeSpentInSec;

  public UserEvent() {}

  public String getEventDate() {
    return eventDate;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }

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

  public void setTimeSpentInSec(Integer timeSpentInSec) {
    this.timeSpentInSec = timeSpentInSec;
  }

  @Override
  public String toString() {
    return "UserEvent{"
        + "eventDate='"
        + eventDate
        + '\''
        + ", userId='"
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
