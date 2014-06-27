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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletMode;

import org.exoplatform.addons.codefest.team_b.core.api.TaskManager;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.calendar.service.Calendar;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.calendar.service.EventCategory;
import org.exoplatform.calendar.service.Utils;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.MembershipEntry;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatfrom.teamb.webui.form.UIChangeView;
import org.exoplatfrom.teamb.webui.form.UIPopupAction;
import org.exoplatfrom.teamb.webui.form.UITaskForm;
import org.exoplatfrom.teamb.webui.form.UIViewTaskForm;

@ComponentConfig(
   lifecycle = UIApplicationLifecycle.class,
   template = "app:/templates/teamb/webui/UITeamBPortlet.gtmpl", 
     events = {
     @EventConfig(listeners = UITeamBPortlet.AddTaskActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.EditTaskActionListener.class),
     @EventConfig(listeners = UITeamBPortlet.ViewDayActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.ViewWeekActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.ViewMonthActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.ViewTaskActionListener.class )
   }
)
public class UITeamBPortlet extends UIPortletApplication {
  
  public static final String DEFAULT_VIEW = "personal";
  private String groupToViewId = DEFAULT_VIEW;
  private String tabActive = "Day"; 
  
  private static final Log   LOG                          = ExoLogger.getLogger(UITeamBPortlet.class);

  public static final String ANY                          = "*.*";

  public static final String SLASH_COLON                  = "/:";

  public static final String DEFAULT_EVENTCATEGORY_ID_ALL = "defaultEventCategoryIdAll";

  public static final String DEFAULT_EVENTCATEGORY_NAME   = "All";

  public UITeamBPortlet() throws Exception {
    UIPopupAction popupAction = addChild(UIPopupAction.class, null, "UITeamBPopupAction");
    popupAction.getChild(UIPopupWindow.class).setId("UITeamBPopupWindow");
    addChild(UIChangeView.class, null, null);
  }

  public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    PortletRequestContext portletReqContext = (PortletRequestContext) context;
    PortletMode portletMode = portletReqContext.getApplicationMode();
    if (portletMode == PortletMode.VIEW) {
      getChild(UIChangeView.class).initOptionsValue();
    } else if (portletMode == PortletMode.EDIT) {
    }
    super.processRender(app, context);
  }

  public void cancelAction() throws Exception {
    getChild(UIPopupAction.class).cancelPopupAction();
  }
  
  /**
   * @return
   */
  public String getGroupToViewId() {
    return groupToViewId;
  }

  /**
   * Set groupId to view
   * @param groupToViewId
   */
  public void setGroupToViewId(String groupToViewId) {
    this.groupToViewId = groupToViewId;
  }

  public List getDone() {
    List list = new ArrayList();
    return list;
  }

  public List getInprogress() {
    List list = new ArrayList();
    return list;
  }

  public List getOpen() {
    List list = new ArrayList();
    return list;
  }
  
  protected String getTabActive() {
    return tabActive ;
  }

  protected List<Task> getOpenedTasks() {
    TaskManager tm = CommonsUtils.getService(TaskManager.class);
    List<Task> openedTasks = tm.getAll();
    return openedTasks;
  }
  
  protected List<Task> getInProgressTasks() {
    TaskManager tm = CommonsUtils.getService(TaskManager.class);
    List<Task> inProgressTasks = tm.getAll();
    return inProgressTasks;
  }

  protected List<Task> getDoneTasks() {
    TaskManager tm = CommonsUtils.getService(TaskManager.class);
    List<Task> doneTasks = tm.getAll();
    return doneTasks;
    
  }
  public void createCalendarEvent(String currentUser, String summary, Date dueDate, String groupId, String priority) throws Exception{
    CalendarService calendarService = CommonsUtils.getService(CalendarService.class);
    String calendarId = getCalendarByGroupId(groupId).getId();
    //
    EventCategory eventCategory = calendarService.getEventCategory(currentUser, DEFAULT_EVENTCATEGORY_ID_ALL);
    //create calendarEvent
    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.setEventCategoryId(eventCategory.getId());
    calendarEvent.setEventCategoryName(DEFAULT_EVENTCATEGORY_NAME);
    calendarEvent.setSummary(summary);
    calendarEvent.setEventType(CalendarEvent.TYPE_TASK) ;
    calendarEvent.setEventState(CalendarEvent.NEEDS_ACTION) ;
    calendarEvent.setCalType("" + Calendar.TYPE_PUBLIC);
    calendarEvent.setTaskDelegator(currentUser);
    calendarEvent.setPriority(priority);
    //
    calendarEvent.setFromDateTime(new Date());
    calendarEvent.setToDateTime(dueDate);
    
    calendarService.savePublicEvent(calendarId, calendarEvent, true) ;          
    
  }
  
  private Calendar getCalendarByGroupId(String groupId) throws Exception {
    CalendarService calendarService = CommonsUtils.getService(CalendarService.class);
    SpaceService spaceService = CommonsUtils.getService(SpaceService.class);
    Space space = spaceService.getSpaceByGroupId(groupId);
    if (space == null) return null;
    String calendarId = Utils.getCalendarIdFromSpace(groupId);
    Calendar calendar = null;
    try {
      calendar = calendarService.getGroupCalendar(calendarId);
    } catch (Exception e) {
      LOG.warn("Desired calendar for " + space.getPrettyName() + " is not exist, create a new calendar.");
      calendar = null;
    }
    if (calendar == null) {
      calendar = new Calendar();
      calendar.setId(calendarId);
      calendar.setPublic(false);
      calendar.setGroups((new String[] { space.getGroupId() }));
      calendar.setName(space.getDisplayName());
      calendar.setEditPermission(new String[] { space.getGroupId() + SLASH_COLON + ANY });
      calendar.setCalendarOwner(groupId);
      calendar.setCalendarColor(Calendar.COLORS[0]);
      calendarService.savePublicCalendar(calendar, true);
    }
    return calendar;
  }
  
  static public class AddTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      WebuiRequestContext context  = event.getRequestContext();
      List<SelectItemOption<String>> list = new ArrayList<SelectItemOption<String>>();
      Identity identity = ConversationState.getCurrent().getIdentity();
      List<String> spaces = new ArrayList<String>();
      for (MembershipEntry membership : identity.getMemberships()) {
        String gr = membership.getGroup();
        if (gr.startsWith("/spaces") && ! spaces.contains(gr)) {
          list.add(new SelectItemOption<String>(gr, gr));
          spaces.add(gr);
        }
      }
      if (list.isEmpty()) {
        context.getUIApplication().addMessage(new ApplicationMessage("UITeamBPortlet.message.groupNotFound", new String[]{}, ApplicationMessage.WARNING));
        ((PortalRequestContext) context.getParentAppRequestContext()).ignoreAJAXUpdateOnPortlets(true);
        return;
      }
      
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      UITaskForm taskForm = popupAction.activate(UITaskForm.class, 700);
      taskForm.setId("UIAddTaskForm");
      taskForm.setGroups(list);
      taskForm.initForm(identity.getUserId());
      context.addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class EditTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      String taskId = event.getRequestContext().getRequestParameter(OBJECTID);
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      UITaskForm taskForm = popupAction.activate(UITaskForm.class, 700);
      taskForm.setTaskId(taskId).setId("UIEditTaskForm");
      event.getRequestContext().addUIComponentToUpdateByAjax(popupAction);
    }
  }

  static public class ViewTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      String taskId = event.getRequestContext().getRequestParameter(OBJECTID);
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      UIViewTaskForm taskForm = popupAction.activate(UIViewTaskForm.class, 800);
      taskForm.setTaskId(taskId);
      event.getRequestContext().addUIComponentToUpdateByAjax(popupAction);
    }
  }

  static public class ViewMonthActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      teamBPortlet.tabActive = "Month";
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class ViewWeekActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      teamBPortlet.tabActive = "Week";
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class ViewDayActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      teamBPortlet.tabActive = "Day";
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }
}
