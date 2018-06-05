package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import node.LocalAxis;

import analysis.Structure;

/**
 * Class for writing nodal local axis information to output file.
 * 
 * @author Murat
 * 
 */
public class NodalLocalAxisInfo extends Writer {

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
			bwriter_.write(header("Nodal Local Axes"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "Name", "x", "y'", "z''" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// check if node has local axis
				if (s.getNode(i).getLocalAxis() != null) {

					// get local axis
					LocalAxis la = s.getNode(i).getLocalAxis();

					// get properties
					table[0] = Integer.toString(i);
					table[1] = la.getName();
					table[2] = formatter(la.getValues()[0]);
					table[3] = formatter(la.getValues()[1]);
					table[4] = formatter(la.getValues()[2]);

					// write
					bwriter_.write(table(table));
					bwriter_.newLine();
				}
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}
}
