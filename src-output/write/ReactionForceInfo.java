package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import node.Node;

import matrix.DVec;

import analysis.Structure;

/**
 * Class for writing joint reaction force information to output file.
 * 
 * @author Murat
 * 
 */
public class ReactionForceInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/** The coordinate system of demanded reaction forces. */
	private int coordinateSystem_;

	/**
	 * Creates ReactionForceInfo object.
	 * 
	 * @param coordinateSystem
	 *            The coordinate system of demanded reaction forces.
	 */
	public ReactionForceInfo(int coordinateSystem) {
		coordinateSystem_ = coordinateSystem;
	}

	/**
	 * Writes nodal reaction forces information to output file.
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
			if (coordinateSystem_ == Node.global_)
				bwriter_.write(header("Global Reaction Forces, 1"));
			else if (coordinateSystem_ == Node.local_)
				bwriter_.write(header("Local Reaction Forces, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = new String[5];
			table[0] = "Node";
			if (coordinateSystem_ == Node.global_) {
				table[1] = "fx";
				table[2] = "fy";
				table[3] = "fz";
				table[4] = "mx";
			} else if (coordinateSystem_ == Node.local_) {
				table[1] = "f1";
				table[2] = "f2";
				table[3] = "f3";
				table[4] = "m1";
			}
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write values
			for (int i = 0; i < structure.getNumberOfNodes(); i++) {

				// get node
				Node n = structure.getNode(i);

				// get reaction force vector
				DVec vec = new DVec(6);
				if (coordinateSystem_ == Node.global_)
					vec = n.getReactionForce(Node.global_);
				else if (coordinateSystem_ == Node.local_)
					vec = n.getReactionForce(Node.local_);

				// write to table
				table[0] = Integer.toString(i);
				table[1] = formatter(vec.get(0));
				table[2] = formatter(vec.get(1));
				table[3] = formatter(vec.get(2));
				table[4] = formatter(vec.get(3));

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
			if (coordinateSystem_ == Node.global_)
				bwriter_.write(header("Global Reaction Forces, 2"));
			else if (coordinateSystem_ == Node.local_)
				bwriter_.write(header("Local Reaction Forces, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = new String[3];
			table[0] = "Node";
			if (coordinateSystem_ == Node.global_) {
				table[1] = "my";
				table[2] = "mz";
			} else if (coordinateSystem_ == Node.local_) {
				table[1] = "m2";
				table[2] = "m3";
			}
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write values
			for (int i = 0; i < structure.getNumberOfNodes(); i++) {

				// get node
				Node n = structure.getNode(i);

				// get reaction force vector
				DVec vec = new DVec(6);
				if (coordinateSystem_ == Node.global_)
					vec = n.getReactionForce(Node.global_);
				else if (coordinateSystem_ == Node.local_)
					vec = n.getReactionForce(Node.local_);

				// write to table
				table[0] = Integer.toString(i);
				table[1] = formatter(vec.get(4));
				table[2] = formatter(vec.get(5));

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
