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
package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 * Class for showing process information.
 * 
 * @author Murat
 * 
 */
public class Progressor extends JDialog {

	private static final long serialVersionUID = 1L;

	private final static int ONE_SECOND = 1000;

	private JLabel label1_;

	private JProgressBar progressBar1_;

	private Timer timer1_;

	private int counter_ = 0;

	/** Boolean variable indicating if the task has been completed or not. */
	private boolean finished_ = false;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Window to be the owner of this dialog.
	 */
	public Progressor(JDialog owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Processing...");

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

		// Create a timer.
		timer1_ = new Timer(ONE_SECOND, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				// increase counter
				counter_++;

				// check if task completed
				if (finished_ == false && counter_ == 1) {
					timer1_.stop();
					setVisible(true);
				}
			}
		});

		// visualize
		visualize();

		// start timer
		timer1_.start();
	}

	/**
	 * Sets status message for the progressor.
	 * 
	 * @param message
	 *            The message to be set.
	 */
	public void setStatusMessage(String message) {
		label1_.setText(message);
	}

	/**
	 * Closes progressor.
	 * 
	 */
	public void close() {

		// task completed
		finished_ = true;

		// stop timer
		timer1_.stop();

		// close dialog
		setVisible(false);
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
