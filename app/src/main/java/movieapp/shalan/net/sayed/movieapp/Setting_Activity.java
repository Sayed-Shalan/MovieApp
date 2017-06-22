package movieapp.shalan.net.sayed.movieapp;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;

public class Setting_Activity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //start onCreate for this Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);

        //set Activity theme
        setTheme(R.style.settings_theme);


        //add screenPreference to setting activity
        addPreferencesFromResource(R.xml.pref_movie);
        
        //create method to bind summary of Sort ListPreference to its value
        bindSummaryToValue(findPreference(getString(R.string.pref_sort_key)));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //method to bind a summary for this prefernce
    private void bindSummaryToValue(Preference preference) {

        //add changeListner for the preference
        preference.setOnPreferenceChangeListener(this);
        //call the onPreferenceChange method and pass preference and its value for it
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(),""));
    }

    //called when change value of the preference
    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {

        //get pref current value
        String pref_value=o.toString();

        //check if this preference is ListPreference
        if(preference instanceof ListPreference)
        {
            ListPreference sortPref=(ListPreference)preference;
            int pref_index=sortPref.findIndexOfValue(pref_value);
            if(pref_index>=0)
            {
                preference.setSummary(sortPref.getEntries()[pref_index]);

            }
        }
        else
        {
            preference.setSummary(pref_value);
        }
        return true;
    }
}
