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
