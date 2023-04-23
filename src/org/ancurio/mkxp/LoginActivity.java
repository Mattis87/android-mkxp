package org.ancurio.mkxp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;

import java.io.File;

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (this.isObbMissing()) {
            findViewById(R.id.obbMissing).setVisibility(View.VISIBLE);
            findViewById(R.id.loginButton).setVisibility(View.GONE);

            return;
        }

        PlayGamesSdk.initialize(this);

        final LoginActivity self = this;

        // Sign in to google play games
        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(this);
        gamesSignInClient.isAuthenticated().addOnCompleteListener(self::authCompleteListener);

        // bind login button
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamesSignInClient.signIn().addOnCompleteListener(self::authCompleteListener);
            }
        });
    }

    protected void authCompleteListener(com.google.android.gms.tasks.Task<com.google.android.gms.games.AuthenticationResult> isAuthenticatedTask) {
        boolean isAuthenticated = (isAuthenticatedTask.isSuccessful() && isAuthenticatedTask.getResult().isAuthenticated());
        if (isAuthenticated) {
            Intent intent = new Intent(this, MKXPActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Not signed in to Google Play Games", Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean isObbMissing() {
        File[] obbFiles = getObbDir().listFiles();
        return obbFiles == null || obbFiles.length < 2;
    }
}
