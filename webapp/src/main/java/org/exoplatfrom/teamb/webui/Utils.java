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

import java.util.regex.Pattern;

import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.addons.codefest.team_b.core.utils.TaskManagerUtils;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

public class Utils {
  public static final String ID = "id";
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
    
    Identity reporter = idm.getIdentity(task.getValue(TaskEntity.reporterId), false);
    
    // get assignee info
    Identity assignee = idm.getIdentity(task.getValue(TaskEntity.assigneeId), false);
    
    // get group info
    Space sp = ss.getSpaceByGroupId(task.getValue(TaskEntity.groupId));
    
    Task t = new Task();
    t.put(ID, task.getValue(TaskEntity.id));
    t.put(SUMMARY, task.getValue(TaskEntity.title));
    t.put(REPORTER, reporter.getProfile().getFullName());
    t.put(ASSIGNEE, assignee.getProfile().getFullName());
    t.put(PRIORITY, task.getValue(TaskEntity.priority));
    t.put(BV, task.getValue(TaskEntity.businessValue));
    t.put(STATUS, task.getValue(TaskEntity.status));
    t.put(GROUP, sp.getDisplayName());
    t.put(COMPLETENESS, task.getValue(TaskEntity.completeness));
    t.put(CREATED_DATE,  task.getValue(TaskEntity.createdTime));
    t.put(DUE_DATE,  task.getValue(TaskEntity.dueDateTime));
    t.put(COMPLETED_DATE, task.getValue(TaskEntity.resolvedTime));
    t.put(DESCRIPTION, task.getValue(TaskEntity.description));
    t.put(NOTE, task.getValue(TaskEntity.resolution));
        
    return t;
  }
  
  public static Identity getIdentityById(String id) {
    IdentityManager idm = CommonsUtils.getService(IdentityManager.class);
    return idm.getIdentity(id, false);
  }

  public static Identity getIdentityByRemoteId(String remoteId) {
    IdentityManager idm = CommonsUtils.getService(IdentityManager.class);
    return idm.getOrCreateIdentity(OrganizationIdentityProvider.NAME, remoteId, false);
  }
  
  private static Pattern weekMatcher   = Pattern.compile("([0-9]w)+$");
  private static Pattern dayMatcher    = Pattern.compile("([0-9]d)+$");
  private static Pattern hourMatcher   = Pattern.compile("([0-9]h)+$");
  private static Pattern minuteMatcher = Pattern.compile("([0-9]m)+$");

  /**
   * Get value time by from 1w 3d 9h 40m to minutes
   * 
   * @param input The input by format 1w 3d 9h 40m
   * @return The mitutes
   */
  public static int getTimeValue(String input) {
    if (input == null || input.trim().length() == 0) {
      return 0;
    }
    input = input.toLowerCase().trim();
    String[] values = input.split(" ");
    int m = 0;
    for (int i = 0; i < values.length; i++) {
      String v = values[i];
      if (weekMatcher.matcher(v).find()) {
        m += Integer.valueOf(v.replace("w", "")) * 5 * 8 * 60;
      } else if (dayMatcher.matcher(v).find()) {
        m += Integer.valueOf(v.replace("d", "")) * 8 * 60;
      } else if (hourMatcher.matcher(v).find()) {
        m += Integer.valueOf(v.replace("h", "")) * 60;
      } else if (minuteMatcher.matcher(v).find()) {
        m += Integer.valueOf(v.replace("m", ""));
      }
    }
    return m;
  }
  
  public static String getUserDisplayName(String userName) {
    UserHandler userHandler = CommonsUtils.getService(OrganizationService.class).getUserHandler();
    try {
      User user = userHandler.findUserByName(userName);
      userName = user.getDisplayName();
      if (TaskManagerUtils.isEmpty(userName)) {
        return user.getFirstName() + " " + user.getLastName();
      }
    } catch (Exception e) {}
    return userName;
  }

  public static String getGroupDisplayName(String groupId) {
    GroupHandler groupHandler = CommonsUtils.getService(OrganizationService.class).getGroupHandler();
    try {
      Group user = groupHandler.findGroupById(groupId);
      return user.getLabel();
    } catch (Exception e) {
    }
    return groupId;
  }
}
