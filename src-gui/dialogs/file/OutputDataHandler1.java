package dialogs.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.OutputData;


/**
 * Class for reading/writing output data object.
 * 
 * @author Murat Artim
 * 
 */
public class OutputDataHandler1 {

	/**
	 * Reads and returns output data object from the given path.
	 * 
	 * @param path
	 *            The path of demanded output data object.
	 * @return Output data object.
	 */
	public static OutputData read(String path) {

		// initialize input stream
		ObjectInputStream in = null;

		// read file
		try {

			// set path for reading output data
			String path1 = path.substring(0, path.length() - 4);
			path1 += ".out";

			// create input stream
			in = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(path1)));

			// read and return output data
			return (OutputData) in.readObject();
		}

		// file not found
		catch (FileNotFoundException e) {
			return null;
		}

		// cannot read file
		catch (IOException e) {
			return null;
		}

		// cannot process file
		catch (ClassNotFoundException e) {
			return null;
		}

		// cannot process file
		catch (ClassCastException e) {
			return null;
		}

		// close input stream
		finally {

			// check if the input stream is null
			if (in != null) {
				try {

					// close
					in.close();
				} catch (IOException io) {
				}
			}
		}
	}

	/**
	 * Writes output data object to given path.
	 * 
	 * @param path
	 *            The path to write the output data object.
	 * @param unknowns
	 *            The unknowns array of output data.
	 * @param steps
	 *            Number of steps.
	 * @return True if no problem occured during writing, False vice versa.
	 */
	public static boolean write(String path, double[][] unknowns, Integer steps) {

		// initialize output stream
		ObjectOutputStream out = null;

		// write to file
		try {

			// set path for saving output data
			String path1 = path.substring(0, path.length() - 4);
			path1 += ".out";

			// create output stream for output data
			out = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(path1)));

			// write output data
			out.writeObject(new OutputData(unknowns, steps));
			return true;
		}

		// cannot write to file
		catch (IOException e) {
			return false;
		}

		// close output stream
		finally {

			// check if the output stream is null
			if (out != null) {
				try {

					// close
					out.close();
				} catch (IOException io) {
				}
			}
		}
	}
}
