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
package org.exoplatform.addons.codefest.team_b.core.utils;

import java.util.Calendar;
import java.util.List;

import org.exoplatform.addons.codefest.team_b.core.api.TaskManager;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
public class TaskManagerUtils {
  
  /**
   * Update the status when the task has been resolved.
   * 
   * @param taskId the taskId
   */
  public static void resolved(String taskId) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    
    String updater = ConversationState.getCurrent().getIdentity().getUserId();
    
    Task task = manager.get(taskId);
    if (task != null) {
      task.setValue(TaskEntity.status, Task.STATUS.RESOLVED.getName());
      task.setValue(TaskEntity.resolvedTime, Calendar.getInstance().getTimeInMillis());
      manager.update(updater, task, TaskEntity.status.getPropertyName(), TaskEntity.resolvedTime.getPropertyName());
    }
  }
  
  /**
   * Update the status when the task has been resolved.
   * 
   * @param taskId the taskId
   */
  public static void inProgress(String taskId) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    
    String updater = ConversationState.getCurrent().getIdentity().getUserId();
    
    Task task = manager.get(taskId);
    if (task != null) {
      task.setValue(TaskEntity.status, Task.STATUS.IN_PROGRESS.getName());
      task.setValue(TaskEntity.updatedTime, Calendar.getInstance().getTimeInMillis());
      task.setValue(TaskEntity.inProgressDate, Calendar.getInstance().getTimeInMillis());
      manager.update(updater, task, TaskEntity.status.getPropertyName(), TaskEntity.updatedTime.getPropertyName(), TaskEntity.inProgressDate.getPropertyName());
    }
  }
  
  /**
   * Update the status when the task has been resolved.
   * 
   * @param taskId the taskId
   */
  public static void reOpen(String taskId) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    
    String updater = ConversationState.getCurrent().getIdentity().getUserId();
    
    Task task = manager.get(taskId);
    if (task != null) {
      task.setValue(TaskEntity.status, Task.STATUS.REOPEN.getName());
      manager.update(updater, task, TaskEntity.status.getPropertyName());
    }
  }
  
  /**
   * Update the status when the task has been resolved.
   * 
   * @param taskId the taskId
   */
  public static void open(String taskId) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    
    String updater = ConversationState.getCurrent().getIdentity().getUserId();
    
    Task task = manager.get(taskId);
    if (task != null) {
      task.setValue(TaskEntity.status, Task.STATUS.OPEN.getName());
      manager.update(updater, task, TaskEntity.status.getPropertyName());
    }
  }

  /**
   * Update the log-word updated.
   * 
   * @param taskId the taskId
   */
  public static void logWork(String taskId, String value) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    
    String updater = ConversationState.getCurrent().getIdentity().getUserId();
    
    Task task = manager.get(taskId);
    if (task != null) {
      task.setValue(TaskEntity.workLogged, value);
      manager.update(updater, task, TaskEntity.workLogged.getPropertyName());
    }
  }
  
  /**
   * Update the status when the task has been resolved.
   * 
   * @param task the task
   */
  public static Task create(Task task) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    
    String remoteId = ConversationState.getCurrent().getIdentity().getUserId();
    
    IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);
    Identity reporter = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, remoteId, false);
    
    return manager.save(reporter, task);
  }
  
  /**
   * Deletes the task
   * @param taskId the taskId
   */
  public static void delete(String taskId) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    manager.delete(taskId);
  }
  
  /**
   * Gets all the tasks
   * @param taskId the taskId
   */
  public static List<Task> getAll() {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    return manager.getAll();
  }
  
  public static Task getTaskById(String id) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    return manager.get(id);
  }
  /**
   * Check string is null or empty 
   * @param String s
   * @return boolean
   */
  public static boolean isEmpty(String s) {
    return (s == null || s.trim().length() <= 0) ? true : false;
  }

  /**
   * check string array is whether empty or not
   * @param array
   * @return false if at least one element of array is not empty, true in the opposite case.
   */
  public static boolean isEmpty(String[] array) {
    if (array != null && array.length > 0) {
      for (String s : array) {
        if (s != null && s.trim().length() > 0)
          return false;
      }
    }
    return true;
  }
}
