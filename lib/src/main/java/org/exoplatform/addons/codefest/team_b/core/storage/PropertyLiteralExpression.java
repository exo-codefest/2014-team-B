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
package org.exoplatform.addons.codefest.team_b.core.storage;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class PropertyLiteralExpression<T>  {

  private final Class<T> type;
  private final String jcrName;
  private final String propertyName;

  public PropertyLiteralExpression(final Class<T> type, final String jcrProperty, String propertyName) {

    if (type == null) {
      throw new NullPointerException();
    }

    if (jcrProperty == null) {
      throw new NullPointerException();
    }
    
    if (propertyName == null) {
      throw new NullPointerException();
    }

    this.type = type;
    this.jcrName = jcrProperty;
    this.propertyName = propertyName;
  }

  public Class<T> getType() {
    return type;
  }

  public String getJcrName() {
    return jcrName;
  }

  public String getPropertyName() {
    return propertyName;
  }
  
  

}
