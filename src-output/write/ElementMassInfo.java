/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import analysis.Structure;

import element.ElementMass;

/**
 * Class for writing element additional mass information to output file.
 * 
 * @author Murat
 * 
 */
public class ElementMassInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes element additional mass information to output file.
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
			bwriter_.write(header("Element Additional Masses"));

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

				// get masses of element
				Vector<ElementMass> masses = s.getElement(i)
						.getAdditionalMasses();

				// check if any masses available
				if (masses != null) {

					// loop over additional masses of element
					for (int j = 0; j < masses.size(); j++) {

						// get mass
						ElementMass mass = masses.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = mass.getName();
						if (mass.getCoordinateSystem() == ElementMass.global_)
							table[2] = "Global";
						else
							table[2] = "Local";
						int comp = mass.getComponent();
						int coord = mass.getCoordinateSystem();
						if (comp == ElementMass.ux_) {
							if (coord == ElementMass.global_)
								table[3] = "ux";
							else
								table[3] = "u1";
						} else if (comp == ElementMass.uy_) {
							if (coord == ElementMass.global_)
								table[3] = "uy";
							else
								table[3] = "u2";
						} else if (comp == ElementMass.uz_) {
							if (coord == ElementMass.global_)
								table[3] = "uz";
							else
								table[3] = "u3";
						} else if (comp == ElementMass.rx_) {
							if (coord == ElementMass.global_)
								table[3] = "rx";
							else
								table[3] = "r1";
						} else if (comp == ElementMass.ry_) {
							if (coord == ElementMass.global_)
								table[3] = "ry";
							else
								table[3] = "r2";
						} else if (comp == ElementMass.rz_) {
							if (coord == ElementMass.global_)
								table[3] = "rz";
							else
								table[3] = "r3";
						}
						table[4] = formatter(mass.getValue());

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
