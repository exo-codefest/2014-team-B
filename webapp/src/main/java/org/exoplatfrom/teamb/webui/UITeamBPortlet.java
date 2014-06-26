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

import java.util.Date;

import javax.portlet.PortletMode;

import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.calendar.service.EventCategory;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;
import org.exoplatfrom.teamb.webui.form.UIPopupAction;

@ComponentConfig(
   lifecycle = UIApplicationLifecycle.class,
   template = "app:/templates/teamb/webui/UITeamBPortlet.gtmpl"
)
public class UITeamBPortlet extends UIPortletApplication {

  public UITeamBPortlet() throws Exception {
    addChild(UITaskContainer.class, null, "UITaskContainer");
    UIPopupAction popupAction = addChild(UIPopupAction.class, null, "UITeamBPopupAction");
    popupAction.getChild(UIPopupWindow.class).setId("UITeamBPopupWindow");
  }

  public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    PortletRequestContext portletReqContext = (PortletRequestContext) context;
    PortletMode portletMode = portletReqContext.getApplicationMode();
    if (portletMode == PortletMode.VIEW) {
    } else if (portletMode == PortletMode.EDIT) {
      return;
    }
    super.processRender(app, context);
  }

  public void cancelAction() throws Exception {
    getChild(UIPopupAction.class).cancelPopupAction();
  }

  public void createCalendarEvent(String currentUser, String category, String summary, String type, Date from, Date to) throws Exception{
    CalendarService calendarService = CommonsUtils.getService(CalendarService.class);
    String calendarId = currentUser + "-defaultCalendarId";
    //create eventCategory
    EventCategory eventCategory = new EventCategory();
    eventCategory.setName(category);
    calendarService.saveEventCategory(currentUser, eventCategory, true);
    //create calendarEvent
    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.setEventCategoryId(eventCategory.getId());
    calendarEvent.setEventCategoryName(eventCategory.getName());
    calendarEvent.setSummary(summary);
    calendarEvent.setEventType(CalendarEvent.TYPE_TASK) ;
    calendarEvent.setEventState(CalendarEvent.NEEDS_ACTION) ;
    calendarEvent.setCalType(type);
    calendarEvent.setTaskDelegator(currentUser);
    //
    calendarEvent.setFromDateTime(from);
    calendarEvent.setToDateTime(to);
    
    if(type.equals("0")) {
      calendarService.saveUserEvent(currentUser, calendarId, calendarEvent, true) ;
    }else if(type.equals("1")){
      calendarService.saveEventToSharedCalendar(currentUser, calendarId, calendarEvent, true) ;
    }else if(type.equals("2")){
      calendarService.savePublicEvent(calendarId, calendarEvent, true) ;          
    }
    
  }
  
}
