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
package main;

import java.io.File;

import javax.swing.ImageIcon;

/**
 * Class for returning every kind of image to be displayed.
 * 
 * @author Murat
 * 
 */
public class ImageHandler {

	/**
	 * Returns an ImageIcon, or null if the path is invalid.
	 * 
	 * @param path
	 *            The path of the icon to be displayed.
	 * @return ImageIcon.
	 */
	public static ImageIcon createImageIcon(String path) {

		// create file
		File file = new File("images/" + path);

		// file exists
		if (file.exists()) {

			// return image
			return new ImageIcon("images/" + path);
		}

		// file does not exist
		System.err.println("Image File Not Found: " + path);
		return null;
	}
}
