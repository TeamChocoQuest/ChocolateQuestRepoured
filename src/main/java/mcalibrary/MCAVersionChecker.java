package mcalibrary;

public class MCAVersionChecker {
	public static final int VersionID = 2;
	
	/** Checks for the right version of the library. Should be called by each model class. */
	public static void checkForLibraryVersion(Class modelClass, int modelVersion)
	{
		if(modelVersion > VersionID)
		{
			System.out.println("MCA WARNING: "+modelClass.getName()+" needs a newer version of the library ("+modelVersion+"). Things could go wrong!");
		}
	}
}
