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

import node.Node;

import matrix.DVec;

import analysis.Structure;

/**
 * Class for writing joint displacements information to output file.
 * 
 * @author Murat
 * 
 */
public class NodalDispInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/** The coordinate system of demanded displacements. */
	private int coordinateSystem_;

	/**
	 * Creates DisplacementInfo object.
	 * 
	 * @param coordinateSystem
	 *            The coordinate system of demanded displacements.
	 */
	public NodalDispInfo(int coordinateSystem) {
		coordinateSystem_ = coordinateSystem;
	}

	/**
	 * Writes nodal displacements information to output file.
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
				bwriter_.write(header("Global Displacements/Rotations, 1"));
			else if (coordinateSystem_ == Node.local_)
				bwriter_.write(header("Local Displacements/Rotations, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = new String[5];
			table[0] = "Node";
			if (coordinateSystem_ == Node.global_) {
				table[1] = "ux";
				table[2] = "uy";
				table[3] = "uz";
				table[4] = "rx";
			} else if (coordinateSystem_ == Node.local_) {
				table[1] = "u1";
				table[2] = "u2";
				table[3] = "u3";
				table[4] = "r1";
			}
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write values
			for (int i = 0; i < structure.getNumberOfNodes(); i++) {

				// get node
				Node n = structure.getNode(i);

				// get displacement vector
				DVec vec = new DVec(6);
				if (coordinateSystem_ == Node.global_)
					vec = n.getUnknown(Node.global_);
				else if (coordinateSystem_ == Node.local_)
					vec = n.getUnknown(Node.local_);

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
				bwriter_.write(header("Global Displacements/Rotations, 2"));
			else if (coordinateSystem_ == Node.local_)
				bwriter_.write(header("Local Displacements/Rotations, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = new String[3];
			table[0] = "Node";
			if (coordinateSystem_ == Node.global_) {
				table[1] = "ry";
				table[2] = "rz";
			} else if (coordinateSystem_ == Node.local_) {
				table[1] = "r2";
				table[2] = "r3";
			}
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write values
			for (int i = 0; i < structure.getNumberOfNodes(); i++) {

				// get node
				Node n = structure.getNode(i);

				// get displacement vector
				DVec vec = new DVec(6);
				if (coordinateSystem_ == Node.global_)
					vec = n.getUnknown(Node.global_);
				else if (coordinateSystem_ == Node.local_)
					vec = n.getUnknown(Node.local_);

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
