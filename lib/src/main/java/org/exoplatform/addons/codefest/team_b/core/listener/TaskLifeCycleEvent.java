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
package org.exoplatform.addons.codefest.team_b.core.listener;

import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.social.common.lifecycle.LifeCycleEvent;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
public class TaskLifeCycleEvent extends LifeCycleEvent<String, Task> {
  
  public enum Type {NEW_TASK, ASSIGNED_TASK, LOGGED_WORK, UPDATED_DESCRIPTION, IN_PROGRESS, REOPEN, RESOLVED, CLOSED, ESTIMATION}

  private Type type;
  
  public TaskLifeCycleEvent(Type type, String user, Task task) {
    super(user, task);
    this.type = type;
  }
  
  public Type getType() {
    return type;
  }
  
  /**
   * username of the task updated
   * @return
   */
  public String getUsername() {
    return source;
  }

  /**
   * actual task section;
   * @return
   */
  public Task getTask() {
    return payload;
  }

}
