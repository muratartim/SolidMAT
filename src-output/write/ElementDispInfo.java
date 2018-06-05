package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import matrix.DVec;

import analysis.Structure;

import element.Element;
import element.Element1D;
import element.Element2D;
import element.Element3D;
import element.ElementLibrary;

/**
 * Class for writing element local approximated displacements information to
 * output file.
 * 
 * @author Murat
 * 
 */
public class ElementDispInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/** Vector for storing one dimensional elements. */
	private Vector<Element1D> element1D_ = new Vector<Element1D>();

	/** Vector for storing two dimensional elements. */
	private Vector<Element2D> element2D_ = new Vector<Element2D>();

	/** Vector for storing three dimensional elements. */
	private Vector<Element3D> element3D_ = new Vector<Element3D>();

	/** Vector for storing ids of one dimensional elements. */
	private Vector<Integer> element1DId_ = new Vector<Integer>();

	/** Vector for storing ids of two dimensional elements. */
	private Vector<Integer> element2DId_ = new Vector<Integer>();

	/** Vector for storing ids of three dimensional elements. */
	private Vector<Integer> element3DId_ = new Vector<Integer>();

	/** The number of output stations. */
	private int stations_;

	/**
	 * Creates ElementDispInfo object.
	 * 
	 * @param stations
	 *            The number of output stations.
	 */
	public ElementDispInfo(int stations) {
		stations_ = stations;
	}

	/**
	 * Writes element local approximated displacements information to output
	 * file.
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

			// store three dimensional elements
			else if (e.getDimension() == ElementLibrary.threeDimensional_) {
				element3D_.add((Element3D) e);
				element3DId_.add(i);
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
			}

			// write tables for three dimensional elements
			if (element3D_.size() != 0)
				writeTable5();

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
					.write(header("Element displacements (One dimensional elements), 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Station", "u1", "u2", "u3" };
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

					// get element displacement vector
					DVec disp = e.getDisplacement(eps1[j], 0.0, 0.0);

					// write components
					table[2] = formatter(disp.get(0));
					table[3] = formatter(disp.get(1));
					table[4] = formatter(disp.get(2));

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
					.write(header("Element displacements (One dimensional elements), 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Station", "r1", "r2", "r3" };
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

					// get element displacement vector
					DVec disp = e.getDisplacement(eps1[j], 0.0, 0.0);

					// write components
					table[2] = formatter(disp.get(3));
					table[3] = formatter(disp.get(4));
					table[4] = formatter(disp.get(5));

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
					.write(header("Element displacements (Two dimensional elements), 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Corner", "u1", "u2", "u3" };
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

					// get element displacement vector
					DVec disp = e.getDisplacement(eps1[j], eps2[j], 0.0);

					// write components
					table[2] = formatter(disp.get(0));
					table[3] = formatter(disp.get(1));
					table[4] = formatter(disp.get(2));

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
					.write(header("Element displacements (Two dimensional elements), 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Corner", "r1", "r2", "r3" };
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

					// get element displacement vector
					DVec disp = e.getDisplacement(eps1[j], eps2[j], 0.0);

					// write components
					table[2] = formatter(disp.get(3));
					table[3] = formatter(disp.get(4));
					table[4] = formatter(disp.get(5));

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
	 * Writes table for three dimensional elements.
	 * 
	 */
	private void writeTable5() {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_
					.write(header("Element displacements (Three dimensional elements)"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Vertex", "u1", "u2", "u3" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write element info
			for (int i = 0; i < element3D_.size(); i++) {

				// get element
				Element3D e = element3D_.get(i);

				// compute corner points in natural coordinates
				double[] eps1 = null;
				double[] eps2 = null;
				double[] eps3 = null;
				if (e.getGeometry() == Element3D.hexahedral_) {
					eps1 = new double[8];
					eps2 = new double[8];
					eps3 = new double[8];
					eps1[0] = 1.0;
					eps2[0] = 1.0;
					eps3[0] = 1.0;
					eps1[1] = -1.0;
					eps2[1] = 1.0;
					eps3[1] = 1.0;
					eps1[2] = -1.0;
					eps2[2] = -1.0;
					eps3[2] = 1.0;
					eps1[3] = 1.0;
					eps2[3] = -1.0;
					eps3[3] = 1.0;
					eps1[4] = 1.0;
					eps2[4] = 1.0;
					eps3[4] = -1.0;
					eps1[5] = -1.0;
					eps2[5] = 1.0;
					eps3[5] = -1.0;
					eps1[6] = -1.0;
					eps2[6] = -1.0;
					eps3[6] = -1.0;
					eps1[7] = 1.0;
					eps2[7] = -1.0;
					eps3[7] = -1.0;
				} else if (e.getGeometry() == Element3D.tetrahedral_) {
					eps1 = new double[4];
					eps2 = new double[4];
					eps3 = new double[4];
					eps1[0] = 0.0;
					eps2[0] = 0.0;
					eps3[0] = 0.0;
					eps1[1] = 1.0;
					eps2[1] = 0.0;
					eps3[1] = 0.0;
					eps1[2] = 0.0;
					eps2[2] = 1.0;
					eps3[2] = 0.0;
					eps1[3] = 0.0;
					eps2[3] = 0.0;
					eps3[3] = 1.0;
				}

				// loop over vertex points
				for (int j = 0; j < eps1.length; j++) {

					// write element id and station number
					table[0] = Integer.toString(element3DId_.get(i));
					table[1] = Integer.toString(j);

					// get element displacement vector
					DVec disp = e.getDisplacement(eps1[j], eps2[j], eps3[j]);

					// write components
					table[2] = formatter(disp.get(0));
					table[3] = formatter(disp.get(1));
					table[4] = formatter(disp.get(2));

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
