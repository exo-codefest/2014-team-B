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

import org.exoplatform.social.common.lifecycle.LifeCycleListener;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
public interface TaskListener extends LifeCycleListener<TaskLifeCycleEvent>{
  
  /**
   * Creates the calendar's task when the task has been created
   * @param event
   */
  public void createdTask(TaskLifeCycleEvent event);
  
  /**
   * Creates the comment when the task has been created
   * @param event
   */
  public void updatedTask(TaskLifeCycleEvent event);
  
  /**
   * Creates the comment when the task has been assigned
   * @param event
   */
  public void assignedTask(TaskLifeCycleEvent event);
  
  /**
   * Creates the comment when the task has been resolved
   * @param event
   */
  public void resolvedTask(TaskLifeCycleEvent event);
  
  /**
   * Creates the comment when the task has been in progress
   * @param event
   */
  public void inProgressTask(TaskLifeCycleEvent event);
  
  /**
   * Creates the comment when the task has been closed
   * @param event
   */
  public void closedTask(TaskLifeCycleEvent event);
  
  /**
   * Creates the comment when the task has been reopened
   * @param event
   */
  public void reopenedTask(TaskLifeCycleEvent event);
  
  /**
   * Creates the comment when the task has been log work
   * @param event
   */
  public void logWorkTask(TaskLifeCycleEvent event);
  
  /**
   * Creates the comment when the task has been filled the estimation
   * @param event
   */
  public void estimationTask(TaskLifeCycleEvent event);
  
}
