package digitalgarden.globalvariables;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragOfPreferenceData extends Fragment 
	{
	// Az activity-vel történő kommunikáció miatt szükséges részek
	private	 ExternalPreferenceDataProvider externalProvider;

	// The container Activity must implement this interface so the frag can deliver messages
	public interface ExternalPreferenceDataProvider 
		{
		public int getPreferenceData();
		
		public void setPreferenceData( int data );
		
		public void incPreferenceData();
		}

	@Override
	public void onAttach(Activity activity)
		{
		super.onAttach(activity);

		try 
			{
			externalProvider = (ExternalPreferenceDataProvider) activity;
			} 
		catch (ClassCastException e) 
			{
			throw new ClassCastException(activity.toString() + " must implement ExternalPreferenceDataProvider");
			}
		}
	
	@Override
	public void onDetach()
		{
		super.onDetach();
		
		externalProvider = null;
		}


	public int getExternalData()
		{
		if ( externalProvider != null )
			return externalProvider.getPreferenceData(); 
		else
			return 0;
		}
	
	public void setExternalData( int data )
		{
		if ( externalProvider != null )
			externalProvider.setPreferenceData( data ); 
		}
	

	private TextView value;
	private Button reset;
	
	private void updateValue()
		{
		value.setText("Preference: " + getExternalData() );
		}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
		{
        View view = inflater.inflate(R.layout.fragment_layout, null); //container, false);
		
        value = (TextView) view.findViewById( R.id.value );
		reset = (Button) view.findViewById(R.id.reset);

        return view;
		}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
		{
		super.onActivityCreated(savedInstanceState);

		updateValue();
		
		reset.setOnClickListener( new View.OnClickListener()
			{
			@Override
			public void onClick( View view )
				{
				setExternalData( 0 );
				updateValue();
				}
			});
		}
	}
