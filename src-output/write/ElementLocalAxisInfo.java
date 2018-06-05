package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import node.LocalAxis;
import element.Element;
import element.Element1D;
import element.ElementLibrary;

import analysis.Structure;

/**
 * Class for writing element local axis information to output file.
 * 
 * @author Murat
 * 
 */
public class ElementLocalAxisInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes local axis information to output file.
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
			bwriter_.write(header("Element Local Axes"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Name", "Rotation" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over elements
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get element
				Element e = s.getElement(i);

				// check if element is one dimensional
				if (e.getDimension() == ElementLibrary.oneDimensional_) {

					// get one dimensional element
					Element1D e1D = (Element1D) e;

					// check if element has local axis
					if (e1D.getLocalAxis() != null) {

						// get local axis
						LocalAxis la = e1D.getLocalAxis();

						// get properties
						table[0] = Integer.toString(i);
						table[1] = la.getName();
						table[2] = formatter(la.getValues()[0]);

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
