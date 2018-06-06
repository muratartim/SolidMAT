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

import element.Element;
import element.Element1D;
import element.Element2D;
import element.ElementLibrary;

/**
 * Class for writing internal force information to output file.
 * 
 * @author Murat
 * 
 */
public class InternalForceInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/** Vector for storing one dimensional elements. */
	private Vector<Element1D> element1D_ = new Vector<Element1D>();

	/** Vector for storing two dimensional elements. */
	private Vector<Element2D> element2D_ = new Vector<Element2D>();

	/** Vector for storing ids of one dimensional elements. */
	private Vector<Integer> element1DId_ = new Vector<Integer>();

	/** Vector for storing ids of two dimensional elements. */
	private Vector<Integer> element2DId_ = new Vector<Integer>();

	/** The number of output stations. */
	private int stations_;

	/**
	 * Creates InternalForceInfo object.
	 * 
	 * @param stations
	 *            The number of output stations.
	 */
	public InternalForceInfo(int stations) {
		stations_ = stations;
	}

	/**
	 * Writes internal force information to output file.
	 * 
	 * @param structure
	 *            The structure to be printed.
	 * @param output
	 *            The output file.
	 */
	protected void write(Structure structure, File output) {

		// store elements
		for (int i = 0; i < structure.getNumberOfElements(); i++) {

			// get element
			Element e = structure.getElement(i);

			// store one dimensional elements
			if (e.getDimension() == ElementLibrary.oneDimensional_) {
				element1D_.add((Element1D) e);
				element1DId_.add(i);
			}

			// store two dimensional elements
			else if (e.getDimension() == ElementLibrary.twoDimensional_) {
				element2D_.add((Element2D) e);
				element2DId_.add(i);
			}
		}

		try {

			// create file writer with appending option
			FileWriter writer = new FileWriter(output, true);

			// create buffered writer
			bwriter_ = new BufferedWriter(writer);

			// write tables for one dimensional elements
			if (element1D_.size() != 0) {
				writeTable1();
				writeTable2();
			}

			// write tables for two dimensional elements
			if (element2D_.size() != 0) {
				writeTable3();
				writeTable4();
				writeTable5();
			}

			// close writer
			bwriter_.close();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table for one dimensional elements part 1.
	 * 
	 */
	private void writeTable1() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_
					.write(header("Internal Forces (One dimensional elements), 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Station", "n", "v2", "v3" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// compute stationary points in natural coordinates
			double distance = 2.0 / (stations_ - 1.0);
			double[] eps1 = new double[stations_];
			eps1[0] = -1.0;
			for (int i = 0; i < stations_ - 1; i++)
				eps1[i + 1] = eps1[i] + distance;

			// write element info
			for (int i = 0; i < element1D_.size(); i++) {

				// get element
				Element1D e = element1D_.get(i);

				// loop over demanded number of output stations
				for (int j = 0; j < stations_; j++) {

					// write element id and station number
					table[0] = Integer.toString(element1DId_.get(i));
					table[1] = Integer.toString(j);

					// write components
					table[2] = formatter(e.getInternalForce(Element1D.N1_,
							eps1[j], 0.0, 0.0));
					table[3] = formatter(e.getInternalForce(Element1D.V2_,
							eps1[j], 0.0, 0.0));
					table[4] = formatter(e.getInternalForce(Element1D.V3_,
							eps1[j], 0.0, 0.0));

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
	 * Writes table for one dimensional elements part 2.
	 * 
	 */
	private void writeTable2() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_
					.write(header("Internal Forces (One dimensional elements), 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Station", "t", "m2", "m3" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// compute stationary points in natural coordinates
			double distance = 2.0 / (stations_ - 1.0);
			double[] eps1 = new double[stations_];
			eps1[0] = -1.0;
			for (int i = 0; i < stations_ - 1; i++)
				eps1[i + 1] = eps1[i] + distance;

			// write element info
			for (int i = 0; i < element1D_.size(); i++) {

				// get element
				Element1D e = element1D_.get(i);

				// loop over demanded number of output stations
				for (int j = 0; j < stations_; j++) {

					// write element id and station number
					table[0] = Integer.toString(element1DId_.get(i));
					table[1] = Integer.toString(j);

					// write components
					table[2] = formatter(e.getInternalForce(Element1D.T1_,
							eps1[j], 0.0, 0.0));
					table[3] = formatter(e.getInternalForce(Element1D.M2_,
							eps1[j], 0.0, 0.0));
					table[4] = formatter(e.getInternalForce(Element1D.M3_,
							eps1[j], 0.0, 0.0));

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
	 * Writes table for two dimensional elements part 1.
	 * 
	 */
	private void writeTable3() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_
					.write(header("Internal Forces (Two dimensional elements), 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Corner", "p11", "n22", "q12" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write element info
			for (int i = 0; i < element2D_.size(); i++) {

				// get element
				Element2D e = element2D_.get(i);

				// compute corner points in natural coordinates
				double[] eps1 = null;
				double[] eps2 = null;
				if (e.getGeometry() == Element2D.quadrangular_) {
					eps1 = new double[4];
					eps2 = new double[4];
					eps1[0] = -1.0;
					eps1[1] = 1.0;
					eps1[2] = 1.0;
					eps1[3] = -1.0;
					eps2[0] = -1.0;
					eps2[1] = -1.0;
					eps2[2] = 1.0;
					eps2[3] = 1.0;
				} else if (e.getGeometry() == Element2D.triangular_) {
					eps1 = new double[3];
					eps2 = new double[3];
					eps1[0] = 1.0;
					eps1[1] = 0.0;
					eps1[2] = 0.0;
					eps2[0] = 0.0;
					eps2[1] = 1.0;
					eps2[2] = 0.0;
				}

				// loop over corner points
				for (int j = 0; j < eps1.length; j++) {

					// write element id and station number
					table[0] = Integer.toString(element2DId_.get(i));
					table[1] = Integer.toString(j);

					// write components
					table[2] = formatter(e.getInternalForce(Element2D.P11_,
							eps1[j], eps2[j], 0.0));
					table[3] = formatter(e.getInternalForce(Element2D.N22_,
							eps1[j], eps2[j], 0.0));
					table[4] = formatter(e.getInternalForce(Element2D.Q12_,
							eps1[j], eps2[j], 0.0));

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
	 * Writes table for two dimensional elements part 2.
	 * 
	 */
	private void writeTable4() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_
					.write(header("Internal Forces (Two dimensional elements), 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Corner", "f13", "h23", "k22" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write element info
			for (int i = 0; i < element2D_.size(); i++) {

				// get element
				Element2D e = element2D_.get(i);

				// compute corner points in natural coordinates
				double[] eps1 = null;
				double[] eps2 = null;
				if (e.getGeometry() == Element2D.quadrangular_) {
					eps1 = new double[4];
					eps2 = new double[4];
					eps1[0] = -1.0;
					eps1[1] = 1.0;
					eps1[2] = 1.0;
					eps1[3] = -1.0;
					eps2[0] = -1.0;
					eps2[1] = -1.0;
					eps2[2] = 1.0;
					eps2[3] = 1.0;
				} else if (e.getGeometry() == Element2D.triangular_) {
					eps1 = new double[3];
					eps2 = new double[3];
					eps1[0] = 1.0;
					eps1[1] = 0.0;
					eps1[2] = 0.0;
					eps2[0] = 0.0;
					eps2[1] = 1.0;
					eps2[2] = 0.0;
				}

				// loop over demanded number of output stations
				for (int j = 0; j < eps1.length; j++) {

					// write element id and station number
					table[0] = Integer.toString(element2DId_.get(i));
					table[1] = Integer.toString(j);

					// write components
					table[2] = formatter(e.getInternalForce(Element2D.F13_,
							eps1[j], eps2[j], 0.0));
					table[3] = formatter(e.getInternalForce(Element2D.H23_,
							eps1[j], eps2[j], 0.0));
					table[4] = formatter(e.getInternalForce(Element2D.K22_,
							eps1[j], eps2[j], 0.0));

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
	 * Writes table for two dimensional elements part 2.
	 * 
	 */
	private void writeTable5() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_
					.write(header("Internal Forces (Two dimensional elements), 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Corner", "m11", "t12" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write element info
			for (int i = 0; i < element2D_.size(); i++) {

				// get element
				Element2D e = element2D_.get(i);

				// compute corner points in natural coordinates
				double[] eps1 = null;
				double[] eps2 = null;
				if (e.getGeometry() == Element2D.quadrangular_) {
					eps1 = new double[4];
					eps2 = new double[4];
					eps1[0] = -1.0;
					eps1[1] = 1.0;
					eps1[2] = 1.0;
					eps1[3] = -1.0;
					eps2[0] = -1.0;
					eps2[1] = -1.0;
					eps2[2] = 1.0;
					eps2[3] = 1.0;
				} else if (e.getGeometry() == Element2D.triangular_) {
					eps1 = new double[3];
					eps2 = new double[3];
					eps1[0] = 1.0;
					eps1[1] = 0.0;
					eps1[2] = 0.0;
					eps2[0] = 0.0;
					eps2[1] = 1.0;
					eps2[2] = 0.0;
				}

				// loop over demanded number of output stations
				for (int j = 0; j < eps1.length; j++) {

					// write element id and station number
					table[0] = Integer.toString(element2DId_.get(i));
					table[1] = Integer.toString(j);

					// write components
					table[2] = formatter(e.getInternalForce(Element2D.M11_,
							eps1[j], eps2[j], 0.0));
					table[3] = formatter(e.getInternalForce(Element2D.T12_,
							eps1[j], eps2[j], 0.0));

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
