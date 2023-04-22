package org.ancurio.button_mapping;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ButtonMappingManager {
    public static class InputLayout {
        private List<VirtualButton> buttonList = new ArrayList<>();

        public static InputLayout getDefaultInputLayout(Context context) {
            InputLayout b = new InputLayout();
            b.setButtonList(getDefaultButtonList(context));
            return b;
        }

        public static LinkedList<VirtualButton> getDefaultButtonList(Context context) {
            LinkedList<VirtualButton> l = new LinkedList<VirtualButton>();
            l.add(new VirtualCross(context, 0.03, -0.06, 100));
            l.add(VirtualButton.Create(context, VirtualButton.CANCEL, -0.13, -0.14, 100));
            l.add(VirtualButton.Create(context, VirtualButton.ENTER, -0.03, -0.25, 100));
            l.add(VirtualButton.Create(context, VirtualButton.KEY_1, -0.03, -0.06, 100));

            return l;
        }

        public List<VirtualButton> getButtonList() {
            return buttonList;
        }

        public void setButtonList(List<VirtualButton> buttonList) {
            this.buttonList = buttonList;
        }
    }
}
