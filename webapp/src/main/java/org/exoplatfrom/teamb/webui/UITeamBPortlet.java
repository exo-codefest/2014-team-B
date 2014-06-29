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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletMode;

import org.exoplatform.addons.codefest.team_b.core.api.TaskListAccess;
import org.exoplatform.addons.codefest.team_b.core.api.TaskManager;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.addons.codefest.team_b.core.model.TaskFilter;
import org.exoplatform.addons.codefest.team_b.core.model.TaskFilter.TimeLine;
import org.exoplatform.addons.codefest.team_b.core.utils.TaskManagerUtils;
import org.exoplatform.calendar.service.Calendar;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.calendar.service.EventCategory;
import org.exoplatform.calendar.service.Utils;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.ListAccess;
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
import org.exoplatfrom.teamb.webui.form.UILogWork;
import org.exoplatfrom.teamb.webui.form.UIPopupAction;
import org.exoplatfrom.teamb.webui.form.UITaskForm;
import org.exoplatfrom.teamb.webui.form.UIViewTaskForm;

@ComponentConfig(
   lifecycle = UIApplicationLifecycle.class,
   template = "app:/templates/teamb/webui/UITeamBPortlet.gtmpl", 
     events = {
     @EventConfig(listeners = UITeamBPortlet.AddTaskActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.EditTaskActionListener.class),
     @EventConfig(listeners = UITeamBPortlet.RemoveTaskActionListener.class),
     @EventConfig(listeners = UITeamBPortlet.ViewDayActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.ViewWeekActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.ViewMonthActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.ViewTaskActionListener.class ),
     @EventConfig(listeners = UITeamBPortlet.DragDropActionListener.class )
   }
)
public class UITeamBPortlet extends UIPortletApplication {
  private static final Log   LOG                          = ExoLogger.getLogger(UITeamBPortlet.class);

  public static final String DEFAULT_VIEW                 = "personal";
  
  private String             groupToViewId                = DEFAULT_VIEW;
  
  private String             tabActive                    = "Day";

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
      getChild(UIChangeView.class).initOptionsValue(getGroupToViewId());
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

  protected String getTabActive() {
    return tabActive ;
  }

  protected List<Task> getOpenedTasks() throws Exception {
    TaskFilter filter = commonTaskFilter().status(Task.STATUS.OPEN);
    filter.withDate(TaskEntity.dueDateTime);

    TaskManager tm = CommonsUtils.getService(TaskManager.class);
    List<Task> openedTasks = Arrays.asList(((TaskListAccess) tm.find(filter)).getAll());
    return openedTasks;
  }

  private TaskFilter commonTaskFilter() {
    TaskFilter filter = new TaskFilter();
    filter.timeLine(TimeLine.getTimeView(tabActive));
    if (!DEFAULT_VIEW.equals(groupToViewId)) {
      filter.groupId(groupToViewId);
    } else {
      filter.assignee(ConversationState.getCurrent().getIdentity().getUserId());
    }
    return filter;
  }

  protected List<Task> getInProgressTasks() throws Exception {
    TaskFilter filter = commonTaskFilter().status(Task.STATUS.IN_PROGRESS);
    filter.withDate(TaskEntity.inProgressDate);

    TaskManager tm = CommonsUtils.getService(TaskManager.class);
    ListAccess<Task> listAccess = tm.find(filter);
    List<Task> inProgressTasks = Arrays.asList(((TaskListAccess) listAccess).getAll());
    return inProgressTasks;
  }

  protected List<Task> getDoneTasks() throws Exception {
    TaskFilter filter = commonTaskFilter().status(Task.STATUS.RESOLVED);
    filter.withDate(TaskEntity.resolvedTime);

    TaskManager tm = CommonsUtils.getService(TaskManager.class);
    List<Task> doneTasks = Arrays.asList(((TaskListAccess) tm.find(filter)).getAll());
    return doneTasks;
  }

  public CalendarEvent createCalendarEvent(String currentUser, String summary, Date dueDate, String groupId, String priority) throws Exception{
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
    //
    return calendarEvent;
  }
  
  public void updateCalendarEvent(String currentUser, String summary, Date dueDate,
                                  String groupId, String priority, String eventId) {
    try {
      CalendarService calendarService = CommonsUtils.getService(CalendarService.class);
      String calendarId = getCalendarByGroupId(groupId).getId();
      CalendarEvent calendarEvent = calendarService.getEventById(eventId);
      calendarEvent.setSummary(summary);
      calendarEvent.setPriority(priority);
      calendarEvent.setTaskDelegator(currentUser);
      calendarEvent.setToDateTime(dueDate);
      calendarService.savePublicEvent(calendarId, calendarEvent, false);
    } catch (Exception e) {
      LOG.warn("Updating the calendarEvent " + eventId + " is unsuccessfully.", e);
    }
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
      Identity identity = ConversationState.getCurrent().getIdentity();
      List<SelectItemOption<String>> list = new ArrayList<SelectItemOption<String>>();
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
      taskForm.initForm();
      context.addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class EditTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      WebuiRequestContext context  = event.getRequestContext();
      String taskId = context.getRequestParameter(OBJECTID);
      TaskManager tm = CommonsUtils.getService(TaskManager.class);
      Task task = tm.get(taskId);
      if(task == null) {
        context.getUIApplication().addMessage(new ApplicationMessage("UITeamBPortlet.message.taskNotFound", new String[]{}, ApplicationMessage.WARNING));
        ((PortalRequestContext) context.getParentAppRequestContext()).ignoreAJAXUpdateOnPortlets(true);
        return;
      }
      
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      UITaskForm taskForm = popupAction.activate(UITaskForm.class, 700);
      taskForm.setTask(task).setId("UIEditTaskForm");
      taskForm.initForm();
      event.getRequestContext().addUIComponentToUpdateByAjax(popupAction);
    }
  }

  static public class RemoveTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      WebuiRequestContext context  = event.getRequestContext();
      String taskId = context.getRequestParameter(OBJECTID);
      TaskManager tm = CommonsUtils.getService(TaskManager.class);
      if(tm.get(taskId) == null) {
        context.getUIApplication().addMessage(new ApplicationMessage("UITeamBPortlet.message.taskNotFound", new String[]{}, ApplicationMessage.WARNING));
        ((PortalRequestContext) context.getParentAppRequestContext()).ignoreAJAXUpdateOnPortlets(true);
        return;
      }
      tm.delete(taskId);
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }

  static public class ViewTaskActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource();
      WebuiRequestContext context  = event.getRequestContext();
      
      String taskId = context.getRequestParameter(OBJECTID);
      
      TaskManager tm = CommonsUtils.getService(TaskManager.class);
      Task task = tm.get(taskId);
      if(task == null) {
        context.getUIApplication().addMessage(new ApplicationMessage("UITeamBPortlet.message.taskNotFound", new String[]{}, ApplicationMessage.WARNING));
        ((PortalRequestContext) context.getParentAppRequestContext()).ignoreAJAXUpdateOnPortlets(true);
        return;
      }
      
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      UIViewTaskForm taskForm = popupAction.activate(UIViewTaskForm.class, 800);
      taskForm.setTask(task);
      taskForm.setTaskActivity(task);
      context.addUIComponentToUpdateByAjax(popupAction);
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

  static public class DragDropActionListener extends EventListener<UITeamBPortlet> {
    public void execute(Event<UITeamBPortlet> event) throws Exception {
      WebuiRequestContext context  = event.getRequestContext();
      boolean ignoreUpdate = true;
      String taskId = context.getRequestParameter(OBJECTID);
      String newTaskStatus = context.getRequestParameter("taskstatus");
      if ("open".equals(newTaskStatus)) {
        TaskManagerUtils.reOpen(taskId);
        LOG.info("Done to reopen task " + taskId);
      } else if ("inprogress".equals(newTaskStatus)) {
        TaskManagerUtils.inProgress(taskId);
        LOG.info("Done to inProgress task " + taskId);
      } else if ("done".equals(newTaskStatus)) {
        LOG.info("Done to resolved task " + taskId);
        TaskManagerUtils.resolved(taskId);
        //
        ignoreUpdate = false;
        UIPopupAction popupAction = event.getSource().getChild(UIPopupAction.class);
        UILogWork taskForm = popupAction.activate(UILogWork.class, 650);
        taskForm.setId("UILogWork");
        taskForm.setTaskId(taskId);
        context.addUIComponentToUpdateByAjax(popupAction);
      }
      if(ignoreUpdate) {
        ((PortalRequestContext) context.getParentAppRequestContext()).ignoreAJAXUpdateOnPortlets(true);
      }
    }
  }
  
  protected enum TIME {
    DAY {
      @Override
      public long time() {
        return 8 * 60;
      }

      @Override
      public double position(long t) {
        long start = TaskFilter.TimeLine.DAY.from().getTimeInMillis();
        long end = TaskFilter.TimeLine.DAY.to().getTimeInMillis();
        return calculatePosition(t, start, end);
      }
    },
    WEEK {
      @Override
      public long time() {
        return DAY.time() * 5;
      }

      @Override
      public double position(long t) {
        java.util.Calendar cal = TaskFilter.TimeLine.DAY.from();
        cal.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
        long start = cal.getTimeInMillis();
        cal.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.FRIDAY);
        long end = cal.getTimeInMillis();
        return calculatePosition(t, start, end);
      }
    },
    MONTH {
      @Override
      public long time() {
        return WEEK.time() * 4;
      }

      @Override
      public double position(long t) {
        long start = TaskFilter.TimeLine.MONTH.from().getTimeInMillis();
        long freeDay = 8 * 24 * 60 * 60 * 1000;
        long end = TaskFilter.TimeLine.MONTH.to().getTimeInMillis() - freeDay;
        return calculatePosition(t, start, end);
      }
    };
    public static TIME getTIME(String name) {
      for (int i = 0; i < TIME.values().length; i++) {
        if (TIME.values()[i].name().equalsIgnoreCase(name)) {
          return TIME.values()[i];
        }
      }
      return null;
    }

    protected double calculatePosition(long t, long start, long end) {
      long h = end - start;
      long th = t - start;
      return th / h;
    }
    public abstract long time();
    public abstract double position(long t);
  }
  
  //
  protected Timeline processTimeline(Task task) {
    // position
    // width
    //
    // workLogged da lam
    // estimation du kien lam
    // completeness
    long taskTime = org.exoplatfrom.teamb.webui.Utils.getTimeValue(task.getValue(TaskEntity.workLogged));
    if (taskTime == 0) {
      taskTime = org.exoplatfrom.teamb.webui.Utils.getTimeValue(task.getValue(TaskEntity.estimation));
    }
    LOG.info("taskTime " + taskTime);
    TIME time = TIME.getTIME(getTabActive());
    Timeline timeline = new Timeline();
    timeline.setWidth(taskTime / time.time());
    //
    Long completeness = task.getValue(TaskEntity.completeness);
    if (completeness == null || completeness == 0) {
      completeness = 100l;
    }
    timeline.setPercent(completeness);

    Long resolvedTime = task.getValue(TaskEntity.resolvedTime);
    if (resolvedTime == null || resolvedTime == 0) {
      resolvedTime = task.getValue(TaskEntity.inProgressDate);
    }
    
    LOG.info("resolvedTime1 " + resolvedTime);
    //
    resolvedTime = resolvedTime - (taskTime * 60 * 1000);
    LOG.info("resolvedTime2 " + resolvedTime);
    timeline.setPosition(time.position(resolvedTime));
    //
    return timeline;
  }
  
  static public class Timeline {
    double position;
    double width;
    double percent;
    
    public double getPosition() {
      return position;
    }
    public void setPosition(double position) {
      this.position = position;
    }
    public double getWidth() {
      return width;
    }
    public void setWidth(double width) {
      this.width = width;
    }
    public double getPercent() {
      return percent;
    }
    public void setPercent(double percent) {
      this.percent = percent;
    }
    @Override
    public String toString() {
      return "{position: " + position + ", width: " + width + ", percent: " + percent + "}";
    }
  }
}
