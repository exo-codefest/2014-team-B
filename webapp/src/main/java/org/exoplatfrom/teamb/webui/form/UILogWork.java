package org.exoplatfrom.teamb.webui.form;

import org.exoplatform.addons.codefest.team_b.core.utils.TaskManagerUtils;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;
import org.exoplatfrom.teamb.webui.UITeamBPortlet;
import org.exoplatfrom.teamb.webui.Utils;

@ComponentConfigs({
  @ComponentConfig(lifecycle = UIFormLifecycle.class, 
    template = "app:/templates/teamb/webui/form/UITaskForm.gtmpl", 
    events = {
      @EventConfig(listeners = UILogWork.SaveActionListener.class),
      @EventConfig(listeners = UILogWork.CloseActionListener.class, phase = Phase.DECODE)
  })
})
public class UILogWork extends BaseUIForm implements UIPopupComponent {
  final public static String FIELD_LOG_WORD        = "LogWork";
  private String taskId = null;
  public UILogWork() throws Exception {
    UIFormStringInput stringInput = new UIFormStringInput(FIELD_LOG_WORD, FIELD_LOG_WORD, null);
    stringInput.addValidator(MandatoryValidator.class);
    //
    addUIFormInput(stringInput);
  }

  static public class SaveActionListener extends EventListener<UILogWork> {
    public void execute(Event<UILogWork> event) throws Exception {
      UILogWork logWork = event.getSource();
      String value = logWork.getUIStringInput(FIELD_LOG_WORD).getValue(); 
      int timeLog = Utils.getTimeValue(value);
      TaskManagerUtils.logWork(logWork.getTaskId(), String.valueOf(timeLog) + "m");
      //
      UITeamBPortlet teamBPortlet = logWork.getAncestorOfType(UITeamBPortlet.class);
      teamBPortlet.cancelAction();
    }
  }

  @Override
  public void activate() {
  }

  @Override
  public void deActivate() {
    setTaskId(null);
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }
}
