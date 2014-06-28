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
package org.exoplatform.addons.codefest.team_b.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.exoplatform.addons.codefest.team_b.core.api.TaskListAccess;
import org.exoplatform.addons.codefest.team_b.core.api.TaskManager;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TaskEntity;
import org.exoplatform.addons.codefest.team_b.core.data.TaskDataBuilder;
import org.exoplatform.addons.codefest.team_b.core.model.Task;
import org.exoplatform.addons.codefest.team_b.core.model.TaskFilter;
import org.exoplatform.addons.codefest.team_b.core.model.TaskFilter.TimeLine;
import org.exoplatform.addons.codefest.team_b.core.test.AbstractCoreTest;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.spi.SpaceService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class TaskManagerImplTest extends AbstractCoreTest {

  private final Log LOG = ExoLogger.getLogger(TaskManagerImplTest.class);
  private List<Task> tearDownTaskList;
  private Identity rootIdentity;
  private Identity johnIdentity;
  private Identity maryIdentity;
  private Identity demoIdentity;

  private IdentityManager identityManager;
  private TaskManager taskManager;
  private SpaceService spaceService;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    identityManager = (IdentityManager) getContainer().getComponentInstanceOfType(IdentityManager.class);
    spaceService = (SpaceService) getContainer().getComponentInstanceOfType(SpaceService.class);
    taskManager = (TaskManager) getContainer().getComponentInstanceOfType(TaskManager.class);
    tearDownTaskList = new ArrayList<Task>();
    rootIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "root", false);
    johnIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "john", false);
    maryIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "mary", false);
    demoIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "demo", false);
  }

  @Override
  public void tearDown() throws Exception {
    for (Task t : tearDownTaskList) {
      try {
        taskManager.delete(t.getId());
      } catch (Exception e) {
        LOG.warn("can not delete task with id: " + t.getId());
      }
    }
    identityManager.deleteIdentity(rootIdentity);
    identityManager.deleteIdentity(johnIdentity);
    identityManager.deleteIdentity(maryIdentity);
    identityManager.deleteIdentity(demoIdentity);
    super.tearDown();
  }
  
  public void testCreateTask() throws Exception {
    Task task = TaskDataBuilder.initOne(demoIdentity).injectOne();
    assertNotNull(task.getId());
    assertTrue(task.getId().length() > 0);
    
    tearDownTaskList.add(task);
  }
  
  public void testGetTask() throws Exception {
    Task task = TaskDataBuilder.initOne(demoIdentity).injectOne();
    task = taskManager.get(task.getId());
    assertNotNull(task);
    assertTrue(task.getValue(TaskEntity.title).length() > 0);
    tearDownTaskList.add(task);
  }
  
  public void testUpdateTask() throws Exception {
    Task task = TaskDataBuilder.initOne(demoIdentity).injectOne();
    
    task.setValue(TaskEntity.title, "title issue has been updated");
    taskManager.update(demoIdentity, task, TaskEntity.title.getPropertyName());
    
    Task got = taskManager.get(task.getId());
    
    assertNotNull(got);
    assertEquals(task.getValue(TaskEntity.title), got.getValue(TaskEntity.title));
    
    tearDownTaskList.add(task);
  }
  
  public void testDeleteTask() throws Exception {
    Task task = TaskDataBuilder.initOne(demoIdentity).injectOne();
    taskManager.delete(task.getId());
    
    Task got = taskManager.get(task.getId());
    assertNull(got);
  }
  
  public void testGetAll() throws Exception {
    tearDownTaskList = TaskDataBuilder.initMore(10, demoIdentity).inject();
    
    List<Task> list = taskManager.getAll();
    assertEquals(10, list.size());
  }
  
  public void testGetAllByReporter() throws Exception {
    tearDownTaskList = TaskDataBuilder.initMore(10, demoIdentity).inject();
    
    tearDownTaskList.addAll(TaskDataBuilder.initMore(10, johnIdentity).inject());
    
    List<Task> list = taskManager.getAll();
    assertEquals(20, list.size());
    
    dump(list);
    
    list = taskManager.getAllByReporter(demoIdentity.getRemoteId());
    assertEquals(10, list.size());
  }
  
  public void testGetAllByAssignee() throws Exception {
    tearDownTaskList = TaskDataBuilder.initMore(10, "task xxx", demoIdentity, maryIdentity).inject();
    
    tearDownTaskList.addAll(TaskDataBuilder.initMore(10, "task xxx", johnIdentity, maryIdentity).inject());
    
    List<Task> list = taskManager.getAll();
    assertEquals(20, list.size());
    
    list = taskManager.getAllByAssignee(maryIdentity.getRemoteId());
    assertEquals(20, list.size());
  }
  
  public void testLoadByDay() throws Exception {
    tearDownTaskList = TaskDataBuilder.initMore(10, "task xxx", demoIdentity, maryIdentity).inject();
    
    tearDownTaskList.addAll(TaskDataBuilder.initMore(10, "task xxx", johnIdentity, maryIdentity).inject());
    
    
    TaskFilter filter = new TaskFilter();
    filter.status(Task.STATUS.OPEN);
    filter.assignee(maryIdentity.getRemoteId());
    filter.withDate(TaskEntity.createdTime);
    filter.timeLine(TimeLine.WEEK);

    TaskManager tm = CommonsUtils.getService(TaskManager.class);
    List<Task> list = Arrays.asList(((TaskListAccess) tm.find(filter)).load(0, 20));
    
    assertEquals(20, list.size());
  }
  
  private void dump(List<Task> list) {
    for(Task t : list) {
      LOG.info(t.toString());
    }
  }
}
