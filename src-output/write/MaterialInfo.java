package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import material.*;

import analysis.Structure;

/**
 * Class for writing material information to output file.
 * 
 * @author Murat
 * 
 */
public class MaterialInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes material information to output file.
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
			writeTable4(structure);

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
			bwriter_.write(header("Material Properties, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Name", "Type", "UM", "UW" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over elements
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get material
				Material m = s.getElement(i).getMaterial();

				// get general properties
				table[0] = Integer.toString(i);
				table[1] = m.getName();
				table[3] = formatter(m.getVolumeMass());
				table[4] = formatter(m.getVolumeWeight());

				// for isotropic materials
				if (m.getType() == Material.isotropic_)
					table[2] = "Iso.";

				// for orthotropic materials
				else if (m.getType() == Material.orthotropic_)
					table[2] = "Ort.";

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
	private void writeTable2(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for second part of table
			bwriter_.write(header("Material Properties, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "E1", "E2", "E3", "V12" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write material info
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get material
				Material m = s.getElement(i).getMaterial();

				// get element id
				table[0] = Integer.toString(i);

				// for isotropic materials
				if (m.getType() == Material.isotropic_) {
					Isotropic m1 = (Isotropic) m;
					table[1] = formatter(m1.getElasticModulus());
					table[2] = formatter(m1.getElasticModulus());
					table[3] = formatter(m1.getElasticModulus());
					table[4] = formatter(m1.getPoisson());
				}

				// for orthotropic materials
				else if (m.getType() == Material.orthotropic_) {
					Orthotropic m1 = (Orthotropic) m;
					table[1] = formatter(m1.getElasticModulus()[0]);
					table[2] = formatter(m1.getElasticModulus()[1]);
					table[3] = formatter(m1.getElasticModulus()[2]);
					table[4] = formatter(m1.getPoisson()[0]);
				}

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
	 * Writes table part 3.
	 * 
	 */
	private void writeTable3(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for second part of table
			bwriter_.write(header("Material Properties, 3"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "V13", "V23", "G12", "G13" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write material info
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get material
				Material m = s.getElement(i).getMaterial();

				// get element id
				table[0] = Integer.toString(i);

				// for isotropic materials
				if (m.getType() == Material.isotropic_) {
					Isotropic m1 = (Isotropic) m;
					table[1] = formatter(m1.getPoisson());
					table[2] = formatter(m1.getPoisson());
					table[3] = formatter(m1.getShearModulus());
					table[4] = formatter(m1.getShearModulus());
				}

				// for orthotropic materials
				else if (m.getType() == Material.orthotropic_) {
					Orthotropic m1 = (Orthotropic) m;
					table[1] = formatter(m1.getPoisson()[1]);
					table[2] = formatter(m1.getPoisson()[2]);
					table[3] = formatter(m1.getShearModulus()[0]);
					table[4] = formatter(m1.getShearModulus()[1]);
				}

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
	 * Writes table part 4.
	 * 
	 */
	private void writeTable4(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for second part of table
			bwriter_.write(header("Material Properties, 4"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Name", "G23", "A1", "A2", "A3" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write material info
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get material
				Material m = s.getElement(i).getMaterial();

				// get element id
				table[0] = Integer.toString(i);

				// for isotropic materials
				if (m.getType() == Material.isotropic_) {
					Isotropic m1 = (Isotropic) m;
					table[1] = formatter(m1.getShearModulus());
					table[2] = formatter(m1.getThermalExpansion());
					table[3] = formatter(m1.getThermalExpansion());
					table[4] = formatter(m1.getThermalExpansion());
				}

				// for orthotropic materials
				else if (m.getType() == Material.orthotropic_) {
					Orthotropic m1 = (Orthotropic) m;
					table[1] = formatter(m1.getShearModulus()[2]);
					table[2] = formatter(m1.getThermalExpansion()[0]);
					table[3] = formatter(m1.getThermalExpansion()[1]);
					table[4] = formatter(m1.getThermalExpansion()[2]);
				}

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
