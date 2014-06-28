/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.addons.codefest.team_b.core.model;

import java.util.Calendar;

import org.exoplatform.addons.codefest.team_b.core.model.Task.STATUS;
import org.exoplatform.addons.codefest.team_b.core.storage.PropertyLiteralExpression;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class TaskFilter {
  public enum TimeLine {
    DAY {
      @Override
      public Calendar from() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        return calendar;
      }

      @Override
      public Calendar to() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 59);
        return calendar;
      }
    }, WEEK {
      @Override
      public Calendar from() {
        Calendar calendar = DAY.from();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar;
      }

      @Override
      public Calendar to() {
        Calendar calendar = DAY.to();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return calendar;
      }
    }, MONTH {
      @Override
      public Calendar from() {
        Calendar calendar = DAY.from();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
      }

      @Override
      public Calendar to() {
        Calendar calendar = WEEK.to();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar;
      }
    };
    
    public static TimeLine getTimeView(String name) {
      for (TimeLine timeView : values()) {
        if (timeView.name().equalsIgnoreCase(name)) {
          return timeView;
        }
      }
      return null;
    }
    public abstract Calendar from();
    public abstract Calendar to();
  }
  
  private TimeLine timeLine;
  private STATUS status;
  private String groupId;
  private String assignee;
  
  private PropertyLiteralExpression<Long> withDate = null;
  
  public TaskFilter() {
  }
  
  public TaskFilter status(STATUS status) {
    this.status = status;
    return this;
  }

  public STATUS status() {
    return this.status;
  }

  public TaskFilter timeLine(TimeLine timeLine) {
    this.timeLine = timeLine;
    return this;
  }
  
  public TimeLine timeLine() {
    return this.timeLine;
  }

  public TaskFilter groupId(String groupId) {
    this.groupId = groupId;
    return this;
  }
  
  public String groupId() {
    return this.groupId;
  }

  public TaskFilter assignee(String assignee) {
    this.assignee = assignee;
    return this;
  }
  
  public String assignee() {
    return this.assignee;
  }
  
  public TaskFilter withDate(PropertyLiteralExpression<Long> withDate) {
    this.withDate = withDate;
    return this;
  }
  
  public PropertyLiteralExpression<Long> withDate() {
    return this.withDate;
  }
  
}
