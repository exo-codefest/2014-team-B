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
package org.exoplatform.addons.codefest.team_b.core.data;

import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.affectVersion;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.assigneeId;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.businessValue;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.createdTime;
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
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.type;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.updatedTime;
import static org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity.workLogged;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.addons.codefest.team_b.core.api.TaskManager;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.model.Identity;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 18, 2014  
 */
public class TaskDataBuilder {
  private final Log LOG = ExoLogger.getLogger(TaskDataBuilder.class);
  private String description;
  private Identity assignee;
  private Identity reporter;
  private int numberOfTasks;
  
  public static TaskDataBuilder initOne(Identity reporter) {
    return initMore(1, null, reporter, null);
  }
  
  public static TaskDataBuilder initOne(String description, Identity reporter) {
    return initMore(1, description, reporter, null);
  }
  
  public static TaskDataBuilder initMore(int number, Identity reporter) {
    return initMore(number, null, reporter, null);
  }
  
  public static TaskDataBuilder initMore(int number, String title, Identity reporter) {
    return initMore(number, title, reporter, null);
  }
  
  public static TaskDataBuilder initMore(int number, String description, Identity reporter, Identity assignee) {
    return new Builder().description(description).reporter(reporter).assignee(assignee).number(number).build();
  }
  
  public TaskDataBuilder(Builder builder) {
    this.description = builder.description;
    
    if (this.description == null) {
      this.description = "Can not access to Space home page";
    }
    
    this.reporter = builder.reporter;
    this.assignee = builder.assignee;
    this.numberOfTasks = builder.numberOfActivity;
  }
  
  public List<Task> inject() {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);
    List<Task> list = new ArrayList<Task>(numberOfTasks);
    for (int i = 0; i < numberOfTasks; i++) {
      Task task = new Task();
      task.setValue(taskId, "" + i);
      task.setValue(TaskEntity.title, "Task title " + i);
      if (assignee != null) {
        task.setValue(assigneeId, assignee.getId());
      }
      task.setValue(reporterId, reporter.getId());
      task.setValue(type, "Bug");
      task.setPriority(Task.PRIORITY.MAJOR.getValue());
      task.setValue(affectVersion, "4.1-M1");
      task.setValue(fixVersion, "4.1-RC1");
      task.setValue(businessValue, 68L + i);
      task.setValue(status, "Open");
      task.setValue(resolution, 1L);
      task.setValue(TaskEntity.description, description);
      task.setValue(createdTime, System.currentTimeMillis());
      task.setValue(updatedTime, System.currentTimeMillis());
      task.setValue(resolvedTime, -1L);
      task.setValue(estimation, "2d");
      task.setValue(remaining, "2d");
      task.setValue(workLogged, "0d");
      task.setValue(groupId, "/spaces/space_1");
      task.setValue(linkUrl, "https://jira.exoplatform.org/browse/SOC-4316");

      try {
        task = manager.save(reporter, task);
        list.add(task);
      } catch (Exception e) {
        LOG.error("can not save activity.", e);
      }
    }
    
    return list;
  }
  
  public Task injectOne() {
    TaskManager manager = CommonsUtils.getService(TaskManager.class);

    Task task = new Task();

    task.setValue(taskId, "1");
    task.setValue(TaskEntity.title, "Task title");
    if (assignee != null) {
      task.setValue(assigneeId, assignee.getId());
    }
    if (reporter != null) {
      task.setValue(reporterId, reporter.getId());
    }
    task.setValue(type, "Bug");
    task.setPriority(Task.PRIORITY.MAJOR.getValue());
    task.setValue(affectVersion, "4.1-M1");
    task.setValue(fixVersion, "4.1-RC1");
    task.setValue(businessValue, 68L);
    task.setValue(status, "Open");
    task.setValue(resolution, 1L);
    task.setValue(TaskEntity.description, "Can not access to Space home page");
    task.setValue(createdTime, System.currentTimeMillis());
    task.setValue(updatedTime, System.currentTimeMillis());
    task.setValue(resolvedTime, -1L);
    task.setValue(estimation, "2d");
    task.setValue(remaining, "2d");
    task.setValue(workLogged, "0d");
    task.setValue(groupId, "/spaces/space_1");
    task.setValue(linkUrl, "https://jira.exoplatform.org/browse/SOC-4316");

    try {
      task = manager.save(reporter, task);
    } catch (Exception e) {
      LOG.error("can not save activity.", e);
    }

    return task;
  }
  
  public Task createOne() {

    Task task = new Task();

    task.setValue(taskId, "1");
    task.setValue(TaskEntity.title, "Task title");
    task.setValue(assigneeId, assignee.getId());
    task.setValue(reporterId, reporter.getId());
    task.setValue(type, "Bug");
    task.setPriority(Task.PRIORITY.MAJOR.getValue());
    task.setValue(affectVersion, "4.1-M1");
    task.setValue(fixVersion, "4.1-RC1");
    task.setValue(businessValue, 68L);
    task.setValue(status, "Open");
    task.setValue(resolution, 1L);
    task.setValue(TaskEntity.description, "Can not access to Space home page");
    task.setValue(createdTime, System.currentTimeMillis());
    task.setValue(updatedTime, System.currentTimeMillis());
    task.setValue(resolvedTime, -1L);
    task.setValue(estimation, "2d");
    task.setValue(remaining, "2d");
    task.setValue(workLogged, "0d");
    task.setValue(groupId, "/spaces/space_1");
    task.setValue(linkUrl, "https://jira.exoplatform.org/browse/SOC-4316");


    return task;
  }
  
  public static class Builder {
    public String description = null;
    public Identity assignee;
    public Identity reporter;
    public int numberOfActivity = 1;
    
    public Builder description(String description) {
      this.description = description;
      return this;
    }
    
    public Builder assignee(Identity assignee) {
      this.assignee = assignee;
      return this;
    }
    
    public Builder reporter(Identity reporter) {
      this.reporter = reporter;
      return this;
    }
    
    public Builder number(int number) {
      this.numberOfActivity = number;
      return this;
    }
    
    public TaskDataBuilder build() {
      return new TaskDataBuilder(this);
    }
    
  }
}
