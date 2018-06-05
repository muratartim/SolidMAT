package dialogs.file;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import main.SolidMAT;

import data.InputData;

import analysis.Structure;

/**
 * Class for handling file actions.
 * 
 * @author Murat Artim
 * 
 */
public class FileHandler1 {

	/**
	 * Creates new file.
	 * 
	 * @param owner
	 *            frame of file handler.
	 */
	public static void createNewFile(SolidMAT owner) {

		// initialize messages and captions
		String msg1 = "Save current model?";
		String cap1 = "Data confirmation";

		// structure, input data or path exists
		if (owner.structure_.isEmpty() == false
				|| owner.inputData_.isEmpty() == false || owner.path_ != null) {

			// display confirmation message
			int conf = JOptionPane.showConfirmDialog(owner.viewer_, msg1, cap1,
					JOptionPane.YES_NO_CANCEL_OPTION);

			// save as/save and reset model
			if (conf == JOptionPane.YES_OPTION) {

				// there is no path
				if (owner.path_ == null)
					saveFileAs(owner);

				// path exists
				else
					saveFile(owner);

				// create new file
				createNewFile1(owner);
			}

			// create new file
			else if (conf == JOptionPane.NO_OPTION)
				createNewFile1(owner);
		}
	}

	/**
	 * Opens existing file.
	 * 
	 * @param owner
	 *            frame of file handler.
	 */
	public static void openFile(SolidMAT owner) {

		// initialize messages and captions
		String msg1 = "Save current model?";
		String cap1 = "Data confirmation";

		// structure, input data or path exists
		if (owner.structure_.isEmpty() == false
				|| owner.inputData_.isEmpty() == false || owner.path_ != null) {

			// display confirmation message
			int conf = JOptionPane.showConfirmDialog(owner.viewer_, msg1, cap1,
					JOptionPane.YES_NO_CANCEL_OPTION);

			// save as/save and open file
			if (conf == JOptionPane.YES_OPTION) {

				// there is no path
				if (owner.path_ == null) {

					// save as
					saveFileAs(owner);

					// open file
					openFile1(owner);
				}

				// path exists
				else {

					// save
					saveFile(owner);

					// open file
					openFile1(owner);
				}
			}

			// open file
			else if (conf == JOptionPane.NO_OPTION)
				openFile1(owner);
		}

		// structure, input data and path don't exist
		else
			openFile1(owner);
	}

	/**
	 * Saves existing file.
	 * 
	 * @param owner
	 *            frame of file handler.
	 */
	public static void saveFile(SolidMAT owner) {

		// path exists
		if (owner.path_ != null) {

			// save file
			FileHandler2 dialog = new FileHandler2(owner, FileHandler2.save_,
					owner.path_);
			dialog.setVisible(true);
		}

		// path doesn't exist
		else
			saveFileAs(owner);
	}

	/**
	 * Saves file to a new location.
	 * 
	 * @param owner
	 *            frame of file handler.
	 */
	public static boolean saveFileAs(SolidMAT owner) {

		// get file chooser
		JFileChooser fc = getFileChooser();

		// show file chooser
		int val = fc.showDialog(owner.viewer_, "Save");

		// save approved
		if (val == JFileChooser.APPROVE_OPTION) {

			// get selected file's path
			String path = fc.getSelectedFile().getAbsolutePath();

			// append extension if necessary
			String extension = ".smt";
			if (path.length() >= extension.length() + 1)
				if (extension.equalsIgnoreCase(path
						.substring(path.length() - 4)) == false)
					path += extension;

			// save file
			FileHandler2 dialog = new FileHandler2(owner, FileHandler2.saveAs_,
					path);
			dialog.setVisible(true);
			return true;
		}

		// save not approved
		else
			return false;
	}

	/**
	 * Sub-method of createNewFile.
	 * 
	 * @param owner
	 *            frame of file handler.
	 */
	private static void createNewFile1(SolidMAT owner) {

		// reset structure-input-path
		owner.setInputData(new InputData());
		owner.setStructure(new Structure());
		owner.path_ = null;

		// set name to main frame
		owner.setName();

		// draw
		owner.drawPre();
	}

	/**
	 * Sub-method of openFile.
	 * 
	 * @param owner
	 *            frame of file handler.
	 */
	private static void openFile1(SolidMAT owner) {

		// get file chooser
		JFileChooser fc = getFileChooser();

		// show file chooser
		int val = fc.showDialog(owner.viewer_, "Open");

		// open approved
		if (val == JFileChooser.APPROVE_OPTION) {

			// get selected file's path
			String path = fc.getSelectedFile().getAbsolutePath();

			// open file
			FileHandler2 dialog = new FileHandler2(owner, FileHandler2.open_,
					path);
			dialog.setVisible(true);
		}
	}

	/**
	 * Creates and returns file chooser.
	 * 
	 * @return File chooser.
	 */
	private static JFileChooser getFileChooser() {

		// create file chooser
		JFileChooser chooser = new JFileChooser();

		// add custom file filter
		chooser.addChoosableFileFilter(new FFilter1());

		// disable the default (Accept All) file filter.
		chooser.setAcceptAllFileFilterUsed(false);

		// add custom icons for file types.
		chooser.setFileView(new FView1());

		// return
		return chooser;
	}
}
