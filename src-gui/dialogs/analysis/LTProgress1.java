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
package dialogs.analysis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import main.Commons; // import main.ImageHandler;
import main.SwingWorker;

import analysis.Analysis;
import analysis.LinearTransient;

/**
 * Class for Linear Transient Analysis Progress monitoring.
 * 
 * @author Murat
 * 
 */
public class LTProgress1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final static int ONE_SECOND = 1000;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_;

	private JLabel label7_;

	private JButton button1_, button2_, button3_;

	private JProgressBar progressBar1_;

	private Timer timer1_;

	/** The counter of timer. */
	private int counter_ = 0;

	/** Analysis object of this dialog. */
	private LinearTransient analysis_;

	protected RunAnalysis1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 * @param analysis
	 *            Analysis to be set to the dialog.
	 */
	public LTProgress1(RunAnalysis1 owner, Analysis analysis) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Linear Transient Analysis", true);
		analysis_ = (LinearTransient) analysis;
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Progress Monitor", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Analysis time :");
		JLabel label2 = new JLabel("Number of equations :");
		JLabel label3 = new JLabel("Effective bandwidth :");
		JLabel label4 = new JLabel("Time step :");
		JLabel label5 = new JLabel("Number of iterations :");
		JLabel label6 = new JLabel("Final residual :");
		label7_ = new JLabel(" ");
		label7_.setPreferredSize(new Dimension(300, 14));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield1_.setEditable(false);
		textfield2_.setEditable(false);
		textfield3_.setEditable(false);
		textfield4_.setEditable(false);
		textfield5_.setEditable(false);
		textfield6_.setEditable(false);

		// build progress bar
		progressBar1_ = new JProgressBar();
		progressBar1_.setValue(0);

		// build buttons
		button1_ = new JButton("Submit");
		button2_ = new JButton("Kill");
		button3_ = new JButton("Close");
		button2_.setEnabled(false);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, label3, 2, 0, 1, 1);
		Commons.addComponent(panel3, label4, 3, 0, 1, 1);
		Commons.addComponent(panel3, label5, 4, 0, 1, 1);
		Commons.addComponent(panel3, label6, 5, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, textfield2_, 1, 1, 1, 1);
		Commons.addComponent(panel3, textfield3_, 2, 1, 1, 1);
		Commons.addComponent(panel3, textfield4_, 3, 1, 1, 1);
		Commons.addComponent(panel3, textfield5_, 4, 1, 1, 1);
		Commons.addComponent(panel3, textfield6_, 5, 1, 1, 1);
		Commons.addComponent(panel3, label7_, 6, 0, 2, 1);
		Commons.addComponent(panel3, progressBar1_, 7, 0, 2, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);
		panel2.add(button3_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);

		// Create a timer.
		timer1_ = new Timer(ONE_SECOND, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				// set analysis time
				counter_++;
				setTime();

				// set message
				label7_.setText(analysis_.getStatus());

				// set # of equations
				if (analysis_.getNumberOfEquations() != 0)
					textfield2_.setText(Integer.toString(analysis_
							.getNumberOfEquations()));

				// set effective bandwidth
				if (analysis_.getEffectiveBandWidth() != 0)
					textfield3_.setText(Integer.toString(analysis_
							.getEffectiveBandWidth()));

				// set current time step
				if (analysis_.getCurrentTimeStep() >= 0)
					textfield4_.setText(Integer.toString(analysis_
							.getCurrentTimeStep()));

				// set # of iterations and residual
				if (analysis_.getNumberOfIterations() != null) {
					textfield5_.setText(analysis_.getNumberOfIterations()
							.toString());
					textfield6_.setText(owner_.owner_.formatter_
							.format(analysis_.getResidual()));
				}

				// check if analysis killed internally or completed
				if (analysis_.isKilled() || analysis_.isCompleted()) {

					// stop timer
					timer1_.stop();

					// enable buttons
					button1_.setEnabled(true);
					button2_.setEnabled(false);
					button3_.setEnabled(true);

					// set window state
					setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

					// set progress bar determinate
					progressBar1_.setIndeterminate(false);
				}
			}
		});

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Handles button action events.
	 */
	public void actionPerformed(ActionEvent e) {

		// initialize thread for the task to be performed
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				analysis_.analyze();
				return null;
			}
		};

		// submit button clicked
		if (e.getSource().equals(button1_)) {

			// initialize texts
			textfield1_.setText("");
			textfield2_.setText("");
			textfield3_.setText("");
			textfield4_.setText("");
			textfield5_.setText("");
			textfield6_.setText("");
			label7_.setText("");

			// set buttons disabled
			button1_.setEnabled(false);
			button2_.setEnabled(true);
			button3_.setEnabled(false);

			// set window state
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

			// set progress bar indeterminate
			progressBar1_.setIndeterminate(true);

			// initialize counter
			counter_ = 0;

			// start timer
			timer1_.start();

			// start task
			worker.start();
		}

		// kill button clicked
		else if (e.getSource().equals(button2_)) {

			// stop task
			worker.interrupt();

			// stop timer
			timer1_.stop();

			// set buttons enabled
			button1_.setEnabled(true);
			button2_.setEnabled(false);
			button3_.setEnabled(true);

			// set progress bar determinate
			progressBar1_.setIndeterminate(false);

			// set window state
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

			// set text to label
			label7_.setText("Analysis interrupted by user.");
		}

		// close button clicked
		else if (e.getSource().equals(button3_)) {

			// set dialog unvisible
			setVisible(false);
		}
	}

	/**
	 * Sets analysis time in hours/minutes/seconds to textfield.
	 */
	private void setTime() {

		// compute hour, minute, and sec.
		int sec = counter_;
		int min = sec / 60;
		int hour = min / 60;
		min = min - hour * 60;
		sec = sec - hour * 3600 - min * 60;

		// convert to string
		String seconds = Integer.toString(sec);
		if (seconds.length() == 1)
			seconds = "0" + seconds;
		String minutes = Integer.toString(min);
		if (minutes.length() == 1)
			minutes = "0" + minutes;
		String hours = Integer.toString(hour);
		if (hours.length() == 1)
			hours = "0" + hours;
		String time = hours + ":" + minutes + ":" + seconds;

		// set to textfield
		textfield1_.setText(time);
	}
}
