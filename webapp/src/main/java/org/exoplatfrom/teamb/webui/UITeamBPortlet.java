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

import javax.portlet.PortletMode;

import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatfrom.teamb.webui.from.UIPopupAction;

@ComponentConfig(
                 lifecycle = UIApplicationLifecycle.class,
                 template = "app:/templates/teamb/webui/UITeamBPortlet.gtmpl",
                 events = {
                   @EventConfig(listeners = UITeamBPortlet.AddTaskActionListener.class ),
                   @EventConfig(listeners = UITeamBPortlet.EditTaskActionListener.class),
                   @EventConfig(listeners = UITeamBPortlet.ViewTaskActionListener.class )
                 }
)
public class UITeamBPortlet extends UIPortletApplication {
  
  public UITeamBPortlet() throws Exception {
    UIPopupAction popupAction = addChild(UIPopupAction.class, null, "UITeamBPopupAction");
    popupAction.getChild(UIPopupWindow.class).setId("UITeamBPopupWindow");
  }
  
  public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    PortletRequestContext portletReqContext = (PortletRequestContext) context;
    PortletMode portletMode = portletReqContext.getApplicationMode();
    if (portletMode == PortletMode.VIEW) {
    } else if (portletMode == PortletMode.EDIT) {
    }
    super.processRender(app, context);
  }
  
  public void cancelAction() throws Exception {
    getChild(UIPopupAction.class).cancelPopupAction();
  }

  static public class AddTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      String taskId = event.getRequestContext().getRequestParameter(OBJECTID);
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class EditTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      String taskId = event.getRequestContext().getRequestParameter(OBJECTID);
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class ViewTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      String taskId = event.getRequestContext().getRequestParameter(OBJECTID);
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

}
