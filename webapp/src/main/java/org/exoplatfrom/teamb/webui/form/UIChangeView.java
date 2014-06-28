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

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.MembershipEntry;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatfrom.teamb.webui.UITeamBPortlet;


@ComponentConfigs({
  @ComponentConfig(lifecycle = UIFormLifecycle.class, 
    template = "app:/templates/teamb/webui/form/UIChangeView.gtmpl", 
    events = {
      @EventConfig(listeners = UITaskForm.SaveActionListener.class),
      @EventConfig(listeners = UIChangeView.OnChangeGroupActionListener.class),
      @EventConfig(listeners = UITaskForm.CloseActionListener.class, phase = Phase.DECODE)
  })
})
public class UIChangeView extends BaseUIForm {
  public static final String FIELD_SEARCH = "search";

  public UIChangeView() {
    UIFormSelectBox selectBox = new UIFormSelectBox(UITaskForm.FIELD_GROUP, UITaskForm.FIELD_GROUP, null);
    selectBox.setOnChange("OnChangeGroup");
    addUIFormInput(selectBox);
  //  addUIFormInput(new UIFormStringInput(FIELD_SEARCH, FIELD_SEARCH, null));
  }

  public void initOptionsValue() throws Exception {
    List<SelectItemOption<String>> options = new ArrayList<SelectItemOption<String>>();
    options.add(new SelectItemOption<String>(getLabel(UITeamBPortlet.DEFAULT_VIEW), UITeamBPortlet.DEFAULT_VIEW));
    ConversationState state = ConversationState.getCurrent();
    Identity identity = state.getIdentity();
    for (MembershipEntry membership : identity.getMemberships()) {
      String gr = membership.getGroup();
      if (gr.startsWith("/spaces") && isManagerGroup(gr, membership.getMembershipType())) {
        options.add(new SelectItemOption<String>(gr, gr));
      }
    }
    getUIFormSelectBox(UITaskForm.FIELD_GROUP).setOptions(options).setDefaultValue(UITeamBPortlet.DEFAULT_VIEW);
  }

  public boolean isManagerGroup(String groupId, String memberShipType) {
    if("manager".equals(memberShipType)) {
      return true;
    }
    return false;
  }
  
  static public class OnChangeGroupActionListener extends EventListener<UIChangeView> {
    public void execute(Event<UIChangeView> event) throws Exception {
      UIChangeView changeView = event.getSource();
      //
      String groupToViewId = changeView.getUIFormSelectBox(UITaskForm.FIELD_GROUP).getValue();
      UITeamBPortlet teamBPortlet = changeView.getParent();
      teamBPortlet.setGroupToViewId(groupToViewId);
      //
      event.getRequestContext().addUIComponentToUpdateByAjax(teamBPortlet);
    }
  }
}
