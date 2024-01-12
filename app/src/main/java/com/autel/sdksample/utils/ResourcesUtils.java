package com.autel.sdksample.utils;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;


import com.autel.sdksample.util.AutelConfigManager;

import java.util.Arrays;
import java.util.List;

/**
 * <p> 功能描述：资源 工具类<br>
 * <p>
 * 详细描述：
 *
 * @UpdateUser: gss
 * @UpdateDate: 2021/12/30
 * @UpdateRemark: 优化警告问题
 * @Version: 1.0 版本
 */
public final class ResourcesUtils {

    private ResourcesUtils() {
    }

    public static Resources getResources() {
        return AutelConfigManager.instance().getAppContext().getResources();
    }

    public static String getString(int resourceId) {
        return AutelConfigManager.instance().getAppContext().getResources().getString(resourceId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return AutelConfigManager.instance().getAppContext().getResources().getString(resId, formatArgs);
    }

    public static int getColor(int resourceId) {
        return AutelConfigManager.instance().getAppContext().getColor(resourceId);
    }

    public static float getDimension(int resourceId) {
        return AutelConfigManager.instance().getAppContext().getResources().getDimension(resourceId);
    }

    public static int getInteger(int resourceId) {
        return AutelConfigManager.instance().getAppContext().getResources().getInteger(resourceId);
    }

    public static Drawable getDrawable(int resourceId) {
        return ContextCompat.getDrawable(AutelConfigManager.instance().getAppContext(),resourceId);
    }

    public static AssetManager getAsset() {
        return AutelConfigManager.instance().getAppContext().getAssets();
    }

    public static Typeface getTypeface(String dir) {
        return Typeface.createFromAsset(getAsset(), dir);
    }

    public static List<String> getStringArrayAsList(int resId) {
        return Arrays.asList(AutelConfigManager.instance().getAppContext().getResources().getStringArray(resId));
    }

}
