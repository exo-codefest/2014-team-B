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
package org.exoplatform.addons.codefest.team_b.core.storage.query;

import org.exoplatform.addons.codefest.team_b.core.storage.PropertyLiteralExpression;
import org.exoplatform.social.core.storage.query.QueryFunction;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
public class CallExpression <T> {

  private final QueryFunction function;
  private final PropertyLiteralExpression<T> property;

  public CallExpression(final QueryFunction function, final PropertyLiteralExpression<T> property) {

    if (function == null) {
      throw new NullPointerException();
    }
    
    if (property == null) {
      throw new NullPointerException();
    }

    this.function = function;
    this.property = property;
  }

  public QueryFunction getFunction() {
    return function;
  }

  public PropertyLiteralExpression<T> getProperty() {
    return property;
  }

}