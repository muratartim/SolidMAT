package solver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import matrix.GPSMat;
import matrix.DVec;
import matrix.USB1Mat;
import matrix.Mat;

/**
 * Class for iterative generalized eigenproblem solver. It solves for the
 * smallest eigenvalues (assumed > 0) and corresponding eigenvectors in the
 * generalized eigenproblem using the subspace iteration method. This class uses
 * an external executable file called "SSPACE.exe".
 * 
 * @author Murat Artim
 * 
 */
public class Solver3 extends GESolver {

	private static final long serialVersionUID = 1L;

	/** Number of required eigenvalues and eigenvectors. */
	private int nRoot_;

	/** Convergence tolerance on eigenvalues (1.e-06 or smaller). */
	private double rTol_;

	/**
	 * Maximum number of subspace iterations permitted (usually set to 16). It
	 * must be increased if a solution has not converged.
	 */
	private int nitem_;

	/**
	 * Creates subspace iterative solver.
	 * 
	 * @param name
	 *            The name of solver.
	 * @param nRoot
	 *            Number of required eigenvalues and eigenvectors.
	 * @param rTol
	 *            Convergence tolerance on eigenvalues (1.e-06 or smaller).
	 * @param nitem
	 *            Maximum number of subspace iterations permitted (usually set
	 *            to 16). It must be increased if a solution has not converged.
	 */
	public Solver3(String name, int nRoot, double rTol, int nitem) {

		// set name
		setName(name);

		// check parameters
		checkParameters(nRoot, rTol, nitem);

		// set parameters
		nRoot_ = nRoot;
		rTol_ = rTol;
		nitem_ = nitem;
	}

	/**
	 * Returns maximum number of permitted iterations.
	 * 
	 * @return Maximum number of permitted iterations.
	 */
	public int getMaxNumberOfIterations() {
		return nitem_;
	}

	@Override
	public int getNumberOfRequiredEigenvalues() {
		return nRoot_;
	}

	@Override
	public double getConvergenceTolerance() {
		return rTol_;
	}

	@Override
	public int getStorageType() {
		return Solver.USB1S_;
	}

	@Override
	public int getType() {
		return Solver.solver3_;
	}

	@Override
	public int getSolverType() {
		return GESolver.SubSpace_;
	}

	@Override
	public void initialize(Mat matA, Mat matB) {

		// cast matrices
		USB1Mat a = (USB1Mat) matA;
		USB1Mat b = (USB1Mat) matB;

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

			// order of matrices a and b
			bwriter.write(Integer.toString(a.columnCount()));
			bwriter.newLine();

			// dimension of adresses array
			bwriter.write(Integer.toString(a.getAdresses().length));
			bwriter.newLine();

			// number of elements below skyline of matrix a
			bwriter.write(Integer.toString(a.getData().length));
			bwriter.newLine();

			// number of elements below skyline of matrix b
			bwriter.write(Integer.toString(b.getData().length));
			bwriter.newLine();

			// number of required eigenvalues and eigenvectors
			int nz = b.getNumberOfNZDElements();
			if (nRoot_ > nz)
				nRoot_ = nz;
			bwriter.write(Integer.toString(nRoot_));
			bwriter.newLine();

			// convergence tolerance on eigenvalues
			bwriter.write(Double.toString(rTol_));
			bwriter.newLine();

			// number of iteration vectors used
			int nc = Math.min(2 * nRoot_, nRoot_ + 8);
			if (nc > nz)
				nc = nz;
			bwriter.write(Integer.toString(nc));
			bwriter.newLine();

			// dimension of storage vectors ar, br
			int nnc = nc * (nc + 1) / 2;
			bwriter.write(Integer.toString(nnc));
			bwriter.newLine();

			// maximum number of subspace iterations permitted
			bwriter.write(Integer.toString(nitem_));
			bwriter.newLine();

			// matrix - a
			bwriter.write("M A T R I X  -  A");
			bwriter.newLine();
			for (int i = 0; i < a.getData().length; i++) {
				bwriter.write(Double.toString(a.getData()[i]));
				bwriter.newLine();
			}

			// matrix - b
			bwriter.write("M A T R I X  -  B");
			bwriter.newLine();
			for (int i = 0; i < b.getData().length; i++) {
				bwriter.write(Double.toString(b.getData()[i]));
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
	}

	@Override
	public void solve(Mat matA, Mat matB, DVec eigval, GPSMat eigvec,
			int nEigval) {

		// cast matrices
		USB1Mat a = (USB1Mat) matA;
		USB1Mat b = (USB1Mat) matB;

		// delete all input
		a.delete();
		b.delete();
		eigval.delete();
		eigvec.delete();

		// initialize buffered reader and file
		BufferedReader breader = null;
		File file = null;

		try {

			// create file
			file = new File("solvers/SSPACE.exe");

			// solver exists
			if (file.exists()) {

				// execute solver
				Process p = Runtime.getRuntime().exec("solvers/SSPACE.exe");
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

						// reallocate input
						a.reAllocate();
						b.reAllocate();
						eigval.reAllocate();
						eigvec.reAllocate();

						// read # of eigenvalues found
						breader.readLine();
						nEigval = Integer.parseInt(breader.readLine().trim());

						// read # of equations
						breader.readLine();
						int nn = Integer.parseInt(breader.readLine().trim());

						// read matrix - a
						breader.readLine();
						for (int i = 0; i < a.getData().length; i++)
							a.set(i, Double.parseDouble(breader.readLine()
									.trim()));

						// read matrix - b
						breader.readLine();
						for (int i = 0; i < b.getData().length; i++)
							b.set(i, Double.parseDouble(breader.readLine()
									.trim()));

						// read eigenvalues
						breader.readLine();
						for (int i = 0; i < nEigval; i++)
							eigval.set(i, Double.parseDouble(breader.readLine()
									.trim()));

						// read eigenvectors
						for (int i = 0; i < nEigval; i++) {
							breader.readLine();
							for (int j = 0; j < nn; j++)
								eigvec.set(j, i, Double.parseDouble(breader
										.readLine().trim()));
						}
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
	 * Checks given parameters.
	 * 
	 * @param nRoot
	 *            Number of required eigenvalues and eigenvectors.
	 * @param rTol
	 *            Convergence tolerance on eigenvalues.
	 * @param nitem
	 *            Maximum number of subspace iterations permitted.
	 */
	private void checkParameters(int nRoot, double rTol, int nitem) {

		// initialize error message
		String err = "Invalid parameter for solver!";

		// check nRoot
		if (nRoot <= 0 || nRoot > 20)
			exceptionHandler(err);

		// check rTol
		if (rTol <= 0.0)
			exceptionHandler(err);

		// check nitem
		if (nitem <= 0)
			exceptionHandler(err);
	}
}
