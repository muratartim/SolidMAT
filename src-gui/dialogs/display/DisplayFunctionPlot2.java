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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.Commons;
// import main.ImageHandler;

/**
 * Class for Plot options menu.
 * 
 * @author Murat Artim
 * 
 */
public class DisplayFunctionPlot2 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JCheckBox checkbox1_, checkbox2_, checkbox3_, checkbox4_;

	private JButton button1_, button2_, button3_;

	private Color pointColor_, lineColor_, backgroundColor_;

	/** Mother dialog of this dialog. */
	private DisplayFunctionPlot1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 */
	public DisplayFunctionPlot2(DisplayFunctionPlot1 owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Plot Options", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Graph Options", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Color", Commons.gridbag_);

		// build checkboxes
		checkbox1_ = new JCheckBox("Data labels-x");
		checkbox2_ = new JCheckBox("Data labels-y");
		checkbox3_ = new JCheckBox("Guidelines");
		checkbox4_ = new JCheckBox("Points visible");

		// build buttons and set font
		button1_ = new JButton("Point color");
		button2_ = new JButton("Line color");
		button3_ = new JButton("Background color");
		JButton button4 = new JButton("  OK  ");
		JButton button5 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, checkbox1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, checkbox2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, checkbox3_, 2, 0, 1, 1);
		Commons.addComponent(panel3, checkbox4_, 3, 0, 1, 1);
		Commons.addComponent(panel4, button1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, button2_, 1, 0, 1, 1);
		Commons.addComponent(panel4, button3_, 2, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 0, 1, 1, 1);
		panel2.add(button4);
		panel2.add(button5);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);

		// initialize
		initialize();

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// set checkboxes
		checkbox1_.setSelected(owner_.graph_.areDataLabelsXSet());
		checkbox2_.setSelected(owner_.graph_.areDataLabelsYSet());
		checkbox3_.setSelected(owner_.graph_.areGuidanceLinesSet());
		checkbox4_.setSelected(owner_.graph_.arePointsVisible());

		// set colors
		pointColor_ = owner_.graph_.getPointColor();
		lineColor_ = owner_.graph_.getLineColor();
		backgroundColor_ = owner_.graph_.getBackgroundColor();
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getActionCommand() == "  OK  ") {

			// call actionOk
			actionOk();
		}

		// cancel button clicked
		else if (e.getActionCommand() == "Cancel") {

			// set dialog unvisible
			setVisible(false);
		}

		// point color button clicked
		else if (e.getSource().equals(button1_)) {

			// show color chooser and get selected color
			Color c = JColorChooser.showDialog(this, "Choose Color",
					pointColor_);

			// check if any selected
			if (c != null)
				pointColor_ = c;
		}

		// line color button clicked
		else if (e.getSource().equals(button2_)) {

			// show color chooser and get selected color
			Color c = JColorChooser
					.showDialog(this, "Choose Color", lineColor_);

			// check if any selected
			if (c != null)
				lineColor_ = c;
		}

		// background color button clicked
		else if (e.getSource().equals(button3_)) {

			// show color chooser and get selected color
			Color c = JColorChooser.showDialog(this, "Choose Color",
					backgroundColor_);

			// check if any selected
			if (c != null)
				backgroundColor_ = c;
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// set checkbox selections
		owner_.graph_.setDataLabelX(checkbox1_.isSelected());
		owner_.graph_.setDataLabelY(checkbox2_.isSelected());
		owner_.graph_.setGuidanceLines(checkbox3_.isSelected());
		owner_.graph_.setPointsVisible(checkbox4_.isSelected());

		// set colors
		owner_.graph_.setPointColor(pointColor_);
		owner_.graph_.setLineColor(lineColor_);
		owner_.graph_.setBackgroundColor(backgroundColor_);

		// set dialog unvisible
		setVisible(false);
	}
}
