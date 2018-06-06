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

import element.Element;
import element.Element1D;
import element.Element2D;
import element.Element3D;
import element.ElementLibrary;


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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import node.Node;

/**
 * Class for Show Element Model menu.
 * 
 * @author Murat
 * 
 */
public class ShowElement1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield4_;

	private JTextArea textArea1_;

	private JLabel label4_;

	/** The owner frame of this dialog. */
	protected SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public ShowElement1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Show Element", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Element", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Properties", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Element ID :");
		JLabel label2 = new JLabel("Type :");
		JLabel label3 = new JLabel("Node IDs :");
		label4_ = new JLabel("Length :");
		label4_.setVisible(false);

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield4_ = new JTextField();
		textfield2_.setEditable(false);
		textfield4_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(80, 20));
		textfield2_.setPreferredSize(new Dimension(163, 20));

		// build text areas
		textArea1_ = new JTextArea(2, 10);
		textArea1_.setLineWrap(true);
		textArea1_.setWrapStyleWord(true);
		textArea1_.setEditable(false);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(textArea1_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// build buttons
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Show");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button2, 0, 2, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, label4_, 2, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, scrollpane1, 1, 1, 1, 1);
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

			// call showElement
			showElement();
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void showElement() {

		// get the entered text
		String text = textfield1_.getText();

		// check for non-numeric and negative values
		try {

			// convert text to integer value
			int value = Integer.parseInt(text);

			// check if given element exists
			Element e = owner_.structure_.getElement(value);

			// get dimension
			int dim = e.getDimension();

			// one dimensional
			if (dim == ElementLibrary.oneDimensional_) {

				// set label
				label4_.setVisible(true);
				label4_.setText("Length :");

				// get 1D element
				Element1D e1D = (Element1D) e;

				// set length
				textfield4_.setText(owner_.formatter_.format(e1D.getLength()));
			}

			// two dimensional
			else if (dim == ElementLibrary.twoDimensional_) {

				// set label
				label4_.setVisible(true);
				label4_.setText("Area :");

				// get 2D element
				Element2D e2D = (Element2D) e;

				// set area
				textfield4_.setText(owner_.formatter_.format(e2D.getArea()));
			}

			// three dimensional
			else if (dim == ElementLibrary.threeDimensional_) {

				// set label
				label4_.setVisible(true);
				label4_.setText("Volume :");

				// get 3D element
				Element3D e3D = (Element3D) e;

				// set volume
				textfield4_.setText(owner_.formatter_.format(e3D.getVolume()));
			}

			// get type of element
			int type = e.getType();

			// get nodes of element
			Node[] nodes = e.getNodes();

			// initialize node ids string
			String ids = "";

			// loop over nodes of element
			for (int i = 0; i < nodes.length; i++) {

				// get id of node from structure
				ids += "," + owner_.structure_.indexOfNode(nodes[i]);
			}

			// set textfields
			textfield2_.setText(Integer.toString(type));
			textArea1_.setText(ids.substring(1));
		} catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(ShowElement1.this,
					"Given element does not exist!", "False data entry", 2);

			// set textfields
			textfield2_.setText("");
			textArea1_.setText("");
			textfield4_.setText("");
			label4_.setVisible(false);
		}
	}
}
