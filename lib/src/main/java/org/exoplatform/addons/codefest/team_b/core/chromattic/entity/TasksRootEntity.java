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
package org.exoplatform.addons.codefest.team_b.core.chromattic.entity;

import java.util.Map;

import org.chromattic.api.annotations.Create;
import org.chromattic.api.annotations.FormattedBy;
import org.chromattic.api.annotations.Name;
import org.chromattic.api.annotations.NamingPrefix;
import org.chromattic.api.annotations.OneToMany;
import org.chromattic.api.annotations.Path;
import org.chromattic.api.annotations.PrimaryType;
import org.chromattic.ext.format.BaseEncodingObjectFormatter;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */

@PrimaryType(name="exo:tasks")
@FormattedBy(BaseEncodingObjectFormatter.class)
@NamingPrefix("exo")
public abstract class TasksRootEntity {
  
  @Name
  public abstract String getName();

  @Path
  public abstract String getPath();

  @OneToMany
  public abstract Map<String, TaskEntity> getTasks();

  @Create
  public abstract TaskEntity createTask(String name);
}
