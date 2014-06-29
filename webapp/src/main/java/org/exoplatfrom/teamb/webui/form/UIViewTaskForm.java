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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.webui.activity.UIActivitiesContainer;
import org.exoplatform.social.webui.composer.UIComposer.PostContext;
import org.exoplatform.social.webui.profile.UIUserActivitiesDisplay.DisplayMode;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatfrom.teamb.webui.UITeamBPortlet;
import org.exoplatfrom.teamb.webui.Utils;

@ComponentConfigs({
  @ComponentConfig(lifecycle = UIFormLifecycle.class, 
    template = "app:/templates/teamb/webui/form/UIViewTaskForm.gtmpl", 
    events = {
      @EventConfig(listeners = UIViewTaskForm.CloseActionListener.class, phase = Phase.DECODE),
      @EventConfig(listeners = UIViewTaskForm.EditTaskActionListener.class)
  })
})
public class UIViewTaskForm extends BaseUIForm implements UIPopupComponent {

  private Task task;
  
  public UIViewTaskForm() {
  }

  @Override
  public void activate() {
  }

  @Override
  public void deActivate() {
  }
  
  public Task getTask() {
    return Utils.fillPropertiesTask(task);
  }

  public void setTask(Task task) {
    this.task = task;
  }
  
  public Date getDate(Long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    return calendar.getTime();
  }
  
  public void setTaskActivity(Task task) throws Exception {
    CalendarService calendarService = CommonsUtils.getService(CalendarService.class);
    SpaceService spaceService = CommonsUtils.getService(SpaceService.class);
    ActivityManager activityManager = CommonsUtils.getService(ActivityManager.class);
    CalendarEvent calendarEvent = calendarService.getEventById(task.getTaskId());
    ExoSocialActivity activity = activityManager.getActivity(calendarEvent.getActivityId());
    Space space = spaceService.getSpaceByGroupId(task.getValue(TaskEntity.groupId));
    if (activity != null && space != null) {
      UIActivitiesContainer activitiesContainer = addChild(UIActivitiesContainer.class, null, null);
      activitiesContainer.setSpace(space);
      activitiesContainer.setActivityList(Arrays.asList(activity));
      activitiesContainer.setPostContext(PostContext.SPACE);
      activitiesContainer.setSelectedDisplayMode(DisplayMode.ALL_ACTIVITIES.toString());
    }
    
  }

  static public class EditTaskActionListener extends EventListener<UIViewTaskForm> {
    public void execute(Event<UIViewTaskForm> event) throws Exception {
      UIViewTaskForm viewTaskForm =  event.getSource();
      UITeamBPortlet teamBPortlet = viewTaskForm.getAncestorOfType(UITeamBPortlet.class);
      teamBPortlet.cancelAction();
      //
      UIPopupAction popupAction = teamBPortlet.getChild(UIPopupAction.class);
      UITaskForm taskForm = popupAction.activate(UITaskForm.class, 700);
      taskForm.setTask(viewTaskForm.task).setId("UIEditTaskForm");
      taskForm.initForm();
      event.getRequestContext().addUIComponentToUpdateByAjax(popupAction);
    }
  }
  
}
