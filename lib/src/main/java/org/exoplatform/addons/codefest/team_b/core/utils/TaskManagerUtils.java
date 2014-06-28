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
      manager.update(updater, task, TaskEntity.status.getPropertyName());
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
      manager.update(updater, task, TaskEntity.status.getPropertyName());
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
   * Update the status when the task has been resolved.
   * 
   * @param task the task
   */
  public static Task create(Task task) {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    
    String remoteId = ConversationState.getCurrent().getIdentity().getUserId();
    
    IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);
    Identity reporter = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, remoteId);
    
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

}
