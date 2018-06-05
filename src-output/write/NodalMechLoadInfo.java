package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import boundary.NodalMechLoad;

import analysis.Structure;

/**
 * Class for writing nodal mechanical load information to output file.
 * 
 * @author Murat
 * 
 */
public class NodalMechLoadInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes nodal mechanical load information to output file.
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
			writeTable3(structure);

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
			bwriter_.write(header("Nodal Mechanical Loads, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "Name", "Case", "C-System" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get mechanical loads of node
				Vector<NodalMechLoad> mechLoads = s.getNode(i)
						.getAllMechLoads();

				// check if any
				if (mechLoads != null) {

					// loop over mechanical loads of node
					for (int j = 0; j < mechLoads.size(); j++) {

						// get mechanical load
						NodalMechLoad mechLoad = mechLoads.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = mechLoad.getName();
						table[2] = mechLoad.getBoundaryCase().getName();
						if (mechLoad.getCoordinateSystem() == NodalMechLoad.global_)
							table[3] = "Global";
						else
							table[3] = "Local";

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
			bwriter_.write(header("Nodal Mechanical Loads, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "fx", "fy", "fz", "mx" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get mechanical loads of node
				Vector<NodalMechLoad> mechLoads = s.getNode(i)
						.getAllMechLoads();

				// check if any
				if (mechLoads != null) {

					// loop over mechanical loads of node
					for (int j = 0; j < mechLoads.size(); j++) {

						// get mechanical load
						NodalMechLoad mechLoad = mechLoads.get(j);

						// set loading scale to 1.0
						mechLoad.setLoadingScale(1.0);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = formatter(mechLoad.getComponents().get(0));
						table[2] = formatter(mechLoad.getComponents().get(1));
						table[3] = formatter(mechLoad.getComponents().get(2));
						table[4] = formatter(mechLoad.getComponents().get(3));

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
	 * Writes table part 3.
	 * 
	 */
	private void writeTable3(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Nodal Mechanical Loads, 3"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "my", "mz" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get mechanical loads of node
				Vector<NodalMechLoad> mechLoads = s.getNode(i)
						.getAllMechLoads();

				// check if any
				if (mechLoads != null) {

					// loop over mechanical loads of node
					for (int j = 0; j < mechLoads.size(); j++) {

						// get mechanical load
						NodalMechLoad mechLoad = mechLoads.get(j);

						// set loading scale to 1.0
						mechLoad.setLoadingScale(1.0);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = formatter(mechLoad.getComponents().get(4));
						table[2] = formatter(mechLoad.getComponents().get(5));

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
