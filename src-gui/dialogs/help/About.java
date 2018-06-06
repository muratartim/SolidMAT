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
package dialogs.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;

import main.Commons;
import main.ImageHandler;
import main.SolidMAT;

/**
 * Class for About menu.
 * 
 * @author Murat Artim
 * 
 */
public class About extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 */
	public About(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "About", true);

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// set color to panels
		panel1.setBackground(Color.WHITE);
		panel2.setBackground(Color.WHITE);

		// build labels
		JLabel label2 = new JLabel("SolidMAT, Version 2.0");
		JLabel label5 = new JLabel("Murat Artim");
		JLabel label7 = new JLabel("muratartim@airbus.com");
		JLabel label6 = new JLabel("SolidMAT is free to use and distribute.");
		JLabel label8 = new JLabel("NO MAGIC");
		JLabel label9 = new JLabel(ImageHandler.createImageIcon("J2EE.jpg"));

		// build buttons and give font
		JButton button1 = new JButton("  OK  ");

		// add components to sub-panels
		Commons.addComponent(panel1, label2, 0, 0, 1, 1);
		Commons.addComponent(panel1, label5, 1, 0, 1, 1);
		Commons.addComponent(panel1, label7, 2, 0, 1, 1);
		Commons.addComponent(panel1, label6, 3, 0, 1, 1);
		Commons.addComponent(panel1, label8, 4, 0, 1, 1);
		Commons.addComponent(panel1, label9, 0, 1, 1, 5);

		// add sub-panels to main panels
		panel2.add(button1);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets dialog unvisible if OK button is clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "  OK  ") {
			setVisible(false);
		}
	}
}
