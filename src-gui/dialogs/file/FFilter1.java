/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dialogs.file;

import java.io.File;
import javax.swing.filechooser.*;

/**
 * Class for file filter. Used for filtering the model files to be opened or
 * saved.
 * 
 * @author Murat Artim
 * 
 */
public class FFilter1 extends FileFilter {

	/** Static variable for the available extension type. */
	public final static String smt_ = "smt";

	/**
	 * Checks whether the given file is acceptable or not.
	 * 
	 * @param file
	 *            The file to be checked.
	 * @return True if acceptable, False if not.
	 */
	public boolean accept(File file) {

		// accept all directories
		if (file.isDirectory())
			return true;

		// get the extension of the file
		String extension = getExtension(file);

		// check for no extension
		if (extension != null) {

			// return true if extension is acceptable
			if (extension.equals(FFilter1.smt_))
				return true;

			// return false if not
			else
				return false;
		}

		// no extension found
		return false;
	}

	/**
	 * Returns the description of the acceptable files.
	 * 
	 * @return The description of the acceptable files.
	 */
	public String getDescription() {
		return "SolidMAT Model Files (*.SMT)";
	}

	/**
	 * Returns the extension of the given file.
	 * 
	 * @param file
	 *            The file to be checked.
	 * @return The extension of the file.
	 */
	public static String getExtension(File file) {

		// initialize extension
		String extension = null;

		// get name of the file
		String fileName = file.getName();

		// get the last index of '.'
		int i = fileName.lastIndexOf('.');

		// append extension of the file
		if (i > 0 && i < fileName.length() - 1)
			extension = fileName.substring(i + 1).toLowerCase();

		// return extension
		return extension;
	}
}
