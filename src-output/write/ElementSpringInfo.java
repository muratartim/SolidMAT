package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import analysis.Structure;

import element.ElementSpring;

/**
 * Class for writing element spring information to output file.
 * 
 * @author Murat
 * 
 */
public class ElementSpringInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes element spring information to output file.
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
			bwriter_.write(header("Element Springs"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Name", "C-System", "Comp.", "Value" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over elements
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get springs of element
				Vector<ElementSpring> springs = s.getElement(i).getSprings();

				// check if any springs available
				if (springs != null) {

					// loop over springs of element
					for (int j = 0; j < springs.size(); j++) {

						// get spring
						ElementSpring spring = springs.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = spring.getName();
						if (spring.getCoordinateSystem() == ElementSpring.global_)
							table[2] = "Global";
						else
							table[2] = "Local";
						int comp = spring.getComponent();
						int coord = spring.getCoordinateSystem();
						if (comp == ElementSpring.ux_) {
							if (coord == ElementSpring.global_)
								table[3] = "ux";
							else
								table[3] = "u1";
						} else if (comp == ElementSpring.uy_) {
							if (coord == ElementSpring.global_)
								table[3] = "uy";
							else
								table[3] = "u2";
						} else if (comp == ElementSpring.uz_) {
							if (coord == ElementSpring.global_)
								table[3] = "uz";
							else
								table[3] = "u3";
						} else if (comp == ElementSpring.rx_) {
							if (coord == ElementSpring.global_)
								table[3] = "rx";
							else
								table[3] = "r1";
						} else if (comp == ElementSpring.ry_) {
							if (coord == ElementSpring.global_)
								table[3] = "ry";
							else
								table[3] = "r2";
						} else if (comp == ElementSpring.rz_) {
							if (coord == ElementSpring.global_)
								table[3] = "rz";
							else
								table[3] = "r3";
						}
						table[4] = formatter(spring.getValue());

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
