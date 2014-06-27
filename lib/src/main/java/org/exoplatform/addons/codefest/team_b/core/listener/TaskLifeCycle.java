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

import org.exoplatform.addons.codefest.team_b.core.listener.TaskLifeCycleEvent.Type;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.social.common.lifecycle.AbstractLifeCycle;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
public class TaskLifeCycle extends AbstractLifeCycle<TaskListener, TaskLifeCycleEvent> {

  @Override
  protected void dispatchEvent(TaskListener listener, TaskLifeCycleEvent event) {
    switch(event.getType()) {
    case LOGGED_WORK:
      listener.logWorkTask(event);
      break;
    case REOPEN:
      listener.reopenedTask(event);
      break;
    case RESOLVED:
      listener.resolvedTask(event);
      break;
    case CLOSED:
      listener.closedTask(event);
      break;
    case IN_PROGRESS:
      listener.inProgressTask(event);
      break;
    case ESTIMATION:
      listener.estimationTask(event);
      break;
    case UPDATED_DESCRIPTION:
      listener.updatedTask(event);
      break;
    case ASSIGNED_TASK:
      listener.assignedTask(event);
      break;
    default:
        break;
    }
  }
  
  public void createdTask(String reporter, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.NEW_TASK, reporter, task));
  }
  
  public void logWorkTask(String logger, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.LOGGED_WORK, logger, task));
  }
  
  public void reopenedTask(String userName, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.REOPEN, userName, task));
  }
  
  public void resolvedTask(String userName, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.RESOLVED, userName, task));
  }
  
  public void closedTask(String userName, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.CLOSED, userName, task));
  }
  
  public void inProgressTask(String userName, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.IN_PROGRESS, userName, task));
  }
  
  public void estimationTask(String userName, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.ESTIMATION, userName, task));
  }
  
  public void updatedTask(String updater, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.UPDATED_DESCRIPTION, updater, task));
  }
  
  public void assginedTask(String assignee, Task task) {
    broadcast(new TaskLifeCycleEvent(Type.UPDATED_DESCRIPTION, assignee, task));
  }

}
