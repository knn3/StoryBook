package com.example.storbook;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Locale;

public class LanguageUtil {
    /*Language Type：
     * 3 language types are supported here, more can be added by yourself.
     * */
    private static final String ENGLISH = "en";
    private static final String CHINESE = "ch";
    private static final String KOREAN = "ko";

    private static HashMap<String, Locale> languagesList = new HashMap<String, Locale>(2) {{
        put(ENGLISH, Locale.ENGLISH);
        put(CHINESE, Locale.CHINESE);
        put(KOREAN, Locale.KOREAN);
        //put(TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
    }};
    /**
     * modify language
     *
     * @param activity
     * @param language For example, modify it to pass "en" in English, see string constants above
     * @param cls      The class to jump to (usually the entry class)
     */
    public static void changeAppLanguage(Activity activity, String language, Class<?> cls) {
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        // app locale Default Simplified Engilsh
        Locale locale = getLocaleByLanguage(StringUtils.isEmpty(language) ? "en" : language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);

        Log.d("language", "setting language："+language);
        //finish();
        // reboot app
        Intent intent = new Intent(activity, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        //loading animation
        //activity.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
        //activity.overridePendingTransition(0, 0);
    }

    /**
     * Get the locale information of the specified language,
     * Returns the native language if the specified language does not exist,
     * or English if the native language is not one of the language sets
     */
    private static Locale getLocaleByLanguage(String language) {
        if (isContainsKeyLanguage(language)) {
            return languagesList.get(language);
        } else {
            Locale locale = Locale.getDefault();
            for (String key : languagesList.keySet()) {
                if (TextUtils.equals(languagesList.get(key).getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
        }
        return Locale.ENGLISH;
    }

    /**
     * Returns true if this mapping contains a mapping relationship for the specified key
     */
    private static boolean isContainsKeyLanguage(String language) {
        return languagesList.containsKey(language);
    }

}
