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

import java.util.Iterator;

import javax.jcr.NodeIterator;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.chromattic.api.ChromatticSession;
import org.chromattic.api.Status;
import org.exoplatform.addons.codefest.team_b.core.chromattic.entity.TasksRootEntity;
import org.exoplatform.addons.codefest.team_b.core.lifecycle.TeamBChromatticLifeCycle;
import org.exoplatform.addons.codefest.team_b.core.storage.NodeNotFoundException;
import org.exoplatform.commons.chromattic.ChromatticManager;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.impl.core.query.QueryImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public abstract class AbstractManager {

  private static final Log LOG = ExoLogger.getLogger(AbstractManager.class);

  //
  protected final TeamBChromatticLifeCycle lifeCycle;

  //
  protected static final String NS_JCR = "jcr:";

  //
  protected static final String NODETYPE_TASKS = "exo:tasks";


  protected AbstractManager() {

    this.lifeCycle = lifecycleLookup();

  }

  protected ChromatticSession getSession() {
    return lifeCycle.getSession();
  }

  private <T> T getRoot(String nodetypeName, Class<T> t) {
    boolean created = startSynchronization();
    try {
      T got = getSession().findByPath(t, nodetypeName);
      if (got == null) {
        got = getSession().insert(t, nodetypeName);
      }
      return got;
    }
    finally {
      stopSynchronization(created);
    }
  }

  public TasksRootEntity getTasksRoot() {
    if (lifeCycle.getTasksRoot().get() == null) {
      lifeCycle.getTasksRoot().set(getRoot(NODETYPE_TASKS, TasksRootEntity.class));
    }

    return (TasksRootEntity) lifeCycle.getTasksRoot().get();
  }

  protected <T> T _findById(final Class<T> clazz, final String nodeId) throws NodeNotFoundException {

    if (nodeId == null) {
      throw new NodeNotFoundException("null id cannot be found");
    }

    //
    T got = getSession().findById(clazz, nodeId);

    //
    if (got == null) {
      throw new NodeNotFoundException(nodeId + " doesn't exists");
    }

    return got;
  }

  protected <T> T _findByPath(final Class<T> clazz, final String nodePath) throws NodeNotFoundException {
    if (nodePath == null) {
      throw new NodeNotFoundException("null nodePath cannot be found");
    }

    //
    T got = getSession().findByPath(clazz, nodePath, true);

    //
    if (got == null) {
      throw new NodeNotFoundException(nodePath + " doesn't exists");
    }

    return got;
  }

  protected void _removeById(final Class<?> clazz, final String nodeId) {
    getSession().remove(getSession().findById(clazz, nodeId));
  }
  
  protected <T> Status getStatus(final Class<T> clazz, final Object entity) throws IllegalArgumentException {
    if (clazz != entity.getClass()) {
      throw new IllegalArgumentException("Entity argument is wrong.");
    }
    return getSession().getStatus(entity);
  }
  
  protected boolean isJcrProperty(String name) {
    return !name.startsWith(NS_JCR);
  }

  protected void _skip(Iterator<?> it, long offset) {

    // TODO : use JCR skip

    while (it.hasNext()) {
      if (offset == 0) {
        return;
      }
      else {
        it.next();
        --offset;
      }
    }
  }
  
  protected <M> M _getMixin(Object o, Class<M> mixinType, boolean create) {
    M mixin = getSession().getEmbedded(o, mixinType);
    if (mixin == null && create) {
      mixin = getSession().create(mixinType);
      getSession().setEmbedded(o, mixinType, mixin);
    }
    return mixin;
  }

  protected <M> boolean _removeMixin(Object o, Class<M> mixinType) {
    M mixin = getSession().getEmbedded(o, mixinType);
    if (mixin != null) {
      getSession().setEmbedded(o, mixinType, null);
      return true;
    } else {
      return false;
    }
  }
  
  protected <M> boolean _hasMixin(Object o, Class<M> mixinType) {
    M mixin = getSession().getEmbedded(o, mixinType);
    return mixin != null;
  }
  
  /**
   * Gets NodeIterator with Statement with offset and limit
   * 
   * @param statement
   * @param offset
   * @param limit
   * @return
   * @throws Exception
   */
  protected NodeIterator nodes(String statement) {
    //
    if (statement == null) return null;
    
    //
    try {
      QueryManager queryMgr = getSession().getJCRSession().getWorkspace().getQueryManager();
      Query query = queryMgr.createQuery(statement, Query.SQL);
      if (query instanceof QueryImpl) {
        QueryImpl impl = (QueryImpl) query;
        
        return impl.execute().getNodes();
      }
      
      //
      return query.execute().getNodes();
    } catch (Exception ex) {
      LOG.error("Query is failed!.", ex);
      return null;
    }
  }
  
  /**
   * Gets NodeIterator with Statement with offset and limit
   * 
   * @param statement
   * @param offset
   * @param limit
   * @return
   * @throws Exception
   */
  protected NodeIterator nodes(String statement, long offset, long limit) {
    //
    if (statement == null) return null;
    
    //
    try {
      QueryManager queryMgr = getSession().getJCRSession().getWorkspace().getQueryManager();
      Query query = queryMgr.createQuery(statement, Query.SQL);
      if (query instanceof QueryImpl) {
        QueryImpl impl = (QueryImpl) query;
        
        //
        impl.setOffset(offset);
        impl.setLimit(limit);
        
        return impl.execute().getNodes();
      }
      
      //
      return query.execute().getNodes();
    } catch (Exception ex) {
      LOG.info("Failed to execute query = " + statement, ex.getMessage());
      return null;
    }
  }

  public static boolean startSynchronization() {

    TeamBChromatticLifeCycle lc = lifecycleLookup();

    if (lc.getManager().getSynchronization() == null) {
      lc.getManager().beginRequest();
      return true;
    }
    return false;
  }

  public static void stopSynchronization(boolean requestClose) {

    TeamBChromatticLifeCycle lc = lifecycleLookup();
    if (requestClose) {
      lc.getManager().endRequest(true);
    }
  }

  public static TeamBChromatticLifeCycle lifecycleLookup() {

    PortalContainer container = PortalContainer.getInstance();
    ChromatticManager manager = (ChromatticManager) container.getComponentInstanceOfType(ChromatticManager.class);
    return (TeamBChromatticLifeCycle) manager.getLifeCycle(TeamBChromatticLifeCycle.LIFECYCLE_NAME);

  }
}
