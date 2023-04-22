package org.ancurio.mkxp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.ancurio.button_mapping.ButtonMappingManager;
import org.ancurio.button_mapping.Helper;
import org.ancurio.button_mapping.VirtualButton;
import org.libsdl.app.SDLActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MKXPActivity extends SDLActivity
{
    /** Configure this to point to game path */
    protected String getGameDirectory() {
        return getFilesDir().getAbsolutePath();
    }

    /** Configure for RTP assets */
    protected String[] getRTPPaths() {
        ArrayList<String> rtps = new ArrayList();

        // Add all OBB files for this app
        File obbDirFile = getObbDir();
        File[] obbFiles = obbDirFile.listFiles();
        if (obbFiles != null) {
            for (File obbFile : obbFiles) {
                if (obbFile.isFile() && obbFile.getName().toLowerCase().endsWith(".obb")) {
                    rtps.add(obbFile.getAbsolutePath());
                }
            }
        }

        return rtps.toArray(new String[0]);
    }

    /** Configure for Scripts.rxdata */
    protected String getScriptsRelativePath() {
        return "GameScripts.rxdata";
    }

    /** Android assets to copy to the game folder */
    protected String[] getAssetsToCopy() {
        return new String[] {
            "GameScripts.rxdata",
        };
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

        // Initialize the button mapping
        inputLayout = ButtonMappingManager.InputLayout.getDefaultInputLayout(getContext());

        // Copy the assets
        copyAssets();
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

    @Override
    protected boolean onUnhandledMessage(int command, Object param) {
        switch (command) {
            case 705:
                addButtons((int) param == 100 || (int) param == 20);
                return true;
        }
        return super.onUnhandledMessage(command, param);
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

    protected void copyAssets() {
        AssetManager assetManager = getAssets();
        for (String asset : getAssetsToCopy()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(asset);
                out = new FileOutputStream(getGameDirectory() + "/" + asset);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + asset, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
}
