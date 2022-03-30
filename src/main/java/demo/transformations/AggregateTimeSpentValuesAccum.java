package demo.transformations;

import demo.model.UserEvent;
import demo.model.UserRow;
import java.io.Serializable;

public class AggregateTimeSpentValuesAccum implements Serializable {

  private UserRow userRow;

  AggregateTimeSpentValuesAccum() {}

  void add(UserEvent event) {
    if (userRow == null) {
      userRow = new UserRow();
      userRow.setUserId(event.getUserId());
      userRow.setUserAge(event.getUserAge());
      userRow.setUserName(event.getUserName());
      userRow.setUserGender(event.getUserGender());
      userRow.setFeedCategory(event.getFeedCategory());
      userRow.updateTimeSpentInSec(event.getTimeSpentInSec());
    } else if (userRow.getUserId().equals(event.getUserId())){
      userRow.updateTimeSpentInSec(event.getTimeSpentInSec());
    }
  }

  void mergeAccums(AggregateTimeSpentValuesAccum accum){
    if(userRow != null && accum.getUserRow() != null){
      if(userRow.getUserId().equals(accum.getUserRow().getUserId())){
        userRow.updateTimeSpentInSec(accum.getUserRow().getTimeSpentInSec());
      }
    } else if(userRow == null && accum.getUserRow() != null){
      userRow = accum.getUserRow();
    }
  }

  UserRow getUserRow(){
    return userRow;
  }
}
