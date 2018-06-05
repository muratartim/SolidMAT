package solver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import matrix.Mat;
import matrix.USB1Mat;
import matrix.DVec;

/**
 * Class for direct-active column-linear equation solver. It can be used for
 * solving linear system of equations (A x = b). It is an implementation of
 * Gauss elimination procedure. This class uses an external executable file
 * called "COLSOL.exe".
 * 
 * @author Murat Artim
 * 
 */
public class Solver1 extends LESolver {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates active column solver.
	 * 
	 * @param name
	 *            The name of solver.
	 */
	public Solver1(String name) {
		setName(name);
	}

	@Override
	public int getSolutionType() {
		return LESolver.direct_;
	}

	@Override
	public int getSolverType() {
		return LESolver.COLSOL_;
	}

	@Override
	public int getStorageType() {
		return Solver.USB1S_;
	}

	@Override
	public int getType() {
		return Solver.solver1_;
	}

	@Override
	public void initialize(Mat matA, DVec vecX) {

		// cast matrix
		USB1Mat a = (USB1Mat) matA;

		// initialize buffered writer
		BufferedWriter bwriter = null;

		try {

			// create input file
			File input = new File("INPUT.txt");

			// create file writer
			FileWriter writer = new FileWriter(input);

			// create buffered writer
			bwriter = new BufferedWriter(writer);

			// write header to input
			bwriter.write("P A R A M E T E R S");
			bwriter.newLine();

			// number of equations
			bwriter.write(Integer.toString(a.rowCount()));
			bwriter.newLine();

			// number of elements below skyline of matrix
			bwriter.write(Integer.toString(a.getData().length));
			bwriter.newLine();

			// dimension of adresses array
			bwriter.write(Integer.toString(a.getAdresses().length));
			bwriter.newLine();

			// triangularization of stiffness matrix
			bwriter.write(Integer.toString(1));
			bwriter.newLine();

			// write matrices/vectors
			write(a, vecX, bwriter);

			// delete marices/vectors
			a.delete();
			vecX.delete();
		}

		// exception occured during writing input file
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing input file!");
		}

		// close writer
		finally {

			// check if the writer is null
			if (bwriter != null) {
				try {

					// close writer
					bwriter.close();
				} catch (IOException io) {
				}
			}
		}

		// initialize buffered reader and file
		BufferedReader breader = null;
		File file = null;

		try {

			// create file
			file = new File("solvers/COLSOL.exe");

			// solver exists
			if (file.exists()) {

				// execute solver
				Process p = Runtime.getRuntime().exec("solvers/COLSOL.exe");
				p.waitFor();

				// get output file
				file = new File("OUTPUT.txt");

				// output file exists
				if (file.exists()) {

					// create file reader
					FileReader reader = new FileReader(file);

					// create buffered reader
					breader = new BufferedReader(reader);

					// check if successfull exit
					String exit = breader.readLine();
					if (exit.equalsIgnoreCase("*** SUCCESSFULL EXIT ***")) {

						// reallocate marices/vectors
						a.reAllocate();
						vecX.reAllocate();

						// read # of equations
						breader.readLine();
						breader.readLine();

						// read matrix - a
						breader.readLine();
						for (int i = 0; i < a.getData().length; i++)
							a.set(i, Double.parseDouble(breader.readLine()
									.trim()));
					}

					// solve failed
					else
						exceptionHandler("Solve failed!");
				}

				// output file doesn't exist
				else
					exceptionHandler("Solve failed!");
			}

			// solver file doesn't exist
			else
				exceptionHandler("Exception occurred during solver execution!");
		}

		// exception occurred during execution
		catch (Exception e) {
			exceptionHandler("Solve failed!");
		}

		// close reader
		finally {

			// check if the reader is null
			if (breader != null) {
				try {

					// close writer
					breader.close();
				} catch (IOException io) {
				}
			}

			// check if the file is null
			if (file != null) {

				// delete output file
				file.delete();
			}
		}
	}

	/**
	 * Solves system of linear equations (A x = b).
	 * 
	 * @param matA
	 *            Coefficient matrix (A). Has to be in USB1 form (assumed
	 *            positive definite).
	 * @param vecB
	 *            Right hand side vector (b). Has to be dense vector (DVec).
	 * @param vecX
	 *            Solution vector (x). Has to be dense vector (DVec).
	 */
	public void solve(Mat matA, DVec vecB, DVec vecX) {

		// cast matrix
		USB1Mat a = (USB1Mat) matA;

		// initialize buffered writer
		BufferedWriter bwriter = null;

		try {

			// create input file
			File input = new File("INPUT.txt");

			// create file writer
			FileWriter writer = new FileWriter(input);

			// create buffered writer
			bwriter = new BufferedWriter(writer);

			// write header to input
			bwriter.write("P A R A M E T E R S");
			bwriter.newLine();

			// number of equations
			bwriter.write(Integer.toString(a.rowCount()));
			bwriter.newLine();

			// number of elements below skyline of matrix
			bwriter.write(Integer.toString(a.getData().length));
			bwriter.newLine();

			// dimension of adresses array
			bwriter.write(Integer.toString(a.getAdresses().length));
			bwriter.newLine();

			// reduction and back-substitution of load vector
			bwriter.write(Integer.toString(2));
			bwriter.newLine();

			// write matrices/vectors
			write(a, vecB, bwriter);

			// delete matrix/vector
			a.delete();
			vecX.delete();
		}

		// exception occured during writing input file
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing input file!");
		}

		// close writer
		finally {

			// check if the writer is null
			if (bwriter != null) {
				try {

					// close writer
					bwriter.close();
				} catch (IOException io) {
				}
			}
		}

		// initialize buffered reader and file
		BufferedReader breader = null;
		File file = null;

		try {

			// create file
			file = new File("solvers/COLSOL.exe");

			// solver exists
			if (file.exists()) {

				// execute solver
				Process p = Runtime.getRuntime().exec("solvers/COLSOL.exe");
				p.waitFor();

				// get output file
				file = new File("OUTPUT.txt");

				// output file exists
				if (file.exists()) {

					// create file reader
					FileReader reader = new FileReader(file);

					// create buffered reader
					breader = new BufferedReader(reader);

					// check if successfull exit
					String exit = breader.readLine();
					if (exit.equalsIgnoreCase("*** SUCCESSFULL EXIT ***")) {

						// reallocate marices/vectors
						a.reAllocate();
						vecX.reAllocate();

						// read # of equations
						breader.readLine();
						int neq = Integer.parseInt(breader.readLine().trim());

						// read matrix - a
						breader.readLine();
						for (int i = 0; i < a.getData().length; i++)
							a.set(i, Double.parseDouble(breader.readLine()
									.trim()));

						// read displacements
						breader.readLine();
						for (int i = 0; i < neq; i++)
							vecX.set(i, Double.parseDouble(breader.readLine()
									.trim()));
					}

					// solve failed
					else
						exceptionHandler("Solve failed!");
				}

				// output file doesn't exist
				else
					exceptionHandler("Solve failed!");
			}

			// solver file doesn't exist
			else
				exceptionHandler("Exception occurred during solver execution!");
		}

		// exception occurred during execution
		catch (Exception e) {
			exceptionHandler("Solve failed!");
		}

		// close reader
		finally {

			// check if the reader is null
			if (breader != null) {
				try {

					// close writer
					breader.close();
				} catch (IOException io) {
				}
			}

			// check if the file is null
			if (file != null) {

				// delete output file
				file.delete();
			}
		}
	}

	@Override
	public void clear() {
	}

	/**
	 * Writes matrices and vectors for input file.
	 * 
	 * @param a
	 *            Matrix A.
	 * @param b
	 *            Vector b.
	 * @param bwriter
	 *            Writer for input file.
	 */
	private void write(USB1Mat a, DVec b, BufferedWriter bwriter) {
		try {

			// matrix - a
			bwriter.write("M A T R I X  -  A");
			bwriter.newLine();
			for (int i = 0; i < a.getData().length; i++) {
				bwriter.write(Double.toString(a.getData()[i]));
				bwriter.newLine();
			}

			// vector - b
			bwriter.write("V E C T O R  -  B/X");
			bwriter.newLine();
			for (int i = 0; i < b.rowCount(); i++) {
				bwriter.write(Double.toString(b.get(i)));
				bwriter.newLine();
			}

			// vector containing adresses of diagonal elements of a
			bwriter.write("M A X A   V E C T O R");
			bwriter.newLine();
			for (int i = 0; i < a.getAdresses().length; i++) {
				bwriter.write(Integer.toString(a.getAdresses()[i] + 1));
				bwriter.newLine();
			}
		}

		// exception occured during writing input file
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing input file!");
		}
	}
}
