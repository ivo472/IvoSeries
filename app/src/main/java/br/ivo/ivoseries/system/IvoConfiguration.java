package br.ivo.ivoseries.system;

import br.ivo.ivoseries.R;
import br.ivo.ivoseries.R.xml;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class IvoConfiguration extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.config);
	}
}
