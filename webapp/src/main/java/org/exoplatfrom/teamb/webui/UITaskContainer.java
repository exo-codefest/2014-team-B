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

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatfrom.teamb.webui.form.UIPopupAction;
import org.exoplatfrom.teamb.webui.form.UITaskForm;
import org.exoplatfrom.teamb.webui.form.UIViewTaskForm;

@ComponentConfig(
  lifecycle = UIContainerLifecycle.class,
  template = "app:/templates/teamb/webui/UITaskContainer.gtmpl", 
  events = {
    @EventConfig(listeners = UITaskContainer.AddTaskActionListener.class ),
    @EventConfig(listeners = UITaskContainer.EditTaskActionListener.class),
    @EventConfig(listeners = UITaskContainer.ViewTaskActionListener.class )
  }
)
public class UITaskContainer extends UIContainer {
  
  public UITaskContainer() {
    
  }

  
  static public class AddTaskActionListener extends EventListener<UITaskContainer> {
    public void execute(Event<UITaskContainer> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource().getParent();
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      popupAction.activate(UITaskForm.class, 700).setId("UIAddTaskForm");
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class EditTaskActionListener extends EventListener<UITaskContainer> {
    public void execute(Event<UITaskContainer> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource().getParent();
      String taskId = event.getRequestContext().getRequestParameter(OBJECTID);
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      UITaskForm taskForm = popupAction.activate(UITaskForm.class, 700);
      taskForm.setTaskId(taskId).setId("UIEditTaskForm");
      event.getRequestContext().addUIComponentToUpdateByAjax(popupAction);
    }
  }

  static public class ViewTaskActionListener extends EventListener<UITaskContainer> {
    public void execute(Event<UITaskContainer> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource().getParent();
      String taskId = event.getRequestContext().getRequestParameter(OBJECTID);
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      UIViewTaskForm taskForm = popupAction.activate(UIViewTaskForm.class, 800);
      taskForm.setTaskId(taskId);
      event.getRequestContext().addUIComponentToUpdateByAjax(popupAction);
    }
  }
}
