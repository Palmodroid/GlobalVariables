package digitalgarden.globalvariables;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragOfSingletonData extends Fragment 
	{
	public int getExternalData()
		{
		return Singleton.getInstance().getSingletonData();
		}
	
	public void setExternalData( int data )
		{
		Singleton.getInstance().setSingletonData( data );
		}
	
	
	private TextView value;
	private Button reset;
	
	private void updateValue()
		{
		value.setText("Singleton: " + getExternalData() );
		}
	
	// http://stackoverflow.com/questions/5026926/making-sense-of-layoutinflater
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
