package ua.com.foxminded.dto;

import ua.com.foxminded.entity.Group;

public class StudentDTO extends UserDTO {
  private Group group;

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }
}
