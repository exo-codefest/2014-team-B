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

import org.exoplatform.social.common.lifecycle.AbstractListenerPlugin;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
public abstract class TaskListenerPlugin extends AbstractListenerPlugin implements TaskListener{

  @Override
  public abstract void createdTask(TaskLifeCycleEvent event);
  
  @Override
  public abstract void updatedTask(TaskLifeCycleEvent event);

  @Override
  public abstract void resolvedTask(TaskLifeCycleEvent event);
  @Override
  public abstract void closedTask(TaskLifeCycleEvent event);

  @Override
  public abstract void reopenedTask(TaskLifeCycleEvent event);

  @Override
  public abstract void logWorkTask(TaskLifeCycleEvent event);

  @Override
  public abstract void estimationTask(TaskLifeCycleEvent event);

  @Override
  public abstract void assignedTask(TaskLifeCycleEvent event);

  @Override
  public abstract void inProgressTask(TaskLifeCycleEvent event);

}
