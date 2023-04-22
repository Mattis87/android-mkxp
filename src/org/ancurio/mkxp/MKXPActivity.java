package org.ancurio.mkxp;
import android.os.Environment;

import org.libsdl.app.SDLActivity;

public class MKXPActivity extends SDLActivity
{
    @Override
    protected String[] getArguments() {
        return new String[]{this.getConfPath()};
    }

    public String getConfPath(){
        return getObbDir().getPath();
    }
}
