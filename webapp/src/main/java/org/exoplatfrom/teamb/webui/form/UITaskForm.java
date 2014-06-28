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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.exoplatform.addons.codefest.team_b.core.api.TaskManager;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.addons.codefest.team_b.core.utils.TaskManagerUtils;
import org.exoplatform.commons.utils.CommonsUtils;
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
import org.exoplatfrom.teamb.webui.UITeamBPortlet;
import org.exoplatfrom.teamb.webui.Utils;

@ComponentConfigs({
  @ComponentConfig(lifecycle = UIFormLifecycle.class, 
    template = "app:/templates/teamb/webui/form/UITaskForm.gtmpl", 
    events = {
      @EventConfig(listeners = UITaskForm.SaveActionListener.class),
      @EventConfig(listeners = UITaskForm.OnChangeGroupActionListener.class),
      @EventConfig(listeners = UITaskForm.CloseActionListener.class, phase = Phase.DECODE)
  })
})
public class UITaskForm extends BaseUIForm implements UIPopupComponent {

  private Task task = null;

  final public static String FIELD_SUMMARY        = "Summary";
  final public static String FIELD_CREATED_DATE   = "CreatedDate";
  final public static String FIELD_DUE_DATE       = "DueDate";
  final public static String FIELD_COMPLETED_DATE = "CompletedDate";
  final public static String FIELD_DESCRIPTION    = "Description";
  final public static String FIELD_PRIORITY       = "Priority";
  final public static String FIELD_GROUP          = "Group";
  final public static String FIELD_REPORTER       = "Reporter";
  final public static String FIELD_ASSIGNEE       = "Assignee";
  final public static String FIELD_BV             = "BV";
  final public static String FIELD_COMPLETENESS   = "Completeness";
  
  public UITaskForm() throws Exception {
    addUIFormInput(new UIFormStringInput(FIELD_SUMMARY, FIELD_SUMMARY, null));
    addUIFormInput(new UIFormTextAreaInput(FIELD_DESCRIPTION, FIELD_DESCRIPTION, null)) ;
    
    List<SelectItemOption<String>> list = new ArrayList<SelectItemOption<String>>();
    list.add(new SelectItemOption<String>(getLabel("Blocker"), "Blocker"));
    list.add(new SelectItemOption<String>(getLabel("Critical"), "Critical"));
    list.add(new SelectItemOption<String>(getLabel("Major"), "Major"));
    list.add(new SelectItemOption<String>(getLabel("Minor"), "Minor"));
    list.add(new SelectItemOption<String>(getLabel("Trivial"), "Trivial"));
    addUIFormInput(new UIFormSelectBox(FIELD_PRIORITY, FIELD_PRIORITY, list));
    //
    
    
    addUIFormInput(new UIFormStringInput(FIELD_REPORTER, FIELD_REPORTER, null));
    List<SelectItemOption<String>> assigneeList = new ArrayList<SelectItemOption<String>>();
    addUIFormInput(new UIFormSelectBox(FIELD_ASSIGNEE, FIELD_ASSIGNEE, assigneeList));
    //
    List<SelectItemOption<String>> groupList = new ArrayList<SelectItemOption<String>>();
    UIFormSelectBox selectBox = new UIFormSelectBox(FIELD_GROUP, FIELD_GROUP, groupList);
    selectBox.setOnChange("OnChangeGroup");
    addUIFormInput(selectBox);
    
    addUIFormInput(new UIFormStringInput(FIELD_BV, FIELD_BV, null));
    addUIFormInput(new UIFormDateTimeInput(FIELD_DUE_DATE, FIELD_DUE_DATE, null, false));
    addUIFormInput(new UIFormDateTimeInput(FIELD_COMPLETED_DATE, FIELD_COMPLETED_DATE, null, false));
    addUIFormInput(new UIFormStringInput(FIELD_COMPLETENESS, FIELD_COMPLETENESS, null));
    
    setActions(new String[]{"Save", "Close"});
  }

  @Override
  public void activate() {
  }
  
  public void setGroups(List<SelectItemOption<String>> list) {
    getUIFormSelectBox(FIELD_GROUP).setOptions(list);
  }
  
  public void initForm() {
    Task selectedTask = TaskManagerUtils.getTaskById(this.task.getValue(TaskEntity.id));
    if (selectedTask != null) {
      
      //
      getUIStringInput(FIELD_SUMMARY).setValue(selectedTask.getValue(TaskEntity.title));
      getUIFormTextAreaInput(FIELD_DESCRIPTION).setValue(selectedTask.getValue(TaskEntity.description));
      
      getUIFormSelectBox(FIELD_PRIORITY).setSelectedValues(new String[] {selectedTask.getValue(TaskEntity.priority)});
      
      String reporterId = selectedTask.getValue(TaskEntity.reporterId);
      Identity reporterIdentity = Utils.getIdentityById(reporterId);
      
      getUIStringInput(FIELD_REPORTER).setValue(reporterIdentity.getProfile().getFullName());
      getUIStringInput(FIELD_REPORTER).setDisabled(true);
      
      getUIFormSelectBox(FIELD_ASSIGNEE).setSelectedValues(new String[] {selectedTask.getValue(TaskEntity.assigneeId)});
      
      //
      getUIFormSelectBox(FIELD_GROUP).setSelectedValues(new String[] {selectedTask.getValue(TaskEntity.groupId)});
      
      //
      
      getUIStringInput(FIELD_BV).setValue(selectedTask.getValue(TaskEntity.businessValue) + "");
      
      getUIFormDateTimeInput(FIELD_DUE_DATE).setValue(selectedTask.getValue(TaskEntity.estimation));
      getUIFormDateTimeInput(FIELD_COMPLETED_DATE).setValue(selectedTask.getValue(TaskEntity.resolvedTime) + "");
     
      getUIStringInput(FIELD_BV).setValue(selectedTask.getValue(TaskEntity.workLogged));
    }
  }
  
  public void initForm(String currentUser) {
    getUIStringInput(FIELD_REPORTER).setValue(currentUser);
    getUIStringInput(FIELD_REPORTER).setDisabled(true);
    
    String groupId = getUIFormSelectBox(FIELD_GROUP).getValue();
    SpaceService spaceService = CommonsUtils.getService(SpaceService.class);
    Space space = spaceService.getSpaceByGroupId(groupId);
    List<SelectItemOption<String>> list = new ArrayList<SelectItemOption<String>>();
    if (spaceService.isManager(space, currentUser)) {
      for (String member : space.getMembers()) {
        list.add(new SelectItemOption<String>(member, member));
      }
      getUIFormSelectBox(FIELD_ASSIGNEE).setOptions(list);
    } else {
      String manager = space.getManagers()[0];
      list.add(new SelectItemOption<String>(manager, manager));
      getUIFormSelectBox(FIELD_ASSIGNEE).setOptions(list);
      getUIFormSelectBox(FIELD_ASSIGNEE).setDisabled(true);
    }
    
    //
    getUIStringInput(FIELD_SUMMARY).setValue(this.task.getValue(TaskEntity.title));
    getUIFormTextAreaInput(FIELD_DESCRIPTION).setValue(this.task.getValue(TaskEntity.description));
    getUIStringInput(FIELD_BV).setValue(this.task.getValue(TaskEntity.businessValue) + "");
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
  
  private String getCalendarPriority(String priority) {
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
      String reporter = uiForm.getUIStringInput(FIELD_REPORTER).getValue();
      String description = uiForm.getUIFormTextAreaInput(FIELD_DESCRIPTION).getValue();
      long bv = Long.parseLong(uiForm.getUIStringInput(FIELD_BV).getValue());
      Date dueDate = uiForm.getUIFormDateTimeInput(FIELD_DUE_DATE).getCalendar().getTime();
      UITeamBPortlet teamBPortlet = uiForm.getAncestorOfType(UITeamBPortlet.class);
      teamBPortlet.createCalendarEvent(currentUser, summary, dueDate, groupId, uiForm.getCalendarPriority(priority));
      
      //
      IdentityManager idm = CommonsUtils.getService(IdentityManager.class);
      Identity reporterIdentity = idm.getOrCreateIdentity(OrganizationIdentityProvider.NAME, reporter, false);
      String assigneeId = idm.getOrCreateIdentity(OrganizationIdentityProvider.NAME, assignee, false).getId();
      
      //
      TaskManager tm = CommonsUtils.getService(TaskManager.class);
      Task task = new Task(); 
      task.setValue(TaskEntity.title, summary);
      task.setValue(TaskEntity.reporterId, reporterIdentity.getId());
      task.setValue(TaskEntity.assigneeId, assigneeId);
      task.setValue(TaskEntity.groupId, groupId);
      task.setValue(TaskEntity.priority, priority);
      task.setValue(TaskEntity.estimation, dueDate.getTime() + "");
      task.setValue(TaskEntity.businessValue, bv);
      task.setValue(TaskEntity.description, description);
      tm.save(reporterIdentity, task);
      
      teamBPortlet.cancelAction();
    }
  }

  static public class OnChangeGroupActionListener extends EventListener<UITaskForm> {
    public void execute(Event<UITaskForm> event) throws Exception {
      String currentUser = event.getRequestContext().getRemoteUser();
      //
      UITeamBPortlet teamBPortlet = event.getSource().getAncestorOfType(UITeamBPortlet.class);
      
      teamBPortlet.cancelAction();
    }
  }
}
