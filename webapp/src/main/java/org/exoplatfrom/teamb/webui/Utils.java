/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU Affero General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatfrom.teamb.webui;

import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

public class Utils {
  public static final String SUMMARY = "Summary";
  public static final String REPORTER = "Reporter";
  public static final String ASSIGNEE = "Assignee";
  public static final String PRIORITY = "Priority";
  public static final String BV = "BV";
  public static final String STATUS = "Status";
  public static final String GROUP = "Group";
  public static final String CREATED_DATE = "CreatedDate";
  public static final String DUE_DATE = "DueDate";
  public static final String COMPLETENESS = "Completeness";
  public static final String COMPLETED_DATE = "CompletedDate";
  public static final String DESCRIPTION = "Description";
  public static final String NOTE = "Note";
  
  
  public static Task fillPropertiesTask(Task task) {
    if (task == null) return null;
    
    // get reporter info
    IdentityManager idm = CommonsUtils.getService(IdentityManager.class);
    SpaceService ss = CommonsUtils.getService(SpaceService.class);
    
    Identity reporter = idm.getIdentity((String)task.get(TaskEntity.reporterId), false);
    
    // get assignee info
    Identity assignee = idm.getIdentity((String)task.get(TaskEntity.assigneeId), false);
    
    // get group info
    Space sp = ss.getSpaceByGroupId((String)task.get(TaskEntity.groupId));
    
    Task t = new Task();
    t.put(SUMMARY, (String)task.get(TaskEntity.title));
    t.put(REPORTER, reporter.getProfile().getFullName());
    t.put(ASSIGNEE, assignee.getProfile().getFullName());
    t.put(PRIORITY, (String)task.get(TaskEntity.priority));
    t.put(BV, (String)task.get(TaskEntity.businessValue));
    t.put(STATUS, (String)task.get(TaskEntity.status));
    t.put(GROUP, sp.getDisplayName());
    t.put(COMPLETENESS, (String)task.get(TaskEntity.workLogged));
    t.put(CREATED_DATE, (String)task.get(TaskEntity.createdTime));
    t.put(DUE_DATE, (String)task.get(TaskEntity.resolvedTime));
    t.put(COMPLETED_DATE, (String)task.get(TaskEntity.resolvedTime));
    t.put(DESCRIPTION, (String)task.get(TaskEntity.description));
    t.put(NOTE,(String)task.get(TaskEntity.resolution));
        
    return t;
  }
  
}