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

import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
public class TaskUpdatesPublisher extends TaskListenerPlugin {
  
  private static final Log LOG = ExoLogger.getLogger(TaskUpdatesPublisher.class);
  
  @Override
  public void updatedTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::updatedTask::task's title by = " + event.getUsername());
  }

  @Override
  public void resolvedTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::resolvedTask::task's resolved = " + event.getUsername());
  }

  @Override
  public void closedTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::closedTask::task's closed by = " + event.getUsername());
  }

  @Override
  public void reopenedTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::reopenedTask::task's reopended by = " + event.getUsername());
  }

  @Override
  public void logWorkTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::logWorkTask::task's logged work by = " + event.getUsername());
  }

  @Override
  public void estimationTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::estimationTask::task's title = " + event.getTask().getValue(TaskEntity.title));
  }

  @Override
  public void assignedTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::assignedTask::task's assignee = " + event.getUsername());
  }

  @Override
  public void inProgressTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::assignedTask::task's in progress by = " + event.getUsername());
  }

  @Override
  public void createdTask(TaskLifeCycleEvent event) {
    LOG.info("TaskUpdatesPublisher::assignedTask::task's created by = " + event.getUsername());
  }
  
}
