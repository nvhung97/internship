package com.example.cpu11398_local.languagechange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import java.util.Locale;

/**
 * Created by Hung-pc on 6/2/2018.
 * This module used to manage language of the application
 */

public class LocateManager {

    public static final  String LANGUAGE_ENGLISH    = "en";
    public static final  String LANGUAGE_VIETNAMESE = "vi";
    private static final String LANGUAGE_KEY        = "language_key";

    /**
     * Set language of context from language in SharePreference
     * @param context context need to config language
     * @return context after config language
     */
    public static Context setLocale(Context context) {
        return updateResources(context, getLanguage(context));
    }

    /**
     * Set language of context to new language
     * @param context context need to config language
     * @param language language code
     * @return context after config new language
     */
    public static Context setNewLocale(Context context, String language) {
        persistLanguage(context, language);
        return updateResources(context, language);
    }

    /**
     * Get language code from SharePreference of context
     * @param context context to get language code
     * @return language code
     */
    public static String getLanguage(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);
    }

    /**
     * Update language in SharePreference of context
     * @param context context contain SharePreference
     * @param language language code
     */
    @SuppressLint("ApplySharedPref")
    private static void persistLanguage(Context context, String language) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(LANGUAGE_KEY, language)
                .commit();
    }

    /**
     * Update language in resources of context
     * @param context context contain resources
     * @param language language code
     * @return context after config language
     */
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return context;
    }

    /**
     * Get location of context
     * @param context context need to get locate
     * @return locate
     */
    public static Locale getLocale(Context context) {
        return Build.VERSION.SDK_INT >= 24
                ? context.getResources().getConfiguration().getLocales().get(0)
                : context.getResources().getConfiguration().locale;
    }
}
