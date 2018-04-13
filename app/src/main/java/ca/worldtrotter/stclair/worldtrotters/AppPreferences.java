package ca.worldtrotter.stclair.worldtrotters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.prefs.Preferences;

public class AppPreferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.simplelayout);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        SettingsFragment settingsFragment = new SettingsFragment();
        transaction.add(android.R.id.content, settingsFragment, "SETTINGS_FRAGMENT");
        transaction.commit();

    }

    //Creating an inner fragment that will launch settings of the app
    public static class  SettingsFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.app_preference);
        }
    }
}
