package solver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import matrix.Mat;
import matrix.DVec;
import matrix.USB2Mat;

/**
 * Class for direct-symmetric-Gauss elimination linear equation solver. It can
 * be used for solving linear system of equations (A x = b). It is an
 * implementation of Gauss elimination procedure. This class uses an external
 * executable file called "SYMSOLVR.exe".
 * 
 * @author Murat Artim
 * 
 */
public class Solver2 extends LESolver {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates symmetric Gauss elimination solver.
	 * 
	 * @param name
	 *            The name of solver.
	 */
	public Solver2(String name) {
		setName(name);
	}

	@Override
	public int getSolutionType() {
		return LESolver.direct_;
	}

	@Override
	public int getSolverType() {
		return LESolver.GaussSymm_;
	}

	@Override
	public int getStorageType() {
		return Solver.USB2S_;
	}

	@Override
	public int getType() {
		return Solver.solver2_;
	}

	@Override
	public void initialize(Mat matA, DVec vecX) {
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
		USB2Mat a = (USB2Mat) matA;

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

			// row count of coefficient matrix
			bwriter.write(Integer.toString(a.rowCount()));
			bwriter.newLine();

			// column count of coefficient matrix
			bwriter.write(Integer.toString(a.getHalfBandwidth() + 1));
			bwriter.newLine();

			// number of equations
			bwriter.write(Integer.toString(a.rowCount()));
			bwriter.newLine();

			// half bandwidth
			bwriter.write(Integer.toString(a.getHalfBandwidth() + 1));
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
			file = new File("solvers/SYMSOLVR.exe");

			// solver exists
			if (file.exists()) {

				// execute solver
				Process p = Runtime.getRuntime().exec("solvers/SYMSOLVR.exe");
				p.waitFor();

				// get output file
				file = new File("OUTPUT.txt");

				// output file exists
				if (file.exists()) {

					// create file reader
					FileReader reader = new FileReader(file);

					// create buffered reader
					breader = new BufferedReader(reader);

					// reallocate marices/vectors
					a.reAllocate();
					vecX.reAllocate();

					// read matrix - a
					breader.readLine();
					for (int i = 0; i < a.rowCount(); i++)
						for (int j = 0; j < a.getHalfBandwidth() + 1; j++)
							a.setToInt(i, j, Double.parseDouble(breader
									.readLine().trim()));

					// check if successfull exit
					String exit = breader.readLine();
					if (exit.equalsIgnoreCase("*** SUCCESSFULL EXIT ***")) {

						// read # of equations
						breader.readLine();
						int neq = Integer.parseInt(breader.readLine().trim());

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
	private void write(USB2Mat a, DVec b, BufferedWriter bwriter) {
		try {

			// matrix - a
			bwriter.write("M A T R I X  -  A");
			bwriter.newLine();
			for (int i = 0; i < a.rowCount(); i++) {
				for (int j = 0; j < a.getHalfBandwidth() + 1; j++) {
					bwriter.write(Double.toString(a.getData()[i][j]));
					bwriter.newLine();
				}
			}

			// vector - b
			bwriter.write("V E C T O R  -  B");
			bwriter.newLine();
			for (int i = 0; i < b.rowCount(); i++) {
				bwriter.write(Double.toString(b.get(i)));
				bwriter.newLine();
			}
		}

		// exception occured during writing input file
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing input file!");
		}
	}
}
