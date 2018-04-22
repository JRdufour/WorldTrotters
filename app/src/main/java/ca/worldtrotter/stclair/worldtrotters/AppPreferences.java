package ca.worldtrotter.stclair.worldtrotters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.prefs.Preferences;

public class AppPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(android.R.id.content, new SettingsFragment());
        transaction.commit();

    }

    /**
     * Create an inner class of type PreferenceFragment
     * This fragment uses R.xml.preferences to inflate a submenu
     * All the preferences we defined in R.xml.preferences are displayed here
     * The PreferenceActivity and PreferenceFragment will automatically
     * handle all File I/O
     */
    public static class SettingsFragment
            extends PreferenceFragment{

        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preference);
        }
    }
}
