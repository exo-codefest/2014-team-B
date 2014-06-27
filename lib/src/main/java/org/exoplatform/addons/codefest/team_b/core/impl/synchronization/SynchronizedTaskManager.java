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
package org.exoplatform.addons.codefest.team_b.core.impl.synchronization;

import java.util.List;

import org.exoplatform.addons.codefest.team_b.core.impl.TaskManagerImpl;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.addons.codefest.team_b.core.model.TaskFilter;
import org.exoplatform.social.core.identity.model.Identity;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class SynchronizedTaskManager extends TaskManagerImpl {

  @Override
  public Task save(Identity reporter, Task task) {
    
    boolean created = startSynchronization();
    try {
      return super.save(reporter, task);
    }
    finally {
      stopSynchronization(created);
    }
  }
  
  @Override
  public Task get(String id) {
    boolean created = startSynchronization();
    try {
      return super.get(id);
    }
    finally {
      stopSynchronization(created);
    }
  }
  
  @Override
  public List<Task> load(TaskFilter filter, int offset, int limit) {
    boolean created = startSynchronization();
    try {
      return super.load(filter, offset, limit);
    }
    finally {
      stopSynchronization(created);
    }
  }
  
  @Override
  public int count(TaskFilter filter) {
    boolean created = startSynchronization();
    try {
      return super.count(filter);
    }
    finally {
      stopSynchronization(created);
    }
  }
  
  @Override
  public void update(Identity updater, Task task, String... propertyNames) {
    boolean created = startSynchronization();
    try {
      super.update(updater, task, propertyNames);
    }
    finally {
      stopSynchronization(created);
    }
  }
  
  @Override
  public void delete(String id) {
    boolean created = startSynchronization();
    try {
      super.delete(id);
    }
    finally {
      stopSynchronization(created);
    }
  }
  
}
