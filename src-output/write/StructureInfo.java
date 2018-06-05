package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import analysis.Structure;

/**
 * Class for writing structure information to output file.
 * 
 * @author Murat
 * 
 */
public class StructureInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes node information to output file.
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
	private void writeTable1(Structure structure) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Structure Information"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "# of nd.", "# of el.", "Volume", "Mass",
					"Weight" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write structure info
			table[0] = Integer.toString(structure.getNumberOfNodes());
			table[1] = Integer.toString(structure.getNumberOfElements());
			table[2] = formatter(structure.getVolume());
			table[3] = formatter(structure.getMass());
			table[4] = formatter(structure.getWeight());

			// write
			bwriter_.write(table(table));
			bwriter_.newLine();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}
}
