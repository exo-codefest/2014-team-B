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
package org.exoplatform.addons.codefest.team_b.core.chromattic.entity;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
import org.chromattic.api.annotations.FormattedBy;
import org.chromattic.api.annotations.Id;
import org.chromattic.api.annotations.Name;
import org.chromattic.api.annotations.NamingPrefix;
import org.chromattic.api.annotations.Path;
import org.chromattic.api.annotations.PrimaryType;
import org.chromattic.api.annotations.Property;
import org.chromattic.ext.format.BaseEncodingObjectFormatter;
import org.exoplatform.addons.codefest.team_b.core.storage.PropertyLiteralExpression;

@PrimaryType(name = "exo:taskdefinition", orderable = true)
@FormattedBy(BaseEncodingObjectFormatter.class)
@NamingPrefix("exo")
public abstract class TaskEntity implements NamedEntity {

  @Id
  public abstract String getId();

  @Name
  public abstract String getName();
  
  @Path
  public abstract String getPath();
  
  
  public static final PropertyLiteralExpression<String> id =
      new PropertyLiteralExpression<String>(String.class, "uuid", "id");
  
  /**
   * The activityId of the task
   */
  @Property(name = "exo:activityId")
  public abstract String getActivityId();
  public abstract void setActivityId(String activityId);
  public static final PropertyLiteralExpression<String> activityId =
    new PropertyLiteralExpression<String>(String.class, "exo:activityId", "activityId");
  
  /**
   * The taskId of the task
   */
  @Property(name = "exo:title")
  public abstract String getTitle();
  public abstract void setTitle(String title);
  public static final PropertyLiteralExpression<String> title =
    new PropertyLiteralExpression<String>(String.class, "exo:title", "title");

  /**
   * The taskId of the task
   */
  @Property(name = "exo:taskId")
  public abstract String getTaskId();
  public abstract void setTaskId(String taskId);
  public static final PropertyLiteralExpression<String> taskId =
    new PropertyLiteralExpression<String>(String.class, "exo:taskId", "taskId");
  
  /**
   * The assigneeId of the task
   */
  @Property(name = "exo:assigneeId")
  public abstract String getAssigneeId();
  public abstract void setAssigneeId(String assigneeId);
  public static final PropertyLiteralExpression<String> assigneeId =
    new PropertyLiteralExpression<String>(String.class, "exo:assigneeId", "assigneeId");

  /**
   * The reporterId of the task
   */
  @Property(name = "exo:reporterId")
  public abstract String getReporterId();
  public abstract void setReporterId(String reporterId);
  public static final PropertyLiteralExpression<String> reporterId =
    new PropertyLiteralExpression<String>(String.class, "exo:reporterId", "reporterId");
  
  /**
   * The type of the task
   * BUG, IMPROVEMENT, TASK
   */
  @Property(name = "exo:type")
  public abstract String getType();
  public abstract void setType(String type);
  public static final PropertyLiteralExpression<String> type =
    new PropertyLiteralExpression<String>(String.class, "exo:type", "type");
  
  /**
   * The type of the task
   * BLOCKER, CRITICAL, MAJOR, MINNOR
   */
  @Property(name = "exo:priority")
  public abstract Integer getPriority();
  public abstract void setPriority(Integer priority);
  public static final PropertyLiteralExpression<String> priority =
    new PropertyLiteralExpression<String>(String.class, "exo:priority", "priority");
  
  @Property(name = "exo:affectVersion")
  public abstract String getAffectVersion();
  public abstract void setAffectVersion(String affectVersion);
  public static final PropertyLiteralExpression<String> affectVersion =
    new PropertyLiteralExpression<String>(String.class, "exo:affectVersion", "affectVersin");
  
  @Property(name = "exo:fixVersion")
  public abstract String getFixVersion();
  public abstract void setFixVersion(String fixVersion);
  public static final PropertyLiteralExpression<String> fixVersion =
    new PropertyLiteralExpression<String>(String.class, "exo:fixVersion", "fixVersion");
  
  @Property(name = "exo:businessValue")
  public abstract Long getBusinessValue();
  public abstract void setBusinessValue(Long businessValue);
  public static final PropertyLiteralExpression<Long> businessValue =
    new PropertyLiteralExpression<Long>(Long.class, "exo:businessValue", "businessValue");
  
  @Property(name = "exo:status")
  public abstract String getStatus();
  public abstract void setStatus(String status);
  public static final PropertyLiteralExpression<String> status =
    new PropertyLiteralExpression<String>(String.class, "exo:status", "status");
  
  @Property(name = "exo:resolution")
  public abstract Long getResolution();
  public abstract void setResolution(Long resolution);
  public static final PropertyLiteralExpression<Long> resolution =
    new PropertyLiteralExpression<Long>(Long.class, "exo:resolution", "resolution");
  
  @Property(name = "exo:description")
  public abstract String getDescription();
  public abstract void setDescription(String description);
  public static final PropertyLiteralExpression<String> description =
    new PropertyLiteralExpression<String>(String.class, "exo:description", "description");
  
  @Property(name = "exo:createdTime")
  public abstract Long getCreatedTime();
  public abstract void setCreatedTime(Long createdTime);
  public static final PropertyLiteralExpression<Long> createdTime =
    new PropertyLiteralExpression<Long>(Long.class, "exo:createdTime", "createdDate");
  
  @Property(name = "exo:updatedTime")
  public abstract Long getUpdatedTime();
  public abstract void setUpdatedTime(Long updatedTime);
  public static final PropertyLiteralExpression<Long> updatedTime =
    new PropertyLiteralExpression<Long>(Long.class, "exo:updatedTime", "updatedTime");
  
  @Property(name = "exo:resolvedTime")
  public abstract Integer getResolvedTime();
  public abstract void setResolvedTime(Integer resolvedTime);
  public static final PropertyLiteralExpression<Integer> resolvedTime =
    new PropertyLiteralExpression<Integer>(Integer.class, "exo:resolvedTime", "resolvedTime");
  
  @Property(name = "exo:estimation")
  public abstract String getEstimation();
  public abstract void setEstimation(String estimation);
  public static final PropertyLiteralExpression<String> estimation =
    new PropertyLiteralExpression<String>(String.class, "exo:estimation", "estimation");
  
  @Property(name = "exo:remaining")
  public abstract String getRemaining();
  public abstract void setRemaining(String remanining);
  public static final PropertyLiteralExpression<String> remaining =
    new PropertyLiteralExpression<String>(String.class, "exo:remaining", "remaining");
  
  @Property(name = "exo:workLogged")
  public abstract String getWorkLogged();
  public abstract void setWorkLogged(String workLogged);
  public static final PropertyLiteralExpression<String> workLogged =
    new PropertyLiteralExpression<String>(String.class, "exo:workLogged", "workLogged");
  
  @Property(name = "exo:groupId")
  public abstract String getGroupId();
  public abstract void setGroupId(String groupId);
  public static final PropertyLiteralExpression<String> groupId =
    new PropertyLiteralExpression<String>(String.class, "exo:groupId", "groupId");
  
  @Property(name = "exo:linkUrl")
  public abstract String getLinkUrl();
  public abstract void setLinkUrl(String linkUrl);
  public static final PropertyLiteralExpression<String> linkUrl =
    new PropertyLiteralExpression<String>(String.class, "exo:linkUrl", "linkUrl");
  
}