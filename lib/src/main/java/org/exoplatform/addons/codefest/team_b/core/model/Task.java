/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.addons.codefest.team_b.core.model;

import java.util.HashMap;

import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.storage.PropertyLiteralExpression;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class Task extends HashMap<String, Object> {
  
  public enum STATUS {
    OPEN("Open"), IN_PROGRESS("In Progress"), REOPEN("Reopen"), RESOLVED("Resolved"), CLOSED("Closed"), WAIT_THIRD_PARTY("Waiting Third Party");
    
    private String name;
    private STATUS(String name) {
      this.name = name;
    }
    
    @Override
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return name;
    }
    
  };
  
  public enum PRIORITY {
    
    BLOCKER("Blocker", 5), CRITICAL("Critical", 4), MAJOR("Major", 3), MINOR("Minor", 2), TRIVIAL("Trivial", 1);
    
    private String name;
    private int value;
    private PRIORITY(String name, int value) {
      this.name = name;
      this.value = value;
    }
    
    @Override
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return name;
    }
    
    public int getValue() {
      return value;
    }
  };
  
  /**
   * Gets priority by value
   *  5 --> Blocker
   *  4 -->Critical
   *  3 --> Major
   *  2 --> Minor
   *  1 --> Trivial
   *  
   * @param value
   * @return
   */
  public static PRIORITY getPriorityByValue(int value) {
    if (value == 5) {
      return PRIORITY.BLOCKER;
    } else if (value == 4) {
      return PRIORITY.CRITICAL;
    } else if (value == 3) {
      return PRIORITY.MAJOR;
    } else if (value == 2) {
      return PRIORITY.MINOR;
    } else {
      return PRIORITY.TRIVIAL;
    }
  }
  
  /**
   * Gets value by name
   * 
   * Blocker -> return 5
   * Critical -> return 4
   * Major -> return 3
   * Minor -> return 2
   * Trivial -> return 1
   * @param name
   * @return
   */
  public static int getPriorityByName(String name) {
    if (PRIORITY.BLOCKER.getName().equals(name)) {
      return PRIORITY.BLOCKER.getValue();
    } else if (PRIORITY.CRITICAL.getName().equals(name)) {
      return PRIORITY.CRITICAL.getValue();
    } else if (PRIORITY.MAJOR.getName().equals(name)) {
      return PRIORITY.MAJOR.getValue();
    } else if (PRIORITY.MINOR.getName().equals(name)) {
      return PRIORITY.MINOR.getValue();
    } else {
      return PRIORITY.TRIVIAL.getValue();
    }
  }
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * Gets the task's id
   * @return
   */
  public String getId() {
    return getValue(TaskEntity.id);
  }
  
  /**
   * Sets the task's id
   * @param id
   */
  public void setId(String id) {
    setValue(TaskEntity.id, id);
  }
  
  /**
   * Gets the task's id
   * @return
   */
  public String getActivityId() {
    return getValue(TaskEntity.activityId);
  }
  
  /**
   * Sets the task's activityId
   * @param activityId
   */
  public void setActivityId(String activityId) {
    setValue(TaskEntity.activityId, activityId);
  }
  
  /**
   * Gets the task's calendar taskId
   * @return
   */
  public String getTaskId() {
    return getValue(TaskEntity.taskId);
  }
  
  /**
   * Sets the task's calendar taskId
   * @param taskId
   */
  public void setTaskId(String taskId) {
    setValue(TaskEntity.taskId, taskId);
  }
  
  /**
   * Gets priority int value
   * @return
   */
  public int getPriority() {
    String display = getValue(TaskEntity.priority);
    return getPriorityByName(display);
  }
  
  /**
   * Sets the task priority
   * @param priorityInt
   */
  public void setPriority(int priorityInt) {
    String display = getPriorityByValue(priorityInt).getName();
    setValue(TaskEntity.priority, display);
  }

  /**
   * Sets the value to the given property
   * @param propertyName
   * @param value
   */
  public <T> void setValue(PropertyLiteralExpression<T> property, T value) {
    this.put(property.getPropertyName(), value);
  }
  
  /**
   * Gets the value of the property
   * @param property
   * @return
   */
  public <T> T getValue(PropertyLiteralExpression<T> property) {
    Object val = this.get(property.getPropertyName());
    return property.getType().cast(val);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Task[").append("id = ").append(getId()).append(",");
    sb.append("title = ").append(getValue(TaskEntity.title)).append(",");
    sb.append("description = ").append(getValue(TaskEntity.description)).append(",");
    sb.append("reporterId = ").append(getValue(TaskEntity.reporterId)).append(",");
    sb.append("assigneeId = ").append(getValue(TaskEntity.assigneeId));
    sb.append("]");
    
    return sb.toString();
  }
  
}
