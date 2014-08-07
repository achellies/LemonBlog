
package com.limemobile.app.blog.activity.imagecrop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.activity.EditUserInfoActivity;
import com.limemobile.app.blog.activity.ThemeActivity;
import com.limemobile.app.blog.activity.theme.Theme;
import com.limemobile.app.blog.constant.ITransKey;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageCropActivity<T extends Activity> extends ThemeActivity implements OnClickListener {
    private Uri mImageCaptureUri;
    private ViewGroup camera;
    private ViewGroup gallery;
    private View back;
    private String capturePath;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            capturePath = intent.getStringExtra(ITransKey.KEY);
        }
        if (TextUtils.isEmpty(capturePath)) {
            finish();
            return;
        }
        File output = new File(capturePath);
        mImageCaptureUri = Uri.fromFile(output);
        setContentView(R.layout.activity_imagecrop);
        initViews();
    }

    @Override
    public void initViews() {
        camera = (ViewGroup) findViewById(R.id.choose_image_camera_container);
        gallery = (ViewGroup) findViewById(R.id.choose_image_gallery_container);
        back = findViewById(R.id.back);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void themeChanged() {
        Resources res = Theme.getInstance().getContext(this).getResources();
        back.setBackgroundDrawable(res.getDrawable(R.drawable.title_back));
        camera.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_top));
        gallery.setBackgroundDrawable(res.getDrawable(R.drawable.circle_list_bottom));
    }

    private void return2caller(boolean captured) {
        if (captured) {
            Intent intent = new Intent(this, EditUserInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra(ITransKey.KEY, capturePath);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.choose_image_camera_container:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                try {
                    intent.putExtra("return-data", true);

                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.choose_image_gallery_container:
                intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(intent,
                                getResources().getString(R.string.choose_image_gallery)),
                        PICK_FROM_FILE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (resultCode == RESULT_OK)
                    doCrop(mImageCaptureUri);
                break;

            case PICK_FROM_FILE:
                if (resultCode == RESULT_OK) {
                    doCrop(data.getData());
                }

                break;

            case CROP_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();

                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        if (photo != null && !photo.isRecycled()) {
                            File file = new File(mImageCaptureUri.getPath());
                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                photo.compress(Bitmap.CompressFormat.PNG, 90, out);
                                return2caller(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;

        }
    }

    private void doCrop(Uri capteredUri) {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            return2caller(true);
            return;
        } else {
            intent.setData(capteredUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName,
                            res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(),
                        cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }
}
