package demo.model;

import java.io.Serializable;
import java.util.Objects;

public class UserEventGroupKey implements Serializable {

  private String userId;
  private String userName;
  private Integer userAge;
  private String userGender;
  private String feedCategory;

  public UserEventGroupKey() {}

  public UserEventGroupKey(UserEvent userEvent) {
      Objects.requireNonNull(userEvent);
      this.userId = userEvent.getUserId();
      this.userName = userEvent.getUserName();
      this.userAge = userEvent.getUserAge();
      this.userGender = userEvent.getUserGender();
      this.feedCategory = userEvent.getFeedCategory();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserEventGroupKey that = (UserEventGroupKey) o;
    return Objects.equals(userId, that.userId)
        && Objects.equals(userName, that.userName)
        && Objects.equals(userAge, that.userAge)
        && Objects.equals(userGender, that.userGender)
        && Objects.equals(feedCategory, that.feedCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, userName, userAge, userGender, feedCategory);
  }

  @Override
  public String toString() {
    return "UserEventGroupKey{"
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
        + '}';
  }
}
