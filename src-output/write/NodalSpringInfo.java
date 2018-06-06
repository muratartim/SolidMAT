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

import node.NodalSpring;

import analysis.Structure;

/**
 * Class for writing spring information to output file.
 * 
 * @author Murat
 * 
 */
public class NodalSpringInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes spring information to output file.
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
			bwriter_.write(header("Nodal Springs, 1"));

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

				// get springs of node
				Vector<NodalSpring> springs = s.getNode(i).getSprings();

				// check if any available
				if (springs != null) {

					// loop over springs of node
					for (int j = 0; j < springs.size(); j++) {

						// get spring
						NodalSpring spring = springs.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = spring.getName();
						if (spring.getCoordinateSystem() == NodalSpring.global_)
							table[2] = "Global";
						else
							table[2] = "Local";
						table[3] = formatter(spring.getStiffness().get(0, 0));
						table[4] = formatter(spring.getStiffness().get(1, 1));

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
			bwriter_.write(header("Nodal Springs, 2"));

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

				// get springs of node
				Vector<NodalSpring> springs = s.getNode(i).getSprings();

				// check if any available
				if (springs != null) {

					// loop over springs of node
					for (int j = 0; j < springs.size(); j++) {

						// get spring
						NodalSpring spring = springs.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = formatter(spring.getStiffness().get(2, 2));
						table[2] = formatter(spring.getStiffness().get(3, 3));
						table[3] = formatter(spring.getStiffness().get(4, 4));
						table[4] = formatter(spring.getStiffness().get(5, 5));

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
