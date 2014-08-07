package com.limemobile.app.utils;

import java.io.File;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class APKUtil {

	public static void runPackage(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.setPackage(pi.packageName);

			List<ResolveInfo> apps = pm.queryIntentActivities(
					resolveIntent, 0);

			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				packageName = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;

				Intent intent = new Intent(Intent.ACTION_MAIN);

				ComponentName cn = new ComponentName(packageName, className);

				intent.setComponent(cn);
				context.startActivity(intent);
			}
		} catch (NameNotFoundException e) {
		}
	}

	public static void removePackage(Context context, String packageName) {
		try {
			Uri packageURI = Uri.parse("package:" + packageName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
			context.startActivity(uninstallIntent);
		} catch (Exception e) {
		}
	}

	public static boolean isPackageInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			if (pi == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public static void installPackage(Context context, String packageName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.fromFile(new File(packageName)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);

	}

	public static String isPackageArchive(Context context, String filePath) {
		PackageManager pm = context.getPackageManager();

		String packageName = null;
		PackageInfo pi = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);

		if (pi != null) {
			ApplicationInfo appInfo = pi.applicationInfo;
			packageName = appInfo.packageName;
			return packageName;
		}
		return packageName;

	}
}
