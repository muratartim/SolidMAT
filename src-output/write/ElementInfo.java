package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import analysis.Structure;

import element.*;

/**
 * Class for writing element information to output file.
 * 
 * @author Murat
 * 
 */
public class ElementInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes element information to output file.
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
			writeTable2(structure);

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
	private void writeTable1(Structure structure) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Element Information, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Type", "Volume", "Mass", "Weight" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write element info
			for (int i = 0; i < structure.getNumberOfElements(); i++) {

				// get element
				Element e = structure.getElement(i);

				// get properties
				table[0] = Integer.toString(i);
				table[1] = Integer.toString(e.getType());
				table[2] = formatter(e.getVolume());
				table[3] = formatter(e.getMass());
				table[4] = formatter(e.getWeight());

				// write
				bwriter_.write(table(table));
				bwriter_.newLine();
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table part 2.
	 * 
	 */
	private void writeTable2(Structure structure) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Element Information, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Length", "Area" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write element info
			for (int i = 0; i < structure.getNumberOfElements(); i++) {

				// get element
				Element e = structure.getElement(i);

				// get properties
				table[0] = Integer.toString(i);
				if (e.getDimension() == ElementLibrary.oneDimensional_) {
					Element1D e1D = (Element1D) e;
					table[1] = formatter(e1D.getLength());
				} else
					table[1] = " ";
				if (e.getDimension() == ElementLibrary.twoDimensional_) {
					Element2D e2D = (Element2D) e;
					table[2] = formatter(e2D.getArea());
				} else
					table[2] = " ";

				// write
				bwriter_.write(table(table));
				bwriter_.newLine();
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}
}
