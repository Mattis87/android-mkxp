package org.ancurio.mkxp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

public class LoginActivity extends Activity {
    protected String legacySavePath() {
        return "Aveyond/aveyond1";
    }

    private final int MIGRATE_INTENT = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (this.isObbMissing()) {
            findViewById(R.id.obbMissing).setVisibility(View.VISIBLE);
            return;
        }

        PlayGamesSdk.initialize(this);

        final LoginActivity self = this;

        // Sign in to google play games
        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(this);
        gamesSignInClient.isAuthenticated().addOnCompleteListener(self::authCompleteListener);

        // bind login button
        findViewById(R.id.loginButton).setOnClickListener(v -> gamesSignInClient.signIn().addOnCompleteListener(self::authCompleteListener));

        findViewById(R.id.startButton).setOnClickListener(v -> startGame());

        findViewById(R.id.migrateButton).setOnClickListener(v -> migrateIntent());

        ((CheckBox) findViewById(R.id.migrateHide)).setChecked(isMigrateHidden());
        ((CheckBox) findViewById(R.id.migrateHide)).setOnCheckedChangeListener((buttonView, isChecked) -> setMigrateHidden(isChecked));
    }

    protected void authCompleteListener(com.google.android.gms.tasks.Task<com.google.android.gms.games.AuthenticationResult> isAuthenticatedTask) {
        boolean isAuthenticated = (isAuthenticatedTask.isSuccessful() && isAuthenticatedTask.getResult().isAuthenticated());
        if (isAuthenticated) {
            if (!isMigrateHidden()) {
                findViewById(R.id.legacyDesc).setVisibility(View.VISIBLE);
                findViewById(R.id.migrateButton).setVisibility(View.VISIBLE);
                findViewById(R.id.migrateHide).setVisibility(View.VISIBLE);
                findViewById(R.id.startButton).setVisibility(View.VISIBLE);
            } else {
                startGame();
            }
        } else {
            findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
            findViewById(R.id.loginDesc).setVisibility(View.VISIBLE);
            Toast.makeText(this, "Not signed in to Google Play Games", Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean isObbMissing() {
        File[] obbFiles = getObbDir().listFiles();
        return obbFiles == null || obbFiles.length < 2;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == MIGRATE_INTENT && resultCode == Activity.RESULT_OK) {
            if (resultData == null) return;

            Uri uri = resultData.getData();
            DocumentFile documentsTree = DocumentFile.fromTreeUri(this, uri);
            if (documentsTree == null) return;

            int numMigrated = 0;
            for (DocumentFile childDocument : documentsTree.listFiles()) {
                String lname = childDocument.getName().toLowerCase();
                if (lname.contains("save") && lname.endsWith(".rxdata") && childDocument.isFile()) {
                    try {
                        // Copy the file to data directory
                        Path newFile = Paths.get(getFilesDir() + "/" + childDocument.getName());
                        Files.deleteIfExists(newFile);
                        InputStream in = getContentResolver().openInputStream(childDocument.getUri());
                        Files.copy(in, newFile);
                        in.close();
                        Files.setLastModifiedTime(newFile, FileTime.fromMillis(System.currentTimeMillis()));
                        numMigrated++;
                    } catch (Exception e) {
                        Log.e("MKXP", "Failed to migrate file: " + childDocument.getName());
                    }
                }
            }

            Toast.makeText(this, numMigrated + " save files were migrated!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void migrateIntent() {
        new AlertDialog.Builder(this)
                .setTitle("Legacy Save Migration")
                .setMessage("The system dialog will now open. Select the " + legacySavePath() + " folder when prompted. It will have some Save rxdata files.")
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, this.legacySavePath());
                    startActivityForResult(intent, MIGRATE_INTENT);
                }).show();
    }

    protected boolean isMigrateHidden() {
        return getPreferences(MODE_PRIVATE).getBoolean("migrate_legacy_v1_hidden", false);
    }

    protected void setMigrateHidden(boolean val) {
        final SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putBoolean("migrate_legacy_v1_hidden", val);
        editor.commit();
    }

    protected void startGame() {
        Intent intent = new Intent(this, MKXPActivity.class);
        startActivity(intent);
        finish();
    }
}
