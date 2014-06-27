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

import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatfrom.teamb.webui.UITeamBPortlet;

public class BaseUIForm extends UIForm {

  static public class CloseActionListener extends EventListener<BaseUIForm> {
    public void execute(Event<BaseUIForm> event) throws Exception {
      UITeamBPortlet teamBPortlet = event.getSource().getAncestorOfType(UITeamBPortlet.class);
      teamBPortlet.cancelAction();
    }
  }
  
  
  /**
   * Sends a warning message to ui and ignore ajax update on Portlets
   * @param messageKey resource bundle key for the message
   */
  protected void warning(String messageKey) {
    warning(messageKey, null, true);
  }

  protected void warning(String messageKey, String arg) {
    warning(messageKey, new String[] { arg });
  }
  
  protected void warning(String messageKey, String[] args) {
    warning(messageKey, args, true);
  }
  
  /**
   * Sends a warning message to ui
   * @param messageKey resource bundle key for the message
   * @param ignoreAJAXUpdateOnPortlets as there is need to update only UI components 
   * of portal (ie: the components outside portlet windows) are updated by AJAX.
   */
  protected void warning(String messageKey, boolean ignoreAJAXUpdateOnPortlets) {
    warning(messageKey, null, ignoreAJAXUpdateOnPortlets);
  }

  /**
   * Sends a parameterized warning to ui
   * @param messageKey
   * @param args arguments of the message
   * @param ignoreAJAXUpdateOnPortlets as there is need to update only UI components 
   * of portal (ie: the components outside portlet windows) are updated by AJAX.
   */
  protected void warning(String messageKey, String[] args, boolean ignoreAJAXUpdateOnPortlets) {
    message(messageKey, args, ApplicationMessage.WARNING, ignoreAJAXUpdateOnPortlets);
  }
  
  /**
   * Sends a warning message to ui
   * @param messageKey resource bundle key for the message
   * @param messageType {@link ApplicationMessage}
   * @param ignoreAJAXUpdateOnPortlets as there is need to update only UI components 
   * of portal (ie: the components outside portlet windows) are updated by AJAX.
   */
  private void message(String messageKey, String[] args, int messageType, boolean ignoreAJAXUpdateOnPortlets) {
    WebuiRequestContext context = WebuiRequestContext.getCurrentInstance();
    context.getUIApplication().addMessage(new ApplicationMessage(messageKey, args, messageType));
    ((PortalRequestContext) context.getParentAppRequestContext()).ignoreAJAXUpdateOnPortlets(ignoreAJAXUpdateOnPortlets);
  }
  
}
