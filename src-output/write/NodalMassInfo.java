package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import node.NodalMass;

import analysis.Structure;

/**
 * Class for writing nodal mass information to output file.
 * 
 * @author Murat
 * 
 */
public class NodalMassInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes nodal mass information to output file.
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
	private void writeTable1(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Nodal Masses, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "Name", "C-System", "ux", "uy" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get masses of node
				Vector<NodalMass> masses = s.getNode(i).getMasses();

				// check if any mass available
				if (masses != null) {

					// loop over masses of node
					for (int j = 0; j < masses.size(); j++) {

						// get mass
						NodalMass mass = masses.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = mass.getName();
						if (mass.getCoordinateSystem() == NodalMass.global_)
							table[2] = "Global";
						else
							table[2] = "Local";
						table[3] = formatter(mass.getMass().get(0, 0));
						table[4] = formatter(mass.getMass().get(1, 1));

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

	/**
	 * Writes table part 2.
	 * 
	 */
	private void writeTable2(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Nodal Masses, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "uz", "rx", "ry", "rz" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get masses of node
				Vector<NodalMass> masses = s.getNode(i).getMasses();

				// check if any mass available
				if (masses != null) {

					// loop over masses of node
					for (int j = 0; j < masses.size(); j++) {

						// get mass
						NodalMass mass = masses.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = formatter(mass.getMass().get(2, 2));
						table[2] = formatter(mass.getMass().get(3, 3));
						table[3] = formatter(mass.getMass().get(4, 4));
						table[4] = formatter(mass.getMass().get(5, 5));

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
