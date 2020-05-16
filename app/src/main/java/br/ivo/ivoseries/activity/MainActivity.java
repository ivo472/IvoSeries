package br.ivo.ivoseries.activity;

import br.ivo.geral.CConnectData;
import br.ivo.ivoseries.R;
import br.ivo.ivoseries.R.id;
import br.ivo.ivoseries.R.layout;
import br.ivo.ivoseries.R.menu;
import br.ivo.ivoseries.R.string;
import br.ivo.ivoseries.fragment.FragmentBaixados;
import br.ivo.ivoseries.fragment.FragmentFavorito;
import br.ivo.ivoseries.fragment.FragmentSearch;
import br.ivo.ivoseries.fragment.FragmentUltimas;
import br.ivo.ivoseries.system.IvoConfiguration;
import br.ivo.ivoseries.system.IvoServiceManager;
import br.ivo.seriessearch.database.DBSeries;
import br.ivo.seriessearch.eztv.CEztvUrls;
import br.ivo.seriessearch.eztv.CTrataListaSeries;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private ProgressDialog progress = null;
	private int miSection = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startService(new Intent(this, IvoServiceManager.class));

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		}
		miSection = number;
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			if( miSection == 4 )
				getMenuInflater().inflate(R.menu.main, menu);
			else
				getMenuInflater().inflate(R.menu.global, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity( new Intent( this, IvoConfiguration.class ) );
			return true;
		}if (id == R.id.check_now) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor editor = prefs.edit();
			editor.putLong("last_time", -1); 
			editor.commit();
		}else if( id == R.id.action_update ){
			progress = ProgressDialog.show(this, getString(R.string.msg_sem_dados_series), getString(R.string.aguarde), false, true);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//String sContent = CConnectData.getContent(CEztvUrls.sPrincipalUrl);		
					CConnectData.downloadFile("https://eztv.ag/js/search_shows1.js","/sdcard/IvoSeriesSearch/eztv-values.htm");
					String sContent = CConnectData.readFile("/sdcard/IvoSeriesSearch/eztv-values.htm");
					CTrataListaSeries eztv = new CTrataListaSeries(sContent);
					DBSeries db = new DBSeries(getApplicationContext());
					db.adicionar(eztv.getList());
					db.close();
					progress.dismiss();
				}
			}).start();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null; 
			if( 1 == getArguments().getInt(ARG_SECTION_NUMBER) ){
				//rootView = inflater.inflate(R.layout.fragment_main, container, false);
				FragmentFavorito favorito = new FragmentFavorito(inflater, container);
				rootView = favorito.getView();
			}else if( 2 == getArguments().getInt(ARG_SECTION_NUMBER) ){
				FragmentUltimas search = new FragmentUltimas(inflater, container);
				rootView = search.getView();
			}else if( 3 == getArguments().getInt(ARG_SECTION_NUMBER) ){
				FragmentBaixados search = new FragmentBaixados(inflater, container);
				rootView = search.getView();
			}else if( 4 == getArguments().getInt(ARG_SECTION_NUMBER) ){
				FragmentSearch search = new FragmentSearch(inflater, container);
				rootView = search.getView();
			}else
				rootView = inflater.inflate(R.layout.fragment_list, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

}
