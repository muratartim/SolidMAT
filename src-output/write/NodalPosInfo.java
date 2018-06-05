package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import node.Node;

import analysis.Structure;

/**
 * Class for writing nodal positions to output file.
 * 
 * @author Murat
 * 
 */
public class NodalPosInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes nodal posisitions to output file.
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
			bwriter_.write(header("Nodal Positions"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "Coordx", "Coordy", "Coordz" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < structure.getNumberOfNodes(); i++) {

				// get node
				Node n = structure.getNode(i);

				// get properties
				table[0] = Integer.toString(i);
				table[1] = formatter(n.getPosition().get(0));
				table[2] = formatter(n.getPosition().get(1));
				table[3] = formatter(n.getPosition().get(2));

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
