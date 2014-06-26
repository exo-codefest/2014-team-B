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

import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.addons.codefest.team_b.core.model.TaskFilter;
import org.exoplatform.commons.utils.ListAccess;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class TaskListAccess implements ListAccess<Task> {
  /** the task manager */
  private TaskManager taskManager;
  
  /** the task filter */
  private TaskFilter taskFilter;

  public TaskListAccess(TaskManager taskManager, TaskFilter filter) {
    this.taskFilter = filter;
    this.taskManager = taskManager;
  }
  
  @Override
  public Task[] load(int index, int length) throws Exception, IllegalArgumentException {
    List<Task> tasks = taskManager.load(taskFilter, index, length);
    return tasks.toArray(new Task[tasks.size()]);
  }

  @Override
  public int getSize() throws Exception {
    return taskManager.count(taskFilter);
  }

}
