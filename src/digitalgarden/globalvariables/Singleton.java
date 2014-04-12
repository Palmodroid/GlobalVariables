package digitalgarden.globalvariables;

public class Singleton
	{
	private Singleton() {}
	
	private static Singleton instance;
	
	public static Singleton getInstance()
		{
		if (instance == null)
			instance = new Singleton();
		
		return instance;
		}
	
	
	private int singletonData = 0;
	
	public void setSingletonData( int data )
		{
		this.singletonData = data;
		}
	
	public int getSingletonData( )
		{
		return this.singletonData;
		}

	public void incSingletonData( )
		{
		this.singletonData++;
		}
	}
