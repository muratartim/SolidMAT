package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import node.Node;

import boundary.Constraint;

import analysis.Structure;

/**
 * Class for writing constraint information to output file.
 * 
 * @author Murat
 * 
 */
public class ConstraintInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes constraint information to output file.
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
			bwriter_.write(header("Constraints, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "Name", "Case", "u1", "u2" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get node
				Node n = s.getNode(i);

				// node has constraint
				if (n.getAppliedConstraint() != null) {

					// get constraint
					Constraint c = n.getAppliedConstraint();

					// get properties
					table[0] = Integer.toString(i);
					table[1] = c.getName();
					table[2] = c.getBoundaryCase().getName();
					table[3] = Boolean.toString(c.getConstraints()[0]);
					table[4] = Boolean.toString(c.getConstraints()[1]);

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

	/**
	 * Writes table part 2.
	 * 
	 */
	private void writeTable2(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Constraints, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "u3", "r1", "r2", "r3" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get node
				Node n = s.getNode(i);

				// node has constraint
				if (n.getAppliedConstraint() != null) {

					// get constraint
					Constraint c = n.getAppliedConstraint();

					// get properties
					table[0] = Integer.toString(i);
					table[1] = Boolean.toString(c.getConstraints()[2]);
					table[2] = Boolean.toString(c.getConstraints()[3]);
					table[3] = Boolean.toString(c.getConstraints()[4]);
					table[4] = Boolean.toString(c.getConstraints()[5]);

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
