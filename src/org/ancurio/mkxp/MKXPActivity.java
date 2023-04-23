package org.ancurio.mkxp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotContents;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataBuffer;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Task;

import org.ancurio.button_mapping.ButtonMappingManager;
import org.ancurio.button_mapping.Helper;
import org.ancurio.button_mapping.VirtualButton;
import org.libsdl.app.SDLActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MKXPActivity extends SDLActivity
{
    private static final String TAG = "mkxp";

    /** Package identifier for save games **/
    protected String savePackageName() {
        return getPackageName();
    }

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

    /** Test if a file is a save game */
    protected boolean isSaveFile(String filename) {
        return filename.toLowerCase().contains("save") && filename.toLowerCase().endsWith(".rxdata");
    }

    /** Get the snapshot name from a filename (save) */
    protected String getSnapshotName(String filename) {
        return savePackageName() + "_" + filename;
    }

    private ButtonMappingManager.InputLayout inputLayout;
    private SnapshotsClient snapshotsClient;

    public static native void nativeExit();

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

        // Init google play games
        snapshotsClient = PlayGames.getSnapshotsClient(this);

        // Load save games
        loadSaveGames();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nativeExit();
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
            case 400:
                updateSaveFiles();
                return true;
        }
        return super.onUnhandledMessage(command, param);
    }

    protected void addButtons(boolean drawText) {
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

    protected void updateSaveFiles() {
        // Read all files in game directory
        File gameDir = new File(getGameDirectory());
        File[] files = gameDir.listFiles();
        if (files == null) {
            return;
        }

        // Check if the file was modified during the last 15 seconds
        // Save the file to the server in this case
        for (File file : files) {
            if (file.isFile() &&
                file.lastModified() > System.currentTimeMillis() - 15000 &&
                isSaveFile(file.getName())) {

                // Read the file
                byte[] data = null;
                try {
                    data = Files.readAllBytes(Paths.get(file.getPath()));
                } catch (IOException e) {
                    Log.e(TAG, "Failed to read file " + file.getName(), e);
                    continue;
                }
                final byte[] finalData = data;

                // Get name of snapshot
                final String snapshotName = getSnapshotName(file.getName());

                // Write the file to the cloud
                snapshotsClient.open(snapshotName,true).addOnSuccessListener(res -> {
                    if (res.getData() == null) {
                        Log.d(TAG, "Conflict detected for file " + file.getName());
                        return;
                    }

                    writeSnapshot(res.getData(), finalData, "Save file").addOnSuccessListener(metadata -> {
                        Log.d(TAG, "Saved file " + snapshotName);
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to save file " + snapshotName, e);
                    });
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to open remote file " + snapshotName, e);
                });
            }
        }
    }

    private Task<SnapshotMetadata> writeSnapshot(Snapshot snapshot, byte[] data, String desc) {
        // Set the data payload for the snapshot
        snapshot.getSnapshotContents().writeBytes(data);

        // Create the change operation
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                .setDescription(desc)
                .build();

        // Commit the operation
        return snapshotsClient.commitAndClose(snapshot, metadataChange);
    }

    protected void loadSaveGames() {
        // List all save games in remote server
        snapshotsClient.load(true).addOnSuccessListener(res -> {
            final SnapshotMetadataBuffer buffer = res.get();
            if (buffer == null) {
                return;
            }

            for (SnapshotMetadata metadata : buffer) {
                // Get the name of the file
                String snapshotName = metadata.getUniqueName();
                
                // Get index of undescore
                int index = snapshotName.lastIndexOf("_");
                if (index == -1) {
                    continue;
                }

                // Split at the underscore
                String packageName = snapshotName.substring(0, index);
                String filename = snapshotName.substring(index + 1);
                if (!packageName.equals(savePackageName())) {
                    continue;
                }

                // Check local file
                File file = new File(getGameDirectory() + "/" + filename);
                if (file.isFile() && metadata.getLastModifiedTimestamp() - file.lastModified() < 30000) {
                    Log.d(TAG, "Skipping remote file since local version is fresh: " + filename);
                    continue;
                }

                // Load the remote file to local
                Log.d(TAG, "Loading remote file: " + filename);

                snapshotsClient.open(snapshotName, true).addOnSuccessListener(sres -> {
                    final Snapshot snapshot = sres.getData();
                    if (snapshot == null) {
                        return;
                    }

                    // Read the file
                    byte[] data = null;
                    try {
                        SnapshotContents contents = snapshot.getSnapshotContents();
                        data = contents.readFully();
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read file " + filename, e);
                        return;
                    }

                    // Write the file
                    String dest = getGameDirectory() + "/" + filename;
                    try {
                        Files.write(Paths.get(dest), data);
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to write file " + filename, e);
                    }
                    Log.i(TAG, "Copied remote file " + filename);
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to open remote file " + snapshotName, e);
                });
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to list remote files", e);
        });
    }

    protected void copyAssets() {
        AssetManager assetManager = getAssets();
        for (String asset : getAssetsToCopy()) {
            InputStream in = null;
            try {
                Path dest = Paths.get(getGameDirectory() + "/" + asset);
                in = assetManager.open(asset);
                Files.deleteIfExists(dest);
                Files.copy(in, dest);
            } catch(IOException e) {
                Log.e(TAG, "Failed to copy asset file: " + asset, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
}
