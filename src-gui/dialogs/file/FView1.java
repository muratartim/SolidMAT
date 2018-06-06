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
import javax.swing.*;
import javax.swing.filechooser.*;

import main.ImageHandler;

/**
 * Class for file view. Used for supplying file views for files to be opened or
 * saved.
 * 
 * @author Murat Artim
 * 
 */
public class FView1 extends FileView {

	/** The file image for files to be opened or saved. */
	private ImageIcon modelFileImage_ = ImageHandler
			.createImageIcon("SolidMAT2.jpg");

	/**
	 * Returns the description of the file to be opened or saved.
	 * 
	 * @param file
	 *            The file to be checked.
	 * @return The description of the file to be opened or saved.
	 */
	public String getTypeDescription(File file) {

		// get the extension of the file
		String extension = FFilter1.getExtension(file);

		// initialize description of the file
		String description = null;

		// check for no extension
		if (extension != null) {

			// SolidMAT Model File
			if (extension.equals(FFilter1.smt_))
				description = "SolidMAT Model File";
		}

		// return description of file
		return description;
	}

	/**
	 * Returns the icon of the file to be opened or saved.
	 * 
	 * @param file
	 *            The file to be opened or saved.
	 * @return The icon of the file to be opened or saved.
	 */
	public Icon getIcon(File file) {

		// get the extension of the file
		String extension = FFilter1.getExtension(file);

		// initialize icon of the file
		Icon icon = null;

		// check for no extension
		if (extension != null) {

			// SolidMAT Model File
			if (extension.equals(FFilter1.smt_))
				icon = modelFileImage_;
		}

		// return icon of the file
		return icon;
	}

	/**
	 * This operation will be figured out by L&F FileView.
	 */
	public String getName(File f) {
		return null;
	}

	/**
	 * This operation will be figured out by L&F FileView.
	 */
	public String getDescription(File f) {
		return null;
	}

	/**
	 * This operation will be figured out by L&F FileView.
	 */
	public Boolean isTraversable(File f) {
		return null;
	}
}
