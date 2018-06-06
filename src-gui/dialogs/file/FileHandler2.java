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
package dialogs.file;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

// import main.ImageHandler;
import main.SolidMAT;
import main.SwingWorker;

import data.InputData;
import data.OutputData;

import analysis.Structure;
import main.Commons;

/**
 * Class for handling file actions.
 * 
 * @author Murat Artim
 * 
 */
public class FileHandler2 extends JDialog {

	private static final long serialVersionUID = 1L;

	public static final int open_ = 0, save_ = 1, saveAs_ = 2;

	private static final String err1_ = "File not found!",
			err2_ = "Cannot read file!", err3_ = "Cannot process file!",
			err4_ = "Cannot write to file!";

	private JLabel label1_;

	private JProgressBar progressBar1_;

	private int type_;

	private String path_;

	private SolidMAT owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Window to be the owner of this dialog.
	 * @param type
	 *            The type of task to be performed.
	 * @param path
	 *            The path of file to be processed.
	 */
	public FileHandler2(SolidMAT owner, int type, String path) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Processing...", true);
		owner_ = owner;
		type_ = type;
		path_ = path;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);

		// build sub-panels
		JPanel panel2 = Commons.getPanel("", Commons.gridbag_);

		// build labels
		label1_ = new JLabel(" ");
		label1_.setPreferredSize(new Dimension(200, 14));

		// build progress bar
		progressBar1_ = new JProgressBar();
		progressBar1_.setIndeterminate(true);

		// add components to sub-panels
		Commons.addComponent(panel2, label1_, 0, 0, 1, 1);
		Commons.addComponent(panel2, progressBar1_, 1, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel2, 0, 0, 1, 1);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);

		// initialize thread for the task to be performed
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				if (type_ == FileHandler2.open_)
					openFile();
				else if (type_ == FileHandler2.save_)
					saveFile();
				else if (type_ == FileHandler2.saveAs_)
					saveFileAs();
				return null;
			}
		};

		// visualize
		visualize();

		// start task
		worker.start();
	}

	/**
	 * Opens existing file.
	 * 
	 */
	private void openFile() {

		// initialize input stream
		ObjectInputStream in = null;

		// read file
		try {

			// create input stream
			in = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(path_)));

			// read input data
			label1_.setText("Reading input data...");
			InputData inputData = (InputData) in.readObject();

			// read structure
			label1_.setText("Reading structure...");
			Structure structure = (Structure) in.readObject();

			// set input data to main frame
			owner_.setInputData(inputData);

			// set structure to main frame
			owner_.setStructure(structure);

			// set path
			owner_.path_ = path_;

			// set name to main frame
			owner_.setName();

			// draw
			label1_.setText("Drawing...");
			owner_.drawPre();
		}

		// file not found
		catch (FileNotFoundException e) {

			// display message
			JOptionPane
					.showMessageDialog(owner_.viewer_, err1_, "Exception", 2);
		}

		// cannot read file
		catch (IOException e) {

			// display message
			JOptionPane
					.showMessageDialog(owner_.viewer_, err2_, "Exception", 2);
		}

		// cannot process file
		catch (ClassNotFoundException e) {

			// display message
			JOptionPane
					.showMessageDialog(owner_.viewer_, err3_, "Exception", 2);
		}

		// cannot process file
		catch (ClassCastException e) {

			// display message
			JOptionPane
					.showMessageDialog(owner_.viewer_, err3_, "Exception", 2);
		}

		// close input stream
		finally {

			// check if the input stream is null
			if (in != null) {
				try {

					// close
					in.close();
				} catch (IOException io) {
				}
			}

			// close dialog
			setVisible(false);
		}
	}

	/**
	 * Saves existing file.
	 * 
	 */
	private void saveFile() {

		// initialize output stream
		ObjectOutputStream out = null;

		// write to file
		try {

			// create output stream for structure and input data
			out = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(path_)));

			// write input data
			label1_.setText("Writing input data...");
			out.writeObject(owner_.inputData_);

			// write structure
			label1_.setText("Writing structure...");
			out.writeObject(owner_.structure_);
		}

		// cannot write to file
		catch (IOException e) {

			// display message
			JOptionPane
					.showMessageDialog(owner_.viewer_, err4_, "Exception", 2);
		}

		// close output stream
		finally {

			// check if the output stream is null
			if (out != null) {
				try {

					// close
					out.close();
				} catch (IOException io) {
				}
			}

			// close dialog
			setVisible(false);
		}
	}

	/**
	 * Saves file to a new location.
	 * 
	 */
	private void saveFileAs() {

		// initialize output stream
		ObjectOutputStream out = null;

		// write to file
		try {

			// create output stream for structure and input data
			out = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(path_)));

			// write input data
			label1_.setText("Writing input data...");
			out.writeObject(owner_.inputData_);

			// write structure
			label1_.setText("Writing structure...");
			out.writeObject(owner_.structure_);

			// owner path doesn't exist
			if (owner_.path_ == null) {

				// write output data
				label1_.setText("Writing output data...");
				OutputDataHandler1.write(path_, null, null);

				// set path
				owner_.path_ = path_;

				// set name to main frame
				owner_.setName();
			}

			// owner path exists
			else {

				// read output data
				OutputData outputData = OutputDataHandler1.read(owner_.path_);

				// problem occured during reading
				if (outputData == null) {

					// display message
					JOptionPane.showMessageDialog(owner_.viewer_, err4_,
							"Exception", 2);
				}

				// no problem occured
				else {

					// write output data
					label1_.setText("Writing output data...");
					OutputDataHandler1.write(path_, outputData.getUnknowns(),
							outputData.getNumberOfSteps());

					// set path
					owner_.path_ = path_;

					// set name to main frame
					owner_.setName();
				}
			}
		}

		// cannot write to file
		catch (IOException e) {

			// display message
			JOptionPane
					.showMessageDialog(owner_.viewer_, err4_, "Exception", 2);
		}

		// close output stream
		finally {

			// check if the output stream is null
			if (out != null) {
				try {

					// close
					out.close();
				} catch (IOException io) {
				}
			}

			// close dialog
			setVisible(false);
		}
	}

	/**
	 * Sets visualization for frame.
	 */
	private void visualize() {

		// pack frame, do not allow resizing, set default close operation as
		// hide and set location
		pack();
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
	}
}
