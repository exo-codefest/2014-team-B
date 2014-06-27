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

import org.exoplatform.commons.utils.CommonsUtils;
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

  private String taskId = "";

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
    addUIFormInput(new UIFormSelectBox(FIELD_ASSIGNEE, FIELD_ASSIGNEE, null));
    //
    UIFormSelectBox selectBox = new UIFormSelectBox(FIELD_GROUP, FIELD_GROUP, null);
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
  }

  @Override
  public void deActivate() {
  }

  public String getTaskId() {
    return taskId;
  }

  public UITaskForm setTaskId(String taskId) {
    this.taskId = taskId;
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
      Date dueDate = uiForm.getUIFormDateTimeInput(FIELD_DUE_DATE).getCalendar().getTime();
      UITeamBPortlet teamBPortlet = uiForm.getAncestorOfType(UITeamBPortlet.class);
      teamBPortlet.createCalendarEvent(currentUser, summary, dueDate, groupId, uiForm.getCalendarPriority(priority));
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
