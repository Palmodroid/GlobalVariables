package digitalgarden.globalvariables;

import android.app.Application;

public class ApplicationWithGlobals extends Application
	{
	private int applicationData = 0;
	
	public int getApplicationData()
		{
		return applicationData;
		}
	
	public void setApplicationData( int data )
		{
		this.applicationData = data;
		}

	public void incApplicationData( )
		{
		this.applicationData++;
		}
	}
