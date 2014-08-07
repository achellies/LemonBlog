
package edu.mit.mobile.android.appupdater;

import java.util.List;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.limemobile.app.blog.R;

/**
 * A handy pop-up dialog box which lists the changelog and asks if you want to
 * update.
 * 
 * @author steve
 */
public class OnUpdateDialog implements OnAppUpdateListener {
    private final Context mContext;
    private final CharSequence mAppName;
    private Uri downloadUri;
    private final Handler mHandler;
    private static final int MSG_SHOW_DIALOG = 1;
    private Dialog mDialog;

    public OnUpdateDialog(Context context, CharSequence appName) {
        mContext = context;
        mAppName = appName;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SHOW_DIALOG:
                        try {
                            // TODO fix this so it'll pop up appropriately
                            mDialog.show();
                        } catch (final Exception e) {
                            // XXX ignore for the moment
                        }

                        break;
                }
            }
        };
    }

    public void appUpdateStatus(boolean isLatestVersion,
            String latestVersionName, List<String> changelog, Uri downloadUri) {
        this.downloadUri = downloadUri;

        if (!isLatestVersion) {
            final Builder db = new Builder(mContext);
            db.setTitle(mAppName);

            final StringBuilder sb = new StringBuilder();
            sb.append(mContext.getString(R.string.app_update_new_version, latestVersionName,
                    mAppName));
            sb.append("\n\n");
            for (final String item : changelog) {
                sb.append(" • ").append(item).append("\n");
            }

            db.setMessage(sb);

            db.setPositiveButton(R.string.upgrade, dialogOnClickListener);
            db.setNegativeButton(android.R.string.cancel, dialogOnClickListener);
            mDialog = db.create();
            mHandler.sendEmptyMessage(MSG_SHOW_DIALOG);

        }
    }

    private final DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, downloadUri));
            }

        }
    };
}
