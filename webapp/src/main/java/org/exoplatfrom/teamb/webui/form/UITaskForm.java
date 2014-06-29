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
package org.exoplatfrom.teamb.webui.form;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.exoplatform.addons.codefest.team_b.core.api.TaskManager;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.MembershipEntry;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormDateTimeInput;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTextAreaInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;
import org.exoplatfrom.teamb.webui.UITeamBPortlet;
import org.exoplatfrom.teamb.webui.Utils;

@ComponentConfigs({
  @ComponentConfig(lifecycle = UIFormLifecycle.class, 
    template = "app:/templates/teamb/webui/form/UITaskForm.gtmpl", 
    events = {
      @EventConfig(listeners = UITaskForm.SaveActionListener.class),
      @EventConfig(listeners = UITaskForm.OnChangeGroupActionListener.class, phase = Phase.DECODE),
      @EventConfig(listeners = UITaskForm.CloseActionListener.class, phase = Phase.DECODE)
  })
})
public class UITaskForm extends BaseUIForm implements UIPopupComponent {

  private Task task = null;
  private String reporterIdentityId = null;

  final public static String FIELD_SUMMARY        = "Summary";
  final public static String FIELD_CREATED_DATE   = "CreatedDate";
  final public static String FIELD_DUE_DATE       = "DueDate";
//  final public static String FIELD_COMPLETED_DATE = "CompletedDate";
  final public static String FIELD_DESCRIPTION    = "Description";
  final public static String FIELD_PRIORITY       = "Priority";
  final public static String FIELD_GROUP          = "Group";
  final public static String FIELD_REPORTER       = "Reporter";
  final public static String FIELD_ASSIGNEE       = "Assignee";
  final public static String FIELD_BV             = "BV";
  final public static String FIELD_ESTIMATION     = "estimation";
  
  public UITaskForm() throws Exception {
    addUIFormInput(new UIFormStringInput(FIELD_SUMMARY, FIELD_SUMMARY, null).addValidator(MandatoryValidator.class));
    addUIFormInput(new UIFormTextAreaInput(FIELD_DESCRIPTION, FIELD_DESCRIPTION, null)) ;
    
    List<SelectItemOption<String>> list = new ArrayList<SelectItemOption<String>>();
    list.add(new SelectItemOption<String>(getLabel(Task.PRIORITY.BLOCKER.getName()), Task.PRIORITY.BLOCKER.getName()));
    list.add(new SelectItemOption<String>(getLabel(Task.PRIORITY.CRITICAL.getName()), Task.PRIORITY.CRITICAL.getName()));
    list.add(new SelectItemOption<String>(getLabel(Task.PRIORITY.MAJOR.getName()), Task.PRIORITY.MAJOR.getName()));
    list.add(new SelectItemOption<String>(getLabel(Task.PRIORITY.MINOR.getName()), Task.PRIORITY.MINOR.getName()));
    list.add(new SelectItemOption<String>(getLabel(Task.PRIORITY.TRIVIAL.getName()), Task.PRIORITY.TRIVIAL.getName()));
    addUIFormInput(new UIFormSelectBox(FIELD_PRIORITY, FIELD_PRIORITY, list));
    //
    
    
    addUIFormInput(new UIFormStringInput(FIELD_REPORTER, FIELD_REPORTER, null).addValidator(MandatoryValidator.class));
    List<SelectItemOption<String>> assigneeList = new ArrayList<SelectItemOption<String>>();
    addUIFormInput(new UIFormSelectBox(FIELD_ASSIGNEE, FIELD_ASSIGNEE, assigneeList));
    //
    List<SelectItemOption<String>> groupList = new ArrayList<SelectItemOption<String>>();
    UIFormSelectBox selectBox = new UIFormSelectBox(FIELD_GROUP, FIELD_GROUP, groupList);
    selectBox.setOnChange("OnChangeGroup");
    addUIFormInput(selectBox);
    
    addUIFormInput(new UIFormStringInput(FIELD_BV, FIELD_BV, null).addValidator(MandatoryValidator.class));
    addUIFormInput(new UIFormStringInput(FIELD_ESTIMATION, FIELD_ESTIMATION, null).addValidator(MandatoryValidator.class));
    addUIFormInput(new UIFormDateTimeInput(FIELD_DUE_DATE, FIELD_DUE_DATE, null, false));
    
    setActions(new String[]{"Save", "Close"});
  }

  @Override
  public void activate() {
  }
  
  public void setGroups(List<SelectItemOption<String>> list) {
    getUIFormSelectBox(FIELD_GROUP).setOptions(list);
  }
  
  public void initForm() {
    boolean isAddingNew = (this.task == null);
    List<SelectItemOption<String>> grougList = new ArrayList<SelectItemOption<String>>();
    org.exoplatform.services.security.Identity identity = ConversationState.getCurrent().getIdentity();
    String currentUser = identity.getUserId();
    String reporterId = currentUser;
    String groupId = "";
    if (!isAddingNew) {
      this.reporterIdentityId = this.task.getValue(TaskEntity.reporterId);
      reporterId = Utils.getIdentityById(this.reporterIdentityId).getRemoteId();
      
      //
      groupId = this.task.getValue(TaskEntity.groupId);
      grougList.add(new SelectItemOption<String>(Utils.getGroupDisplayName(groupId), groupId));
      UIFormSelectBox selectBox = getUIFormSelectBox(FIELD_GROUP).setOptions(grougList);
      selectBox.setValue(groupId);
      selectBox.setDisabled(true);
    } else {
      List<String> spaces = new ArrayList<String>();
      for (MembershipEntry membership : identity.getMemberships()) {
        String gr = membership.getGroup();
        if (gr.startsWith("/spaces") && ! spaces.contains(gr)) {
          grougList.add(new SelectItemOption<String>(Utils.getGroupDisplayName(gr), gr));
          spaces.add(gr);
        }
      }
      groupId = spaces.get(0);
      getUIFormSelectBox(FIELD_GROUP).setOptions(grougList).setValue(groupId);
    }

    getUIStringInput(FIELD_REPORTER).setValue(Utils.getUserDisplayName(reporterId));
    getUIStringInput(FIELD_REPORTER).setDisabled(true);

    SpaceService spaceService = CommonsUtils.getService(SpaceService.class);
    Space space = spaceService.getSpaceByGroupId(groupId);
    List<SelectItemOption<String>> list = new ArrayList<SelectItemOption<String>>();
    // check with currentUser
    if (spaceService.isManager(space, currentUser)) {
      for (String member : space.getMembers()) {
        list.add(new SelectItemOption<String>(Utils.getUserDisplayName(member), member));
      }
      getUIFormSelectBox(FIELD_ASSIGNEE).setOptions(list);
    } else {
      String manager = space.getManagers()[0];
      list.add(new SelectItemOption<String>(Utils.getUserDisplayName(manager), manager));
      getUIFormSelectBox(FIELD_ASSIGNEE).setOptions(list).setDisabled(true);
    }
    
    if (this.task != null) {
      //
      getUIStringInput(FIELD_SUMMARY).setValue(this.task.getValue(TaskEntity.title));
      getUIFormTextAreaInput(FIELD_DESCRIPTION).setValue(this.task.getValue(TaskEntity.description));
      
      getUIFormSelectBox(FIELD_PRIORITY).setValue(Task.getPriorityByValue(this.task.getValue(TaskEntity.priority)).getName());
      
      getUIFormSelectBox(FIELD_ASSIGNEE).setValue(this.task.getValue(TaskEntity.assigneeId));
      
      //
      getUIStringInput(FIELD_BV).setValue(this.task.getValue(TaskEntity.businessValue) + "");
      
      
      getUIStringInput(FIELD_ESTIMATION).setValue(String.valueOf(this.task.getValue(TaskEntity.estimation)));
      
      if (this.task.getValue(TaskEntity.dueDateTime) != null) {  
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.task.getValue(TaskEntity.dueDateTime));
        getUIFormDateTimeInput(FIELD_DUE_DATE).setValue(formatter.format(calendar.getTime()));
      }
    }
  }

  @Override
  public void deActivate() {
  }

  public Task getTask() {
    return task;
  }

  public UITaskForm setTask(Task task) {
    this.task = task;
    return this;
  }
  
  private static String getCalendarPriority(String priority) {
    if ("Blocker".equals(priority) || "Critical".equals(priority)) {
      return "High";
    } else if ("Minor".equals(priority) || "Trivial".equals(priority)) {
      return "Low";
    } else {
      return "Normal";
    }
  }

  static public class SaveActionListener extends EventListener<UITaskForm> {
    public void execute(Event<UITaskForm> event) throws Exception {
      String currentUser = event.getRequestContext().getRemoteUser();
      //
      UITaskForm uiForm = event.getSource();
      String groupId = uiForm.getUIFormSelectBox(FIELD_GROUP).getValue();
      String summary = uiForm.getUIStringInput(FIELD_SUMMARY).getValue();
      String priority = uiForm.getUIStringInput(FIELD_PRIORITY).getValue();
      String assignee = uiForm.getUIStringInput(FIELD_ASSIGNEE).getValue();
      String estimation = uiForm.getUIStringInput(FIELD_ESTIMATION).getValue();
      String description = uiForm.getUIFormTextAreaInput(FIELD_DESCRIPTION).getValue();
      long bv = Long.parseLong(uiForm.getUIStringInput(FIELD_BV).getValue());
      Calendar dueDate = uiForm.getUIFormDateTimeInput(FIELD_DUE_DATE).getCalendar();
      if(dueDate == null) {
        dueDate = Calendar.getInstance();
      }
      dueDate.set(Calendar.HOUR_OF_DAY, 0);
      dueDate.set(Calendar.MINUTE, 2);
      
      IdentityManager idm = CommonsUtils.getService(IdentityManager.class);
      Identity currentIdentity = idm.getOrCreateIdentity(OrganizationIdentityProvider.NAME, currentUser, false);

      UITeamBPortlet teamBPortlet = uiForm.getAncestorOfType(UITeamBPortlet.class);
      Task task = uiForm.task;
      if (uiForm.task == null) {
        CalendarEvent calendarEvent = teamBPortlet.createCalendarEvent(currentUser, summary, dueDate.getTime(),
                                                                       groupId, getCalendarPriority(priority));
        task = new Task();
        task.setTaskId(calendarEvent.getId());
        task.setValue(TaskEntity.reporterId, currentIdentity.getId());
        task.setValue(TaskEntity.groupId, groupId);
        
        Calendar currentTime = Calendar.getInstance();
        task.setValue(TaskEntity.createdTime, currentTime.getTimeInMillis());
        task.setValue(TaskEntity.updatedTime, currentTime.getTimeInMillis());
        task.setValue(TaskEntity.status, Task.STATUS.OPEN.getName());
      } else {
        teamBPortlet.updateCalendarEvent(currentUser, summary, dueDate.getTime(), groupId,
                                         getCalendarPriority(priority), uiForm.task.getValue(TaskEntity.taskId));
      }
      //
      String assigneeId = idm.getOrCreateIdentity(OrganizationIdentityProvider.NAME, assignee, false).getId();
      //
      TaskManager tm = CommonsUtils.getService(TaskManager.class);
      
      task.setValue(TaskEntity.title, summary);
      task.setValue(TaskEntity.assigneeId, assigneeId);
      task.setValue(TaskEntity.priority, Task.getPriorityByName(priority.trim()));
      task.setValue(TaskEntity.description, description);
      task.setValue(TaskEntity.businessValue, bv);
      task.setValue(TaskEntity.estimation, estimation);
      task.setValue(TaskEntity.dueDateTime, dueDate.getTimeInMillis());
      if (uiForm.task == null) {
        tm.save(currentIdentity, task);
      } else {
        tm.update(currentIdentity, task, TaskEntity.title.getPropertyName(), TaskEntity.estimation.getPropertyName(), 
                  TaskEntity.dueDateTime.getPropertyName(), TaskEntity.priority.getPropertyName(),
                  TaskEntity.businessValue.getPropertyName(), TaskEntity.description.getPropertyName());
      }
      teamBPortlet.cancelAction();
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class OnChangeGroupActionListener extends EventListener<UITaskForm> {
    public void execute(Event<UITaskForm> event) throws Exception {
      String currentUser = event.getRequestContext().getRemoteUser();
      UITaskForm uiForm = event.getSource();

      String selectedGroupId = uiForm.getUIFormSelectBox(FIELD_GROUP).getValue();
      
      SpaceService spaceService = CommonsUtils.getService(SpaceService.class);
      Space space = spaceService.getSpaceByGroupId(selectedGroupId);
      List<SelectItemOption<String>> list = new ArrayList<SelectItemOption<String>>();
      if (spaceService.isManager(space, currentUser)) {
        for (String member : space.getMembers()) {
          list.add(new SelectItemOption<String>(member, member));
        }
        uiForm.getUIFormSelectBox(FIELD_ASSIGNEE).setOptions(list);
      } else {
        String manager = space.getManagers()[0];
        list.add(new SelectItemOption<String>(manager, manager));
        uiForm.getUIFormSelectBox(FIELD_ASSIGNEE).setOptions(list);
        uiForm.getUIFormSelectBox(FIELD_ASSIGNEE).setDisabled(true);
      }
    }
  }
}
