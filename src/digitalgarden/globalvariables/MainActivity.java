package digitalgarden.globalvariables;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import digitalgarden.globalvariables.FragOfActivityData.ExternalActivityDataProvider;
import digitalgarden.globalvariables.FragOfFragmentData.ExternalFragmentDataProvider;
import digitalgarden.globalvariables.FragOfPreferenceData.ExternalPreferenceDataProvider;

public class MainActivity extends FragmentActivity 
	implements 
	ExternalActivityDataProvider, 
	ExternalPreferenceDataProvider, 
	ExternalFragmentDataProvider
	{
	/*
	 * Activity-ben tárolt adatok (onSaveInstanceSave tárolja)
	 */
	private int activityData = 0;
	
	public int getActivityData()
		{
		return activityData;
		}
	
	public void setActivityData( int data )
		{
		this.activityData = data;
		}

	public void incActivityData( )
		{
		this.activityData++;
		}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
		{
		super.onSaveInstanceState(outState);
		outState.putInt("DATA", this.activityData);
		}

	// Ez csak onStart UTÁN kerül meghívásra, viszont Bundle soha nem lesz null
	/*
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
		{
		super.onRestoreInstanceState(savedInstanceState);
		activityData = savedInstanceState.getInt( "DATA", 0 );
		}
	*/
	
	
	/*
	 * Preferences-ben tárolt adatok (preferences tárolja)
	 */
	private int preferenceData = 0;
	
	public int getPreferenceData()
		{
		return preferenceData;
		}
	
	public void setPreferenceData( int data )
		{
		this.preferenceData = data;
		}

	public void incPreferenceData( )
		{
		this.preferenceData++;
		}

	@Override
	protected void onPause()
		{
		super.onPause();
		
	    //onPause-ban mentünk (mert később nem biztonságos, de elegendő onCreate-ben beolvasni, mert utána már élnek az adataink.
	    // We need an Editor object to make preference changes.
	    // All objects are from android.context.Context
	    SharedPreferences settings = getSharedPreferences("PREFS", 0);
	    SharedPreferences.Editor editor = settings.edit();
		editor.putInt("PREF_DATA", preferenceData);
	    // Commit the edits!
	    editor.commit();
	    }
	// Visszatöltést ld. onCreate-ben
	
	
	/*
	 * Retained Fragment-ben tárolt adatok
	 */
	private RetainedVariables retainedVariables;
	
	public static class RetainedVariables extends Fragment
		{
		private int fragmentData = 0;
		
		@Override
		public void onCreate(Bundle savedInstanceState) 
			{
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
			}
		}

	public int getFragmentData()
		{
		if (retainedVariables != null)
			return retainedVariables.fragmentData;
		else
			return 0;
		}
	
	public void setFragmentData( int data )
		{
		if (retainedVariables != null)
			retainedVariables.fragmentData = data;
		}
	
	public void incFragmentData( )
		{
		if (retainedVariables != null)
			retainedVariables.fragmentData++;
		}

	
	/*
	 * onCreate-ben minden data elemet incrementálunk
	 * kiv. fragment szint, mert a fragment még itt nincs meg
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);

		if ( savedInstanceState != null )
			activityData = savedInstanceState.getInt( "DATA", 0 );

		// a párja onPause-ban van, de már itt visszaolvasható, mert később nem változik
		SharedPreferences settings = getSharedPreferences("PREFS", 0);
		preferenceData = settings.getInt("PREF_DATA", 0);

		// application szint
		((ApplicationWithGlobals)getApplication()).incApplicationData(); 

		// activity szint
		incActivityData();
		
		// preference szint incrementálás
		incPreferenceData();
		
		// singleton szint
		Singleton.getInstance().incSingletonData();
		
		// fragment szint - itt még nincs meg a fragment!! Csak onResumeFragments után lehet!
		}

	@Override
	protected void onResumeFragments()
		{
		super.onResumeFragments();

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		retainedVariables = (RetainedVariables)fragmentManager.findFragmentByTag( "VAR" );
		if ( retainedVariables == null )
			{
			retainedVariables = new RetainedVariables();
			fragmentTransaction.add( retainedVariables, "VAR" );
			}
		
		// fragment szint incrementálás
		incFragmentData();
		
		
		Fragment fragOfApplicationData = fragmentManager.findFragmentByTag("APPLICATION_DATA_LEVEL");
		if (fragOfApplicationData == null)
			{
			fragOfApplicationData = new FragOfApplicationData();
			fragmentTransaction.add( R.id.main_layout, fragOfApplicationData, "APPLICATION_DATA_LEVEL" );
			}

		Fragment fragOfActivityData = fragmentManager.findFragmentByTag("ACTIVITY_DATA_LEVEL");
		if (fragOfActivityData == null)
			{
			fragOfActivityData = new FragOfActivityData();
			fragmentTransaction.add( R.id.main_layout, fragOfActivityData, "ACTIVITY_DATA_LEVEL" );
			}

		Fragment fragOfPreferenceData = fragmentManager.findFragmentByTag("PREFERENCE_DATA_LEVEL");
		if (fragOfPreferenceData == null)
			{
			fragOfPreferenceData = new FragOfPreferenceData();
			fragmentTransaction.add( R.id.main_layout, fragOfPreferenceData, "PREFERENCE_DATA_LEVEL" );
			}

		Fragment fragOfSingletonData = fragmentManager.findFragmentByTag("SINGLETON_DATA_LEVEL");
		if (fragOfSingletonData == null)
			{
			fragOfSingletonData = new FragOfSingletonData();
			fragmentTransaction.add( R.id.main_layout, fragOfSingletonData, "SINGLETON_DATA_LEVEL" );
			}

		Fragment fragOfFragmentData = fragmentManager.findFragmentByTag("FRAGMENT_DATA_LEVEL");
		if (fragOfFragmentData == null)
			{
			fragOfFragmentData = new FragOfFragmentData();
			fragmentTransaction.add( R.id.main_layout, fragOfFragmentData, "FRAGMENT_DATA_LEVEL" );
			}
		else
			{
			((FragOfFragmentData)fragOfFragmentData).updateValue();
			}

		fragmentTransaction.commit();
		}

	}
