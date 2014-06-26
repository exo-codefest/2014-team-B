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
import java.util.List;

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.MembershipEntry;
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
      @EventConfig(listeners = UITaskForm.CloseActionListener.class, phase = Phase.DECODE)
  })
})
public class UITaskForm extends BaseUIForm implements UIPopupComponent {

  private String taskId = "";
  final public static String FIELD_SUMMARY = "summary".intern() ;
  final public static String FIELD_CREATED_DATE = "createdDate".intern() ;
  final public static String FIELD_DUE_DATE = "dueDate".intern() ;
  final public static String FIELD_COMPLETED_DATE = "completedDate".intern() ;
  final public static String FIELD_DESCRIPTION = "description".intern() ;
  final public static String FIELD_PRIORITY = "priority".intern() ;
  final public static String FIELD_GROUP = "group".intern() ;
  final public static String FIELD_REPORTER = "reporter".intern() ;
  final public static String FIELD_ASSIGNEE = "assignee".intern() ;
  final public static String FIELD_BV= "bv".intern() ;
  final public static String FIELD_COMPLETNESS = "completness".intern() ;
  
  public UITaskForm() throws Exception {
    addUIFormInput(new UIFormStringInput(FIELD_SUMMARY, FIELD_SUMMARY, null));
    addUIFormInput(new UIFormStringInput(FIELD_REPORTER, FIELD_REPORTER, null));
    addUIFormInput(new UIFormStringInput(FIELD_ASSIGNEE, FIELD_ASSIGNEE, null));
    addUIFormInput(new UIFormTextAreaInput(FIELD_DESCRIPTION, FIELD_DESCRIPTION, null)) ;
    
    List<SelectItemOption<String>> list = new ArrayList<SelectItemOption<String>>();
    list.add(new SelectItemOption<String>(getLabel("Blocker"), "Blocker"));
    list.add(new SelectItemOption<String>(getLabel("Critical"), "Critical"));
    list.add(new SelectItemOption<String>(getLabel("Major"), "Major"));
    list.add(new SelectItemOption<String>(getLabel("Minor"), "Minor"));
    list.add(new SelectItemOption<String>(getLabel("Trivial"), "Trivial"));
    addUIFormInput(new UIFormSelectBox(FIELD_PRIORITY, FIELD_PRIORITY, list));
    
    list = new ArrayList<SelectItemOption<String>>();
    ConversationState state = ConversationState.getCurrent();
    Identity identity = state.getIdentity();
    for (MembershipEntry membership : identity.getMemberships()) {
      String gr = membership.getGroup();
      if (gr.startsWith("/spaces")) {
        String space = gr.replace("/spaces/", "");
        list.add(new SelectItemOption<String>(space, space));
      }
    }
    addUIFormInput(new UIFormSelectBox(FIELD_GROUP, FIELD_GROUP, list));
    
    addUIFormInput(new UIFormStringInput(FIELD_BV, FIELD_BV, null));
    addUIFormInput(new UIFormDateTimeInput(FIELD_CREATED_DATE, FIELD_CREATED_DATE, null, false));
    addUIFormInput(new UIFormDateTimeInput(FIELD_DUE_DATE, FIELD_DUE_DATE, null, false));
    addUIFormInput(new UIFormDateTimeInput(FIELD_COMPLETED_DATE, FIELD_COMPLETED_DATE, null, false));
    addUIFormInput(new UIFormStringInput(FIELD_COMPLETNESS, FIELD_COMPLETNESS, null));
    
    
  }

  @Override
  public void activate() {
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

  static public class SaveActionListener extends EventListener<UITaskForm> {
    public void execute(Event<UITaskForm> event) throws Exception {
      String currentUser = event.getRequestContext().getRemoteUser();
      //
      UITeamBPortlet teamBPortlet = event.getSource().getAncestorOfType(UITeamBPortlet.class);
      
      teamBPortlet.cancelAction();
    }
  }

  static public class CloseActionListener extends EventListener<UITaskForm> {
    public void execute(Event<UITaskForm> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource().getAncestorOfType(UITeamBPortlet.class);
      teamBPortlet.cancelAction();
    }
  }

}
