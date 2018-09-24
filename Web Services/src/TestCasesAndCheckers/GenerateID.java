package TestCasesAndCheckers;

/**
 * 
 * This Singleton class generates unique IDs for every new record
 */
public class GenerateID {
	
	private static volatile int newID = 0;
	private static volatile GenerateID uniqueInstance = null;

	public static GenerateID getInstance() {
		if (GenerateID.uniqueInstance == null) {
			synchronized(GenerateID.class) {
				if (GenerateID.uniqueInstance == null) {
					GenerateID.uniqueInstance = new GenerateID();
				}
			}
		}
		return GenerateID.uniqueInstance;
	}

	private GenerateID() {
	}
	/**
	 * Generates unique ID for a new record
	 * @param type
	 * @return unique id
	 */
	public static String generateNewID(String type) {
		String formattedID = null;
		++newID;
		formattedID = String.format("%05d", newID);
		if (type.equals("SR"))
			return "SR" + formattedID;
		else if (type.equals("TR"))
			return "TR" + formattedID;
		return "Invalid Type";
	}
}