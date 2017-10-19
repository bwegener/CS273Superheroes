package edu.orangecoastcollege.cs273.bwegener.cs273superheroes;

import android.preference.PreferenceFragment;
import android.support .annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * This is the <code>SettingsActivity</code> it creates the settings
 * where the user can switch what type of quiz they take
 *
 * @author Brian Wegener
 * @version 1.0
 *
 * Created by bwegener on 10/12/2017
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * This is where the settings menu is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsActivityFragment())
                .commit();
    }

    /**
     * This is the settings activity fragment
     */
    public static class SettingsActivityFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }
    }
}
