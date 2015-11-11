package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {
    public static final String PREF_KEY_NETWORK_SETTINGS = "pref_key_network_settings";
    public static final String PREF_KEY_IP_ADDRESS = "pref_key_ip_address";
    public static final String PREF_KEY_SECRET_TOKEN = "pref_key_secret_token";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
    }

    public static String getPref(Activity activity, String prefKey) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPref.getString(prefKey, null);
    }

}