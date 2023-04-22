package org.ancurio.mkxp;
import android.os.Environment;

import org.libsdl.app.SDLActivity;

public class MKXPActivity extends SDLActivity
{
    @Override
    protected String[] getArguments() {
        return new String[]{
            this.getConfPath(),
        };
    }

    @Override
    protected String[] getLibraries() {
        return new String[] {
                "mkxp_wrapper",
                "openal",
                "mkxp",
        };
    }
    public String getConfPath(){
        return getObbDir().getPath();
    }
}
