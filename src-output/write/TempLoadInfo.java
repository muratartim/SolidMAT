package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import boundary.ElementTemp;

import analysis.Structure;

/**
 * Class for writing element temperature load information to output file.
 * 
 * @author Murat
 * 
 */
public class TempLoadInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes element mechanical load information to output file.
	 * 
	 * @param structure
	 *            The structure to be printed.
	 * @param output
	 *            The output file.
	 */
	protected void write(Structure structure, File output) {

		try {

			// create file writer with appending option
			FileWriter writer = new FileWriter(output, true);

			// create buffered writer
			bwriter_ = new BufferedWriter(writer);

			// write tables
			writeTable1(structure);

			// close writer
			bwriter_.close();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table part 1.
	 * 
	 */
	private void writeTable1(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Element Temperature Loads"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Name", "Case", "Value" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over elements
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get temperature loads of element
				Vector<ElementTemp> tempLoads = s.getElement(i)
						.getAllTempLoads();

				// check if any temp loads available
				if (tempLoads != null) {

					// loop over temperature loads of element
					for (int j = 0; j < tempLoads.size(); j++) {

						// get temperature load
						ElementTemp tempLoad = tempLoads.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = tempLoad.getName();
						table[2] = tempLoad.getBoundaryCase().getName();
						table[3] = formatter(tempLoad.getValue());

						// write
						bwriter_.write(table(table));
						bwriter_.newLine();
					}
				}
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}
}
