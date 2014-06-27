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
package org.exoplatform.addons.codefest.team_b.core.api;

import java.util.List;

import org.exoplatform.addons.codefest.team_b.core.listener.TaskListenerPlugin;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.addons.codefest.team_b.core.model.TaskFilter;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.social.core.identity.model.Identity;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public interface TaskManager {

  /**
   * 
   * @param task
   * @return
   */
  public Task save(Identity identity, Task task);
  
  /**
   * 
   * @param filter
   * @param offset
   * @param limit
   * @return
   */
  public List<Task> getAll();

  /**
   * Gets all of task by reporter
   * @param reporter the reporter as remoteId
   * @return
   */
  public List<Task> getAllByReporter(String reporter);
  
  /**
   * Gets all of task by assignee
   * @param assignee the assignee as remoteId
   * @return
   */
  public List<Task> getAllByAssignee(String assignee);
  
  /**
   * 
   * @param filter
   * @param offset
   * @param limit
   * @return
   */
  public ListAccess<Task> find(TaskFilter filter);
  
  /**
   * 
   * @param filter
   * @param offset
   * @param limit
   * @return
   */
  public List<Task> load(TaskFilter filter, int offset, int limit);

  /**
   * 
   * @param filter
   * @return
   */
  public int count(TaskFilter filter);
  
  /**
   * Gets the task by the given Id
   * 
   * @param id the id
   * @return the task
   */
  public Task get(String id);
  
  
  /**
   * 
   * @param task
   * @return
   */
  public void update(Identity updater, Task task, String ...propertyNames);
  
  /**
   * 
   * @param task
   * @return
   */
  public void update(String remoteId, Task task, String ...propertyNames);
  
  
  /**
   * Deletes the task by the given Id
   * 
   * @param id the id
   */
  public void delete(String id);
  
  /**
   * Adds the Task Listener plugin
   * @param plugin the plugin
   */
  public void addTaskListener(TaskListenerPlugin plugin);
  
}
