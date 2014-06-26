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
package org.exoplatform.addons.codefest.team_b.core.lifecycle;

import org.chromattic.api.ChromatticSession;
import org.exoplatform.commons.chromattic.ChromatticLifeCycle;
import org.exoplatform.commons.chromattic.SessionContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.xml.InitParams;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Jun
 * 26, 2014
 */
public class TeamBChromatticLifeCycle extends ChromatticLifeCycle {

  private static final ThreadLocal<ChromatticSession> session = new ThreadLocal<ChromatticSession>();

  private final ThreadLocal taskRoot = new ThreadLocal();

  public static final String LIFECYCLE_NAME = "exo";

  public ChromatticSession getSession() {
    if (invalidSession()) {
      reCreateSession();
    }

    return session.get();
  }

  public TeamBChromatticLifeCycle(final InitParams params) {
    super(params);
  }

  @Override
  protected void onOpenSession(final SessionContext context) {
    session.set(context.getSession());
    super.onOpenSession(context);

  }

  @Override
  protected void onCloseSession(final SessionContext context) {
    super.onCloseSession(context);
    if (session.get() != null) {
      session.get().close();
    }
    session.remove();
    taskRoot.set(null);
  }

  public ThreadLocal getTasksRoot() {
    if (invalidSession()) {
      reCreateSession();
    }
    return taskRoot;
  }

  private boolean invalidSession() {
    boolean invalid = (session.get() == null);
    if (invalid)
      return invalid;
    return session.get().getJCRSession().isLive() == false || session.get().isClosed();
  }

  private void reCreateSession() {
    try {
      onOpenSession(openContext());
    } catch (IllegalStateException e) {
      this.closeContext(false);
      if (this.getManager().getSynchronization() != null) {
        this.getManager().endRequest(false);
      }
      this.getManager().startRequest(PortalContainer.getInstance());
      session.set(this.getChromattic().openSession());
    }
  }

}
