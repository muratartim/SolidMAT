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
package dialogs.model;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import node.Node;

/**
 * Class for Show Node Model menu.
 * 
 * @author Murat
 * 
 */
public class ShowNode1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_;

	/** The owner frame of this dialog. */
	protected SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public ShowNode1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Show Node", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Node", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Coordinates", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Node ID :");
		JLabel label2 = new JLabel("X coordinate :");
		JLabel label3 = new JLabel("Y coordinate :");
		JLabel label4 = new JLabel("Z coordinate :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield2_.setEditable(false);
		textfield3_.setEditable(false);
		textfield4_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(80, 20));
		textfield2_.setPreferredSize(new Dimension(134, 20));

		// build buttons
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Show");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button2, 0, 2, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, label4, 2, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel4, textfield4_, 2, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		panel2.add(button1);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getActionCommand() == "  OK  ") {

			// set dialog unvisible
			setVisible(false);
		}

		// show button clicked
		else if (e.getActionCommand() == "Show") {

			// call showNode
			showNode();
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void showNode() {

		// get the entered text
		String text = textfield1_.getText();

		// check for non-numeric and negative values
		try {

			// convert text to integer value
			int value = Integer.parseInt(text);

			// check if given node exists
			Node node = owner_.structure_.getNode(value);

			// get the coordinates of node
			double x = node.getPosition().get(0);
			double y = node.getPosition().get(1);
			double z = node.getPosition().get(2);

			// set textfields
			textfield2_.setText(owner_.formatter_.format(x));
			textfield3_.setText(owner_.formatter_.format(y));
			textfield4_.setText(owner_.formatter_.format(z));
		} catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(ShowNode1.this,
					"Given node does not exist!", "False data entry", 2);

			// set textfields
			textfield2_.setText("");
			textfield3_.setText("");
			textfield4_.setText("");
		}
	}
}
