package main;

/**
 * Class for opening demanded document. Note that this class uses platform
 * specific (for Windows) code.
 * 
 * @author Murat
 * 
 */
public class DocumentHandler {

	/** Static variable for the path of Acrobat Reader. */
	private static final String acroPath_ = "C:\\Program Files\\Adobe\\Acrobat 7.0\\Acrobat\\Acrobat.exe";

	/** Static variable for the openning option of Acrobat Reader. */
	public static final int page_ = 0, search_ = 1;

	/**
	 * Opens the demanded PDF document.
	 * 
	 * @param path
	 *            The path of the document to be opened.
	 * @param option
	 *            The opening option of document.
	 * @param param
	 *            The opening parameter of document.
	 */
	public static void openPDFDocument(String path, int option, String param) {

		try {

			// get document's path
			java.net.URL imgURL = DocumentHandler.class.getResource(path);
			String docPath = imgURL.getPath().substring(1);

			// initialize opening parameter
			String parameter = null;

			// set parameter for page option
			if (option == DocumentHandler.page_)
				parameter = "/A page=" + param + "=OpenActions";

			// set parameter for search option
			else if (option == DocumentHandler.search_)
				parameter = "/A search=" + param + "=OpenActions";

			// open document
			String command = acroPath_ + " " + parameter + " " + docPath;
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
		}

		// exception occurred during execution
		catch (Exception e) {
			System.err.println("Exception occurred during opening document!");
		}
	}
}
