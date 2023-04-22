package org.ancurio.mkxp;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

import org.libsdl.app.SDLActivity;
import org.ancurio.button_mapping.ButtonMappingManager;
import org.ancurio.button_mapping.Helper;
import org.ancurio.button_mapping.VirtualButton;

public class MKXPActivity extends SDLActivity
{
    /** Configure this to point to game path */
    protected String getGameDirectory() {
        return getFilesDir().getAbsolutePath();
    }

    /** Configure for RTP assets */
    protected String[] getRTPPaths() {
        return new String[] {
            getObbDir() + "/obb_type_main.2000028.obb",
            getObbDir() + "/obb_type_patch.2000028.obb",
            getObbDir() + "/ScriptsNew.zip",
        };
    }

    /** Configure for Scripts.rxdata */
    protected String getScriptsRelativePath() {
        return "ScriptsNew.rxdata";
    }

    private ButtonMappingManager.InputLayout inputLayout;

    @Override
    protected String[] getLibraries() {
        return new String[] {
                "openal",
                "mkxp",
        };
    }

    @Override
    protected String[] getArguments() {
        return new String[]{
            this.getGameDirectory(),
            String.join(",", this.getRTPPaths()) + ",",
            this.getScriptsRelativePath(),
        };
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        inputLayout = ButtonMappingManager.InputLayout.getDefaultInputLayout(getContext());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void addButtons(boolean drawText) {
        // Adding the buttons
        for (VirtualButton b : inputLayout.getButtonList()) {
            // We add it, if it's not the case already
            if (b.getParent() != mLayout) {
                if (b.getParent() != null) {
                    ((ViewGroup) b.getParent()).removeAllViews();
                }
                mLayout.addView(b);
            }

            // Draw text if we want to
            if (b.drawtext != drawText) {
                b.drawtext = drawText;
                b.invalidate();
            }

            // Setup the positions
            Helper.setLayoutPosition(this, b, b.getPosX(), b.getPosY());
        }
    }

    @Override
    protected boolean onUnhandledMessage(int command, Object param) {
        switch (command) {
            case 705:
                addButtons((int) param == 100 || (int) param == 20);
                return true;
        }
        return super.onUnhandledMessage(command, param);
    }
}
