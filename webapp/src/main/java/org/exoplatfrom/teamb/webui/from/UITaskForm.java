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
package org.exoplatfrom.teamb.webui.from;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
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
  
  public UITaskForm() {
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
