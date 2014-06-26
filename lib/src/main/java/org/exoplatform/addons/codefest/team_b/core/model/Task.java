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

import java.util.HashMap;

import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.storage.PropertyLiteralExpression;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class Task extends HashMap<String, Object> {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public String getId() {
    return getValue(TaskEntity.id);
  }

  /**
   * Sets the value to the given property
   * @param propertyName
   * @param value
   */
  public <T> void setValue(PropertyLiteralExpression<T> property, T value) {
    this.put(property.getPropertyName(), value);
  }
  
  /**
   * Gets the value of the property
   * @param property
   * @return
   */
  public <T> T getValue(PropertyLiteralExpression<T> property) {
    Object val = this.get(property.getPropertyName());
    return property.getType().cast(val);
  }
  
}
