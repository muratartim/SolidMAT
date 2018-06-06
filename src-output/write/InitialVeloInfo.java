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

import node.Node;
import boundary.InitialVelo;

import analysis.Structure;

/**
 * Class for writing initial velocity information to output file.
 * 
 * @author Murat
 * 
 */
public class InitialVeloInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes initial velocity information to output file.
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
			bwriter_.write(header("Initial velocities, 1"));

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

				// get node
				Node n = s.getNode(i);

				// get initial velocities of node
				Vector<InitialVelo> dl = n.getAllInitialVelo();

				// check if any available
				if (dl != null) {

					// loop over initial velocities of node
					for (int j = 0; j < dl.size(); j++) {

						// get initial velocity
						InitialVelo load = dl.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = load.getName();
						table[2] = load.getBoundaryCase().getName();
						if (load.getCoordinateSystem() == InitialVelo.global_)
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
			bwriter_.write(header("Initial velocities, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "ux", "uy", "uz", "rx" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get node
				Node n = s.getNode(i);

				// get initial velocities of node
				Vector<InitialVelo> dl = n.getAllInitialVelo();

				// check if any available
				if (dl != null) {

					// loop over initial velocities of node
					for (int j = 0; j < dl.size(); j++) {

						// get initial velocity
						InitialVelo load = dl.get(j);

						// set loading scale to 1.0
						load.setLoadingScale(1.0);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = formatter(load.getComponents().get(0));
						table[2] = formatter(load.getComponents().get(1));
						table[3] = formatter(load.getComponents().get(2));
						table[4] = formatter(load.getComponents().get(3));

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
			bwriter_.write(header("Initial velocities, 3"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Node", "ry", "rz" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over nodes
			for (int i = 0; i < s.getNumberOfNodes(); i++) {

				// get node
				Node n = s.getNode(i);

				// get initial velocities of node
				Vector<InitialVelo> dl = n.getAllInitialVelo();

				// check if any available
				if (dl != null) {

					// loop over initial velocities of node
					for (int j = 0; j < dl.size(); j++) {

						// get initial velocity
						InitialVelo load = dl.get(j);

						// set loading scale to 1.0
						load.setLoadingScale(1.0);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = formatter(load.getComponents().get(4));
						table[2] = formatter(load.getComponents().get(5));

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
