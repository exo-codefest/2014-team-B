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
package org.exoplatform.addons.codefest.team_b.core.impl;

import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.affectVersion;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.assigneeId;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.businessValue;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.createdTime;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.description;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.estimation;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.fixVersion;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.groupId;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.linkUrl;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.remaining;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.reporterId;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.resolution;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.resolvedTime;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.status;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.taskId;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.title;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.type;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.updatedTime;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.workLogged;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.NodeIterator;

import org.chromattic.api.query.Ordering;
import org.chromattic.api.query.QueryBuilder;
import org.chromattic.api.query.QueryResult;
import org.exoplatform.addons.codefest.team_b.core.api.TaskListAccess;
import org.exoplatform.addons.codefest.team_b.core.api.TaskManager;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TasksRootEntity;
import org.exoplatform.addons.codefest.team_b.core.listener.TaskLifeCycle;
import org.exoplatform.addons.codefest.team_b.core.listener.TaskListenerPlugin;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.addons.codefest.team_b.core.model.TaskFilter;
import org.exoplatform.addons.codefest.team_b.core.storage.NodeNotFoundException;
import org.exoplatform.addons.codefest.team_b.core.storage.query.WhereExpression;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class TaskManagerImpl extends AbstractManager implements TaskManager {
  
  private static final Log LOG = ExoLogger.getLogger(TaskManagerImpl.class);
  
  private final TaskLifeCycle taskLifeCycle;

  public TaskManagerImpl() {
    taskLifeCycle = new TaskLifeCycle();
  }
  
  @Override
  public Task save(Identity identity, Task task) {
    String id = task.getId();
    //
    if (id == null) {
      _create(task);
      taskLifeCycle.createdTask(identity.getRemoteId(), task);
    } else {
      _save(task);
    }
    
    getSession().save();
    //
    return task;
  }
  
  /**
   * Creates new task
   * @param task
   */
  private void _create(Task task) {
    TasksRootEntity root = getTasksRoot();
    
    long currentMillis = System.currentTimeMillis();
    Long createdTime = task.getValue(TaskEntity.createdTime) != null ? task.getValue(TaskEntity.createdTime) : currentMillis;
    try {
      //
      String taskName = String.valueOf(createdTime);
      TaskEntity entity = root.createTask(taskName);
      root.getTasks().put(taskName, entity);
      //
      LOG.info("taskId: " + entity.getId());
      task.setId(entity.getId());
      fillEntityFromModel(task, entity);
    } catch (Exception e) {
      LOG.warn("Creates the task unseccessfully.", e.getMessage());
    }
  }
  
  /**
   * Fills the value from entity to model
   * @param task
   * @param entity
   */
  private void fillEntityFromModel(Task task, TaskEntity entity) {
    entity.setTaskId(task.getValue(taskId));
    entity.setTitle(task.getValue(title));
    entity.setAssigneeId(task.getValue(assigneeId));
    entity.setReporterId(task.getValue(reporterId));
    entity.setType(task.getValue(type));
    entity.setPriority(task.getPriority());
    entity.setAffectVersion(task.getValue(affectVersion));
    entity.setFixVersion(task.getValue(fixVersion));
    entity.setBusinessValue(task.getValue(businessValue));
    entity.setStatus(task.getValue(status));
    entity.setResolution(task.getValue(resolution));
    entity.setDescription(task.getValue(description));
    entity.setCreatedTime(task.getValue(createdTime));
    entity.setUpdatedTime(task.getValue(updatedTime));
    entity.setResolvedTime(task.getValue(resolvedTime));
    entity.setEstimation(task.getValue(estimation));
    entity.setRemaining(task.getValue(remaining));
    entity.setWorkLogged(task.getValue(workLogged));
    entity.setGroupId(task.getValue(groupId));
    entity.setLinkUrl(task.getValue(linkUrl));
  }
  
  /**
   * Fills the value from entity to model
   * 
   * @param entity
   * @param task
   */
  private void fillModelFromEntity(TaskEntity entity, Task task) {
    task.setId(entity.getId());
    task.setValue(taskId, entity.getTaskId());
    task.setValue(title, entity.getTitle());
    task.setValue(assigneeId, entity.getAssigneeId());
    task.setValue(reporterId, entity.getReporterId());
    task.setValue(type, entity.getType());
    task.setPriority(entity.getPriority());
    task.setValue(affectVersion, entity.getAffectVersion());
    task.setValue(fixVersion, entity.getFixVersion());
    task.setValue(businessValue, entity.getBusinessValue());
    task.setValue(status, entity.getStatus());
    task.setValue(resolution, entity.getResolution());
    task.setValue(description, entity.getDescription());
    task.setValue(createdTime, entity.getCreatedTime());
    task.setValue(updatedTime, entity.getUpdatedTime());
    task.setValue(resolvedTime, entity.getResolvedTime());
    task.setValue(estimation, entity.getEstimation());
    task.setValue(remaining, entity.getRemaining());
    task.setValue(workLogged, entity.getWorkLogged());
    task.setValue(groupId, entity.getGroupId());
    task.setValue(linkUrl, entity.getLinkUrl());
  }
  
  /**
   * Updates the task values when it's created
   * @param task the given task
   */
  private void _save(Task task) {
    String id = task.getValue(TaskEntity.id);
    try {
      TaskEntity entity = _findById(TaskEntity.class, id);
      fillEntityFromModel(task, entity);
    } catch (NodeNotFoundException e) {
      LOG.info("Can not find the task by the given id = " + id, e.getMessage());
    }
  }

  @Override
  public ListAccess<Task> find(TaskFilter filter) {
    return new TaskListAccess(this, filter);
  }

  @Override
  public Task get(String id) {
    if (id == null) {
      throw new NullPointerException("id can not be null.");
    }
    Task task = new Task();
    try {
      TaskEntity entity = _findById(TaskEntity.class, id);
      fillModelFromEntity(entity, task);
    } catch (NodeNotFoundException e) {
      LOG.info("Can not find the task by the given id = " + id, e.getMessage());
      return null;
    }
    return task;
  }

  @Override
  public void update(Identity updater, Task task, String... propertyNames) {
    String id = task.getValue(TaskEntity.id);
    try {
      TaskEntity entity = _findById(TaskEntity.class, id);
      
      for(String name : propertyNames) {
        //
       if (name.equals(status.getPropertyName())) {
          String status = task.getValue(TaskEntity.status);
          if (Task.STATUS.CLOSED.equals(status)) {
            taskLifeCycle.closedTask(updater.getRemoteId(), task);
          } else if (Task.STATUS.IN_PROGRESS.equals(status)) {
            taskLifeCycle.inProgressTask(updater.getRemoteId(), task);
          } else if (Task.STATUS.REOPEN.equals(status)) {
            taskLifeCycle.reopenedTask(updater.getRemoteId(), task);
          } else if (Task.STATUS.RESOLVED.equals(status)) {
            taskLifeCycle.resolvedTask(updater.getRemoteId(), task);
          }
          entity.setStatus(status);
        } else if (name.equals(title.getPropertyName()) || name.equals(description.getPropertyName())) {
          taskLifeCycle.updatedTask(updater.getRemoteId(), task);
          entity.setTitle(task.getValue(title));
          entity.setDescription(task.getValue(description));
        } else if (name.equals(workLogged.getPropertyName())) {
          taskLifeCycle.logWorkTask(updater.getRemoteId(), task);
          //TODO need add when log work
          entity.setWorkLogged(task.getValue(workLogged));
        } else if (name.equals(assigneeId.getPropertyName())) {
          taskLifeCycle.assginedTask(updater.getRemoteId(), task);
          entity.setAssigneeId(task.getValue(assigneeId));
        }
      }
      
      getSession().save();
      
    } catch (NodeNotFoundException e) {
      LOG.info("Can not find the task by the given id = " + id, e.getMessage());
    }
  }

  @Override
  public List<Task> load(TaskFilter filter, int offset, int limit) {
    return null;
  }

  @Override
  public int count(TaskFilter filter) {
    return 0;
  }

  @Override
  public void delete(String id) {
    _removeById(TaskEntity.class, id);
    
  }

  @Override
  public List<Task> getAll() {
    TasksRootEntity root = getTasksRoot();
    Iterator<TaskEntity> it = root.getTaskList().iterator();
    
    List<Task> result = new ArrayList<Task>();
    Task task = null;
    while(it.hasNext()) {
      TaskEntity entity = it.next();
      LOG.info("path = " + entity.getPath());
      task = new Task();
      fillModelFromEntity(entity, task);
      result.add(task);
    }
    return result;
  }

  @Override
  public void addTaskListener(TaskListenerPlugin plugin) {
    this.taskLifeCycle.addListener(plugin);
  }

  @Override
  public List<Task> getAllByReporter(String reporter) {
    IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);
    Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, reporter);
    
    QueryBuilder<TaskEntity> builder = getSession().createQueryBuilder(TaskEntity.class);
    WhereExpression whereExpression = new WhereExpression();
    whereExpression.startGroup();
    whereExpression.equals(TaskEntity.reporterId, identity.getId());
    whereExpression.endGroup();
    
    LOG.info("getAllByReporter::query = " + whereExpression.toString());
    builder.where(whereExpression.toString());
    
    builder.orderBy(TaskEntity.priority.getName(), Ordering.DESC);
    builder.orderBy(TaskEntity.businessValue.getName(), Ordering.DESC);
    

    List<Task> tasks = new ArrayList<Task>();

    try {
      //
      QueryResult<TaskEntity> results = builder.get().objects();

      LOG.info("getAllByReporter::size = " + results.size());
      
      while (results.hasNext()) {
        TaskEntity currentTask = results.next();
        Task t = new Task();
        fillModelFromEntity(currentTask, t);
        tasks.add(t);
      }
    } catch (Exception e) {
      LOG.info("Failed query", e.getMessage());
    }
    return tasks;
  }
  

  @Override
  public List<Task> getAllByAssignee(String assignee) {
    IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);
    Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, assignee);
    
    QueryBuilder<TaskEntity> builder = getSession().createQueryBuilder(TaskEntity.class);
    WhereExpression whereExpression = new WhereExpression();
    whereExpression.startGroup();
    whereExpression.equals(TaskEntity.assigneeId, identity.getId());
    whereExpression.endGroup();
    
    LOG.info("getAllByAssignee::query = " + whereExpression.toString());
    builder.where(whereExpression.toString());
    
    builder.orderBy(TaskEntity.priority.getName(), Ordering.DESC);
    builder.orderBy(TaskEntity.businessValue.getName(), Ordering.DESC);
    

    List<Task> tasks = new ArrayList<Task>();

    try {
      //
      QueryResult<TaskEntity> results = builder.get().objects();

      LOG.info("getAllByAssignee::size = " + results.size());
      
      while (results.hasNext()) {
        TaskEntity currentTask = results.next();
        Task t = new Task();
        fillModelFromEntity(currentTask, t);
        tasks.add(t);
      }
    } catch (Exception e) {
      LOG.info("Failed query", e.getMessage());
    }
    return tasks;
  }

  @Override
  public void update(String updater, Task task, String... propertyNames) {
    IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);
    Identity identityUpdater = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, updater);
    update(identityUpdater, task, propertyNames);
  }

}
