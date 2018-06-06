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
package dialogs.display;


import java.awt.BorderLayout;
import java.awt.Dimension;

// import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.SwingWorker;

/**
 * Class for Display Undeformed shape Display menu.
 * 
 * @author Murat
 * 
 */
public class DisplayUndeformedShape1 extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel label1_;

	private JProgressBar progressBar1_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public DisplayUndeformedShape1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Processing...", true);
		owner_ = owner;

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
				draw();
				return null;
			}
		};

		// visualize
		visualize();

		// start task
		worker.start();
	}

	/**
	 * Performs drawing operation.
	 * 
	 */
	private void draw() {

		// set options
		boolean[] nodeOpt = owner_.preVis_.getNodeOptions();
		boolean[] elementOpt = owner_.preVis_.getElementOptions();
		for (int i = 2; i < nodeOpt.length; i++)
			nodeOpt[i] = false;
		for (int i = 3; i < elementOpt.length; i++)
			elementOpt[i] = false;

		// draw
		label1_.setText("Drawing...");
		owner_.drawPre();

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
