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

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class TaskFilter {
  public enum TIMEVIEW {
    DAY {
      @Override
      Calendar getFrom() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        return calendar;
      }

      @Override
      Calendar getTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        return Calendar.getInstance();
      }
    }, WEEK {
      @Override
      Calendar getFrom() {
        Calendar calendar = DAY.getFrom();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar;
      }

      @Override
      Calendar getTo() {
        Calendar calendar = DAY.getTo();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return calendar;
      }
    }, MONTH {
      @Override
      Calendar getFrom() {
        Calendar calendar = DAY.getFrom();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
      }

      @Override
      Calendar getTo() {
        Calendar calendar = DAY.getTo();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar;
      }
    };
    
    public static TIMEVIEW getTimeView(String name) {
      for (TIMEVIEW timeView : values()) {
        if (timeView.name().equalsIgnoreCase(name)) {
          return timeView;
        }
      }
      return null;
    }
    abstract Calendar getFrom();
    abstract Calendar getTo();
  }
  
  private TIMEVIEW timeview;
  private STATUS status;
  
  public TaskFilter() {
  }
  
  public TaskFilter status(STATUS timeview) {
    this.status = timeview;
    return this;
  }

  public STATUS status() {
    return this.status;
  }

  public TaskFilter timeview(TIMEVIEW timeview) {
    this.timeview = timeview;
    return this;
  }
  
  public TIMEVIEW timeview() {
    return this.timeview;
  }
  

}
