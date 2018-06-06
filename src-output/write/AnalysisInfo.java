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

import analysis.*;
import math.Function;

/**
 * Class for writing analysis information to output file.
 * 
 * @author Murat
 * 
 */
public class AnalysisInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes node information to output file.
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

			// get analysis info and type
			Vector<Object> info = structure.getAnalysisInfo();
			int type = (Integer) info.get(1);

			// write tables
			if (type == Analysis.linearStatic_) {
				writeTable1(info);
				writeTable2(info);
			} else if (type == Analysis.modal_) {
				writeTable3(info);
				writeTable4(info);
			} else if (type == Analysis.linearTransient_) {
				writeTable5(info);
				writeTable6(info);
				writeTable7(info);
			} else if (type == Analysis.linearBuckling_) {
				writeTable8(info);
				writeTable9(info);
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
	 * Writes table for linear static analysis, 1.
	 * 
	 */
	private void writeTable1(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Analysis Information, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Name", "Type", "Solver" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write structure info
			table[0] = (String) info.get(0);
			table[1] = "LStatic";
			table[2] = (String) info.get(4);

			// write
			bwriter_.write(table(table));
			bwriter_.newLine();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table for linear static analysis, 2.
	 * 
	 */
	private void writeTable2(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Analysis Information, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "B-Cases", "B-scales" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// get info
			String[] names = (String[]) info.get(2);
			double[] scales = (double[]) info.get(3);

			// loop over cases
			for (int i = 0; i < names.length; i++) {

				// set info
				table[0] = names[i];
				table[1] = formatter(scales[i]);

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
	 * Writes table for modal analysis, 1.
	 * 
	 */
	private void writeTable3(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Analysis Information, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Name", "Type", "Solver", "B-Cases" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write info
			table[0] = (String) info.get(0);
			table[1] = "Modal";
			table[2] = (String) info.get(3);

			// get first boundary case
			String[] names = (String[]) info.get(2);
			if (names.length != 0)
				table[3] = names[0];

			// write
			bwriter_.write(table(table));
			bwriter_.newLine();

			// loop over other boundary cases
			table = new String[5];
			table[0] = "";
			table[1] = "";
			table[2] = "";
			table[3] = "";
			for (int i = 1; i < names.length; i++) {

				// write info
				table[3] = names[i];

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
	 * Writes table for modal analysis, 2.
	 * 
	 */
	private void writeTable4(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for second part of table
			bwriter_.write(header("Analysis Information, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Mode", "Eigen", "NF", "CF", "T" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// get info
			double[] eig = (double[]) info.get(4);
			double[] nf = (double[]) info.get(5);
			double[] cf = (double[]) info.get(6);
			double[] per = (double[]) info.get(7);

			// loop over modes
			for (int i = 0; i < eig.length; i++) {

				// get properties
				table[0] = Integer.toString(i);
				table[1] = formatter(eig[i]);
				table[2] = formatter(nf[i]);
				table[3] = formatter(cf[i]);
				table[4] = formatter(per[i]);

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
	 * Writes table for transient analysis, 1.
	 * 
	 */
	private void writeTable5(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Analysis Information, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Name", "Type", "Solver", "# of steps", "DT" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write info
			table[0] = (String) info.get(0);
			table[1] = "LTransient";
			table[2] = (String) info.get(4);
			table[3] = Integer.toString((Integer) info.get(5));
			table[4] = formatter((Double) info.get(6));

			// write
			bwriter_.write(table(table));
			bwriter_.newLine();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table for transient analysis, 2.
	 * 
	 */
	private void writeTable6(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Analysis Information, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Int-Met", "L-Func", "Stiff-fac", "Mass-fac",
					"Int-Par1" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write info
			int method = (Integer) info.get(7);
			if (method == LinearTransient.newmark_)
				table[0] = "Newmark";
			else if (method == LinearTransient.wilson_)
				table[0] = "Wilson";
			Function func = (Function) info.get(10);
			table[1] = func.getName();
			double[] damp = (double[]) info.get(9);
			table[2] = formatter(damp[0]);
			table[3] = formatter(damp[1]);
			double[] param = (double[]) info.get(8);
			table[4] = formatter(param[0]);

			// write
			bwriter_.write(table(table));
			bwriter_.newLine();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table for transient analysis, 3.
	 * 
	 */
	private void writeTable7(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Analysis Information, 3"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Int-Par2", "B-Cases", "B-Scales" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write info
			double[] param = (double[]) info.get(8);
			int method = (Integer) info.get(7);
			if (method == LinearTransient.newmark_)
				table[0] = formatter(param[1]);

			// get first boundary case and scale
			String[] names = (String[]) info.get(2);
			double[] scales = (double[]) info.get(3);
			if (names.length != 0) {
				table[1] = names[0];
				table[2] = formatter(scales[0]);
			}

			// write
			bwriter_.write(table(table));
			bwriter_.newLine();

			// loop over other boundary cases
			table = new String[4];
			table[0] = "";
			for (int i = 1; i < names.length; i++) {

				// write info
				table[1] = names[i];
				table[2] = formatter(scales[i]);

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
	 * Writes table for linear buckling analysis, 1.
	 * 
	 */
	private void writeTable8(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Analysis Information, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Name", "Type", "Solver", "B-Cases", "B-Scales" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write info
			table[0] = (String) info.get(0);
			table[1] = "LBuckling";
			table[2] = (String) info.get(4);

			// get first boundary case and scale
			String[] names = (String[]) info.get(2);
			double[] scales = (double[]) info.get(3);
			if (names.length != 0) {
				table[3] = names[0];
				table[4] = formatter(scales[0]);
			}

			// write
			bwriter_.write(table(table));
			bwriter_.newLine();

			// loop over other boundary cases
			table = new String[5];
			table[0] = "";
			table[1] = "";
			table[2] = "";
			table[3] = "";
			table[4] = "";
			for (int i = 1; i < names.length; i++) {

				// write info
				table[3] = names[i];
				table[4] = formatter(scales[i]);

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
	 * Writes table for linear buckling analysis, 2.
	 * 
	 */
	private void writeTable9(Vector<Object> info) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for second part of table
			bwriter_.write(header("Analysis Information, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Mode", "L-Factor" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// get info
			double[] eig = (double[]) info.get(5);

			// loop over modes
			for (int i = 0; i < eig.length; i++) {

				// get properties
				table[0] = Integer.toString(i);
				table[1] = formatter(eig[i]);

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
