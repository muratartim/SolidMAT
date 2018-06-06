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
package dialogs.library;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import main.Commons;
import main.ImageHandler;

import section.*;

/**
 * Class for Add/Modify sections library.
 * 
 * @author Murat
 * 
 */
public class Section2 extends JDialog implements ActionListener, ItemListener,
		FocusListener {

	private static final long serialVersionUID = 1L;

	private JLabel label5_, label6_, label7_, label8_, label9_, label10_,
			label34_, label35_, label36_, label37_, label38_;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_,
			textfield10_, textfield11_, textfield12_, textfield13_,
			textfield14_, textfield15_, textfield16_, textfield17_,
			textfield18_, textfield19_, textfield20_, textfield21_,
			textfield22_, textfield23_, textfield24_, textfield25_,
			textfield26_, textfield27_, textfield28_, textfield29_,
			textfield30_, textfield31_, textfield32_, textfield33_,
			textfield34_, textfield35_, textfield36_, textfield37_,
			textfield38_, textfield39_, textfield40_, textfield41_,
			textfield42_, textfield43_, textfield44_, textfield45_,
			textfield46_, textfield47_;

	private JComboBox combobox1_, combobox2_;

	private JTabbedPane tabbedpane1_, tabbedpane2_;

	private JCheckBox checkbox1_, checkbox2_;

	private JRadioButton radiobutton1_, radiobutton2_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected Section1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners, calls defaultValues.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 * @param add
	 *            Used for determining if add or modify button clicked from
	 *            mother dialog.
	 */
	public Section2(Section1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Library", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel3 = Commons.getPanel(null, Commons.flow_);
		JPanel panel4 = Commons.getPanel(null, Commons.flow_);
		JPanel panel5 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel14 = Commons.getPanel(null, Commons.gridbag_);

		// build sub-panels
		JPanel panel6 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel7 = Commons.getPanel("Properties", Commons.gridbag_);
		JPanel panel8 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel9 = Commons.getPanel("Properties", Commons.gridbag_);
		JPanel panel10 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel11 = Commons.getPanel("Properties", Commons.gridbag_);
		JPanel panel12 = new JPanel();
		JPanel panel13 = new JPanel();
		JPanel panel17 = new JPanel();
		JPanel panel15 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel16 = Commons.getPanel("Properties", Commons.gridbag_);
		panel12.setLayout(new BoxLayout(panel12, BoxLayout.Y_AXIS));
		panel13.setLayout(new BoxLayout(panel13, BoxLayout.Y_AXIS));
		panel17.setLayout(new BoxLayout(panel17, BoxLayout.Y_AXIS));

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Type :");
		JLabel label3 = new JLabel("Name :");
		label5_ = new JLabel("R :");
		label6_ = new JLabel("b1 :");
		label7_ = new JLabel("b2 :");
		label8_ = new JLabel("tw :");
		label9_ = new JLabel("t1 :");
		label10_ = new JLabel("t2 :");
		label6_.setEnabled(false);
		label7_.setEnabled(false);
		label8_.setEnabled(false);
		label9_.setEnabled(false);
		label10_.setEnabled(false);
		JLabel label12 = new JLabel("Name :");
		JLabel label13 = new JLabel("Cross section area :");
		JLabel label14 = new JLabel("Shear area in 2 direction :");
		JLabel label15 = new JLabel("Shear area in 3 direction :");
		JLabel label16 = new JLabel("Moment of inertia about 2 axis :");
		JLabel label17 = new JLabel("Moment of inertia about 3 axis :");
		JLabel label18 = new JLabel("Radius of gyration about 2 axis :");
		JLabel label19 = new JLabel("Radius of gyration about 3 axis :");
		JLabel label20 = new JLabel("Torsional constant :");
		JLabel label21 = new JLabel("Warping constant :");
		JLabel label22 = new JLabel("Name :");
		JLabel label23 = new JLabel("Copy from :");
		JLabel label24 = new JLabel("Copy to :");
		JLabel label25 = new JLabel("Cross section area :");
		JLabel label26 = new JLabel("Shear area in 2 direction :");
		JLabel label27 = new JLabel("Shear area in 3 direction :");
		JLabel label28 = new JLabel("Moment of inertia about 2 axis :");
		JLabel label29 = new JLabel("Moment of inertia about 3 axis :");
		JLabel label30 = new JLabel("Radius of gyration about 2 axis :");
		JLabel label31 = new JLabel("Radius of gyration about 3 axis :");
		JLabel label32 = new JLabel("Torsional constant :");
		JLabel label33 = new JLabel("Warping constant :");
		JLabel label4 = new JLabel("Thickness variation :");
		JLabel label38 = new JLabel("Polar moment of inertia :");
		JLabel label39 = new JLabel("Polar radius of gyration :");
		JLabel label40 = new JLabel("Polar moment of inertia :");
		JLabel label41 = new JLabel("Polar radius of gyration :");
		label34_ = new JLabel("Thickness :");
		label35_ = new JLabel("Thickness of corner J :");
		label36_ = new JLabel("Thickness of corner K :");
		label37_ = new JLabel("Thickness of corner L :");
		label38_ = new JLabel();
		label35_.setEnabled(false);
		label36_.setEnabled(false);
		label37_.setEnabled(false);
		label2.setPreferredSize(new Dimension(60, 23));
		label3.setPreferredSize(new Dimension(53, 23));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield8_ = new JTextField();
		textfield10_ = new JTextField();
		textfield11_ = new JTextField();
		textfield12_ = new JTextField();
		textfield13_ = new JTextField();
		textfield14_ = new JTextField();
		textfield15_ = new JTextField();
		textfield16_ = new JTextField();
		textfield17_ = new JTextField();
		textfield18_ = new JTextField();
		textfield19_ = new JTextField();
		textfield20_ = new JTextField();
		textfield21_ = new JTextField();
		textfield22_ = new JTextField();
		textfield23_ = new JTextField();
		textfield24_ = new JTextField();
		textfield25_ = new JTextField();
		textfield26_ = new JTextField();
		textfield27_ = new JTextField();
		textfield28_ = new JTextField();
		textfield29_ = new JTextField();
		textfield9_ = new JTextField();
		textfield30_ = new JTextField();
		textfield31_ = new JTextField();
		textfield32_ = new JTextField();
		textfield33_ = new JTextField();
		textfield34_ = new JTextField();
		textfield35_ = new JTextField();
		textfield36_ = new JTextField();
		textfield37_ = new JTextField();
		textfield38_ = new JTextField();
		textfield39_ = new JTextField();
		textfield40_ = new JTextField();
		textfield41_ = new JTextField();
		textfield42_ = new JTextField();
		textfield43_ = new JTextField();
		textfield44_ = new JTextField();
		textfield45_ = new JTextField();
		textfield46_ = new JTextField();
		textfield47_ = new JTextField();
		textfield3_.setEnabled(false);
		textfield4_.setEnabled(false);
		textfield5_.setEnabled(false);
		textfield6_.setEnabled(false);
		textfield7_.setEnabled(false);
		textfield16_.setEditable(false);
		textfield33_.setEditable(false);
		textfield30_.setEnabled(false);
		textfield31_.setEnabled(false);
		textfield32_.setEnabled(false);
		textfield42_.setEditable(false);
		textfield43_.setEditable(false);
		textfield44_.setEditable(false);
		textfield45_.setEditable(false);
		textfield46_.setEditable(false);
		textfield47_.setEditable(false);
		textfield23_.setEditable(false);
		textfield36_.setEditable(false);
		textfield29_.setEditable(false);
		textfield39_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(353, 20));
		textfield10_.setPreferredSize(new Dimension(353, 20));
		textfield11_.setPreferredSize(new Dimension(230, 20));
		textfield8_.setPreferredSize(new Dimension(340, 20));
		textfield9_.setPreferredSize(new Dimension(280, 20));
		textfield17_.setPreferredSize(new Dimension(353, 20));

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Constant", true);
		radiobutton2_ = new JRadioButton("Variable", false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);

		// build checkboxes and set font
		checkbox1_ = new JCheckBox("End I");
		checkbox2_ = new JCheckBox("End J");

		// build list for combo box, build combo box and set maximum visible
		// row number. Then set font for items
		String types[] = { "Circle", "Ellipse", "Square", "Rectangle",
				"Equilateral Triangle", "Isosceles Triangle",
				"Circular Segment", "Circular Sector", "Trapezoid",
				"Polygon with n sides", "Hollow Circle", "Hollow Ellipse", "T",
				"Channel", "I", "L", "Z", "Thin Walled Hollow Ellipse",
				"Thin Walled Hollow Circle", "Thin Walled Hollow Rectangle",
				"Thin Walled Channel", "Thin Walled C", "Thin Walled Hat",
				"Thin Walled Inward Twin Channel",
				"Thin Walled Outward Twin Channel",
				"Thin Walled Equal Flanged I", "Thin Walled Unequal Flanged I",
				"Thin Walled Z", "Thin Walled Hollow Circular Sector" };
		combobox1_ = new JComboBox(types);
		combobox1_.setMaximumRowCount(6);
		combobox2_ = new JComboBox();
		combobox2_.setMaximumRowCount(6);
		for (int i = 0; i < owner_.temporary_.size(); i++) {
			int type = owner_.temporary_.get(i).getType();
			if (type != Section.thickness_ && type != Section.varThickness_)
				combobox2_.addItem(owner_.temporary_.get(i).getName());
		}
		combobox2_.setPreferredSize(new Dimension(110, 20));

		// build buttons and give font
		JButton button1 = new JButton(" Section properties ");
		JButton button2 = new JButton("  OK  ");
		JButton button3 = new JButton("Cancel");
		JButton button4 = new JButton("Copy");
		button4.setPreferredSize(new Dimension(110, 20));

		// add components to sub-panels
		Commons.addComponent(panel6, label1, 0, 0, 1, 1);
		Commons.addComponent(panel6, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel7, label2, 0, 0, 1, 1);
		Commons.addComponent(panel7, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel7, button1, 0, 2, 1, 1);
		Commons.addComponent(panel7, label5_, 1, 0, 1, 1);
		Commons.addComponent(panel7, label6_, 2, 0, 1, 1);
		Commons.addComponent(panel7, label7_, 3, 0, 1, 1);
		Commons.addComponent(panel7, label8_, 4, 0, 1, 1);
		Commons.addComponent(panel7, label9_, 5, 0, 1, 1);
		Commons.addComponent(panel7, label10_, 6, 0, 1, 1);
		Commons.addComponent(panel7, textfield2_, 1, 1, 1, 1);
		Commons.addComponent(panel7, textfield3_, 2, 1, 1, 1);
		Commons.addComponent(panel7, textfield4_, 3, 1, 1, 1);
		Commons.addComponent(panel7, textfield5_, 4, 1, 1, 1);
		Commons.addComponent(panel7, textfield6_, 5, 1, 1, 1);
		Commons.addComponent(panel7, textfield7_, 6, 1, 1, 1);
		Commons.addComponent(panel7, panel4, 1, 2, 1, 6);
		Commons.addComponent(panel8, label3, 0, 0, 1, 1);
		Commons.addComponent(panel8, textfield8_, 0, 1, 1, 1);
		Commons.addComponent(panel10, label12, 0, 0, 1, 1);
		Commons.addComponent(panel10, textfield10_, 0, 1, 1, 1);
		Commons.addComponent(panel11, label13, 0, 0, 1, 1);
		Commons.addComponent(panel11, label14, 1, 0, 1, 1);
		Commons.addComponent(panel11, label15, 2, 0, 1, 1);
		Commons.addComponent(panel11, label38, 3, 0, 1, 1);
		Commons.addComponent(panel11, label16, 4, 0, 1, 1);
		Commons.addComponent(panel11, label17, 5, 0, 1, 1);
		Commons.addComponent(panel11, label39, 6, 0, 1, 1);
		Commons.addComponent(panel11, label18, 7, 0, 1, 1);
		Commons.addComponent(panel11, label19, 8, 0, 1, 1);
		Commons.addComponent(panel11, label20, 9, 0, 1, 1);
		Commons.addComponent(panel11, label21, 10, 0, 1, 1);
		Commons.addComponent(panel11, textfield11_, 0, 1, 1, 1);
		Commons.addComponent(panel11, textfield12_, 1, 1, 1, 1);
		Commons.addComponent(panel11, textfield13_, 2, 1, 1, 1);
		Commons.addComponent(panel11, textfield42_, 3, 1, 1, 1);
		Commons.addComponent(panel11, textfield14_, 4, 1, 1, 1);
		Commons.addComponent(panel11, textfield15_, 5, 1, 1, 1);
		Commons.addComponent(panel11, textfield43_, 6, 1, 1, 1);
		Commons.addComponent(panel11, textfield16_, 7, 1, 1, 1);
		Commons.addComponent(panel11, textfield33_, 8, 1, 1, 1);
		Commons.addComponent(panel11, textfield34_, 9, 1, 1, 1);
		Commons.addComponent(panel11, textfield35_, 10, 1, 1, 1);
		Commons.addComponent(panel15, label22, 0, 0, 1, 1);
		Commons.addComponent(panel15, textfield17_, 0, 1, 1, 1);
		Commons.addComponent(panel16, label23, 0, 0, 1, 1);
		Commons.addComponent(panel16, combobox2_, 0, 1, 1, 1);
		Commons.addComponent(panel16, button4, 0, 2, 1, 1);
		Commons.addComponent(panel16, label24, 1, 0, 1, 1);
		Commons.addComponent(panel16, checkbox1_, 1, 1, 1, 1);
		Commons.addComponent(panel16, checkbox2_, 1, 2, 1, 1);
		Commons.addComponent(panel16, label25, 2, 0, 1, 1);
		Commons.addComponent(panel16, label26, 3, 0, 1, 1);
		Commons.addComponent(panel16, label27, 4, 0, 1, 1);
		Commons.addComponent(panel16, label40, 5, 0, 1, 1);
		Commons.addComponent(panel16, label28, 6, 0, 1, 1);
		Commons.addComponent(panel16, label29, 7, 0, 1, 1);
		Commons.addComponent(panel16, label41, 8, 0, 1, 1);
		Commons.addComponent(panel16, label30, 9, 0, 1, 1);
		Commons.addComponent(panel16, label31, 10, 0, 1, 1);
		Commons.addComponent(panel16, label32, 11, 0, 1, 1);
		Commons.addComponent(panel16, label33, 12, 0, 1, 1);
		Commons.addComponent(panel16, textfield18_, 2, 1, 1, 1);
		Commons.addComponent(panel16, textfield19_, 3, 1, 1, 1);
		Commons.addComponent(panel16, textfield20_, 4, 1, 1, 1);
		Commons.addComponent(panel16, textfield44_, 5, 1, 1, 1);
		Commons.addComponent(panel16, textfield21_, 6, 1, 1, 1);
		Commons.addComponent(panel16, textfield22_, 7, 1, 1, 1);
		Commons.addComponent(panel16, textfield45_, 8, 1, 1, 1);
		Commons.addComponent(panel16, textfield23_, 9, 1, 1, 1);
		Commons.addComponent(panel16, textfield36_, 10, 1, 1, 1);
		Commons.addComponent(panel16, textfield37_, 11, 1, 1, 1);
		Commons.addComponent(panel16, textfield38_, 12, 1, 1, 1);
		Commons.addComponent(panel16, textfield24_, 2, 2, 1, 1);
		Commons.addComponent(panel16, textfield25_, 3, 2, 1, 1);
		Commons.addComponent(panel16, textfield26_, 4, 2, 1, 1);
		Commons.addComponent(panel16, textfield46_, 5, 2, 1, 1);
		Commons.addComponent(panel16, textfield27_, 6, 2, 1, 1);
		Commons.addComponent(panel16, textfield28_, 7, 2, 1, 1);
		Commons.addComponent(panel16, textfield47_, 8, 2, 1, 1);
		Commons.addComponent(panel16, textfield29_, 9, 2, 1, 1);
		Commons.addComponent(panel16, textfield39_, 10, 2, 1, 1);
		Commons.addComponent(panel16, textfield40_, 11, 2, 1, 1);
		Commons.addComponent(panel16, textfield41_, 12, 2, 1, 1);
		Commons.addComponent(panel9, label4, 0, 0, 1, 1);
		Commons.addComponent(panel9, radiobutton1_, 0, 1, 1, 1);
		Commons.addComponent(panel9, radiobutton2_, 0, 2, 1, 1);
		Commons.addComponent(panel9, label34_, 1, 0, 1, 1);
		Commons.addComponent(panel9, label35_, 2, 0, 1, 1);
		Commons.addComponent(panel9, label36_, 3, 0, 1, 1);
		Commons.addComponent(panel9, label37_, 4, 0, 1, 1);
		Commons.addComponent(panel9, textfield9_, 1, 1, 2, 1);
		Commons.addComponent(panel9, textfield30_, 2, 1, 2, 1);
		Commons.addComponent(panel9, textfield31_, 3, 1, 2, 1);
		Commons.addComponent(panel9, textfield32_, 4, 1, 2, 1);
		panel12.add(Box.createRigidArea(new Dimension(0, 53)));
		panel13.add(Box.createRigidArea(new Dimension(0, 260)));
		panel17.add(Box.createRigidArea(new Dimension(0, 170)));

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel6, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel7, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel17, 2, 0, 1, 1);
		Commons.addComponent(panel2, panel8, 0, 0, 1, 1);
		Commons.addComponent(panel2, panel9, 1, 0, 1, 1);
		Commons.addComponent(panel2, panel13, 2, 0, 1, 1);
		Commons.addComponent(panel5, panel10, 0, 0, 1, 1);
		Commons.addComponent(panel5, panel11, 1, 0, 1, 1);
		Commons.addComponent(panel5, panel12, 2, 0, 1, 1);
		Commons.addComponent(panel14, panel15, 0, 0, 1, 1);
		Commons.addComponent(panel14, panel16, 1, 0, 1, 1);
		panel3.add(button2);
		panel3.add(button3);
		panel4.add(label38_);

		// build tabbedpane and set font
		tabbedpane1_ = new JTabbedPane();
		tabbedpane2_ = new JTabbedPane();

		// add panels to tabbedpane
		tabbedpane1_.addTab("Lines", tabbedpane2_);
		tabbedpane1_.addTab("Areas", panel2);
		tabbedpane2_.addTab("Standard", panel1);
		tabbedpane2_.addTab("General", panel5);
		tabbedpane2_.addTab("Variable", panel14);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", tabbedpane1_);
		getContentPane().add("South", panel3);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		combobox1_.addItemListener(this);
		radiobutton1_.addActionListener(this);
		radiobutton2_.addActionListener(this);
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield4_.addFocusListener(this);
		textfield5_.addFocusListener(this);
		textfield6_.addFocusListener(this);
		textfield7_.addFocusListener(this);
		textfield8_.addFocusListener(this);
		textfield9_.addFocusListener(this);
		textfield10_.addFocusListener(this);
		textfield11_.addFocusListener(this);
		textfield12_.addFocusListener(this);
		textfield13_.addFocusListener(this);
		textfield14_.addFocusListener(this);
		textfield15_.addFocusListener(this);
		textfield17_.addFocusListener(this);
		textfield18_.addFocusListener(this);
		textfield19_.addFocusListener(this);
		textfield20_.addFocusListener(this);
		textfield21_.addFocusListener(this);
		textfield22_.addFocusListener(this);
		textfield24_.addFocusListener(this);
		textfield25_.addFocusListener(this);
		textfield26_.addFocusListener(this);
		textfield27_.addFocusListener(this);
		textfield28_.addFocusListener(this);
		textfield30_.addFocusListener(this);
		textfield31_.addFocusListener(this);
		textfield32_.addFocusListener(this);
		textfield34_.addFocusListener(this);
		textfield35_.addFocusListener(this);
		textfield37_.addFocusListener(this);
		textfield38_.addFocusListener(this);
		textfield40_.addFocusListener(this);
		textfield41_.addFocusListener(this);

		// If add is clicked set default, if not initialize
		setDefaultText(new JTextField());
		if (add_) {
			computeValues(0);
			computeValues(1);
		} else
			initialize();

		// draw
		draw();

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// get index of selected item in list of mother dialog
		int index = owner_.list1_.getSelectedIndex();

		// get the selected object
		Section selected = owner_.temporary_.get(index);

		// get name, type
		String name = selected.getName();
		int type = selected.getType();

		// set tab
		if (type == Section.thickness_ || type == Section.varThickness_)
			tabbedpane1_.setSelectedIndex(1);
		else
			tabbedpane1_.setSelectedIndex(0);

		// for lines
		if (type != Section.thickness_ && type != Section.varThickness_) {

			// for standard sections
			if (type != Section.userDefined_ && type != Section.variable_) {

				// set sub-tab
				tabbedpane2_.setSelectedIndex(0);

				// set name
				textfield1_.setText(name);

				// set combobox
				combobox1_.setSelectedIndex(type);

				// for circle section
				if (type == Section.circle_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
				}

				// for ellipse section
				else if (type == Section.ellipse_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
				}

				// for square section
				else if (type == Section.square_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
				}

				// for rectangle section
				else if (type == Section.rectangular_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
				}

				// for equilateral triangle section
				else if (type == Section.eTriangle_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
				}

				// for isosceles triangle section
				else if (type == Section.iTriangle_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
				}

				// for circular segment section
				else if (type == Section.cSegment_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
				}

				// for circular sector section
				else if (type == Section.cSector_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
				}

				// for trapezoid section
				else if (type == Section.trapezoid_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
				}

				// for polygon section
				else if (type == Section.polygon_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
				}

				// for hollow circle section
				else if (type == Section.hCircle_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
				}

				// for hollow ellipse section
				else if (type == Section.hEllipse_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for T section
				else if (type == Section.tee_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
					textfield6_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(4)));
				}

				// for Channel section
				else if (type == Section.channel_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
					textfield6_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(4)));
				}

				// for I section
				else if (type == Section.i_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
					textfield6_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(4)));
				}

				// for L section
				else if (type == Section.l_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for Z section
				else if (type == Section.z_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for thin walled hollow ellipse section
				else if (type == Section.tWHEllipse_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
				}

				// for thin walled hollow circle section
				else if (type == Section.tWHCircle_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
				}

				// for thin walled hollow rectangle section
				else if (type == Section.tWHRectangle_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for thin walled channel section
				else if (type == Section.tWChannel_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
				}

				// for thin walled c section
				else if (type == Section.c_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for thin walled hat section
				else if (type == Section.hat_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for thin walled inward twin channel section
				else if (type == Section.iTwinChannel_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for thin walled outward twin channel section
				else if (type == Section.oTwinChannel_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for thin walled equal flanged I section
				else if (type == Section.eFI_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
				}

				// for thin walled unequal flanged I section
				else if (type == Section.uFI_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
					textfield5_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(3)));
					textfield6_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(4)));
					textfield7_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(5)));
				}

				// for thin walled Z section
				else if (type == Section.tWZ_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
				}

				// for thin walled hollow circular sector section
				else if (type == Section.hCSector_) {
					textfield2_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(0)));
					textfield3_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(1)));
					textfield4_.setText(owner_.owner_.formatter_
							.format(selected.getDimension(2)));
				}

				// set default values for others
				setDefaultText(textfield8_);
				setDefaultText(textfield9_);
				setDefaultText(textfield10_);
				setDefaultText(textfield11_);
				setDefaultText(textfield12_);
				setDefaultText(textfield13_);
				setDefaultText(textfield14_);
				setDefaultText(textfield15_);
				setDefaultText(textfield17_);
				setDefaultText(textfield18_);
				setDefaultText(textfield19_);
				setDefaultText(textfield20_);
				setDefaultText(textfield21_);
				setDefaultText(textfield22_);
				setDefaultText(textfield24_);
				setDefaultText(textfield25_);
				setDefaultText(textfield26_);
				setDefaultText(textfield27_);
				setDefaultText(textfield28_);
				setDefaultText(textfield30_);
				setDefaultText(textfield31_);
				setDefaultText(textfield32_);
				setDefaultText(textfield34_);
				setDefaultText(textfield35_);
				setDefaultText(textfield37_);
				setDefaultText(textfield38_);
				setDefaultText(textfield40_);
				setDefaultText(textfield41_);

				// compute values
				computeValues(0);
				computeValues(1);
			}

			// for general section
			else if (type == Section.userDefined_) {

				// set sub-tab
				tabbedpane2_.setSelectedIndex(1);

				// set name
				textfield10_.setText(name);

				// set property text fields
				textfield11_.setText(owner_.owner_.formatter_.format(selected
						.getArea(0)));
				textfield12_.setText(owner_.owner_.formatter_.format(selected
						.getShearAreaX2(0)));
				textfield13_.setText(owner_.owner_.formatter_.format(selected
						.getShearAreaX3(0)));
				textfield42_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX1(0)));
				textfield14_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX2(0)));
				textfield15_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX3(0)));
				textfield43_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX1(0)));
				textfield16_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX2(0)));
				textfield33_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX3(0)));
				textfield34_.setText(owner_.owner_.formatter_.format(selected
						.getTorsionalConstant(0)));
				textfield35_.setText(owner_.owner_.formatter_.format(selected
						.getWarpingConstant(0)));

				// set default values for other textfields
				setDefaultText(textfield1_);
				setDefaultText(textfield2_);
				setDefaultText(textfield3_);
				setDefaultText(textfield4_);
				setDefaultText(textfield5_);
				setDefaultText(textfield6_);
				setDefaultText(textfield7_);
				setDefaultText(textfield8_);
				setDefaultText(textfield9_);
				setDefaultText(textfield17_);
				setDefaultText(textfield18_);
				setDefaultText(textfield19_);
				setDefaultText(textfield20_);
				setDefaultText(textfield21_);
				setDefaultText(textfield22_);
				setDefaultText(textfield24_);
				setDefaultText(textfield25_);
				setDefaultText(textfield26_);
				setDefaultText(textfield27_);
				setDefaultText(textfield28_);
				setDefaultText(textfield30_);
				setDefaultText(textfield31_);
				setDefaultText(textfield32_);
				setDefaultText(textfield37_);
				setDefaultText(textfield38_);
				setDefaultText(textfield40_);
				setDefaultText(textfield41_);

				// compute values
				computeValues(1);
			}

			// for variable section
			else if (type == Section.variable_) {

				// set sub-tab
				tabbedpane2_.setSelectedIndex(2);

				// set name
				textfield17_.setText(name);

				// set property text fields
				textfield18_.setText(owner_.owner_.formatter_.format(selected
						.getArea(0)));
				textfield19_.setText(owner_.owner_.formatter_.format(selected
						.getShearAreaX2(0)));
				textfield20_.setText(owner_.owner_.formatter_.format(selected
						.getShearAreaX3(0)));
				textfield44_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX1(0)));
				textfield21_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX2(0)));
				textfield22_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX3(0)));
				textfield45_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX1(0)));
				textfield23_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX2(0)));
				textfield36_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX3(0)));
				textfield37_.setText(owner_.owner_.formatter_.format(selected
						.getTorsionalConstant(0)));
				textfield38_.setText(owner_.owner_.formatter_.format(selected
						.getWarpingConstant(0)));
				textfield24_.setText(owner_.owner_.formatter_.format(selected
						.getArea(1)));
				textfield25_.setText(owner_.owner_.formatter_.format(selected
						.getShearAreaX2(1)));
				textfield26_.setText(owner_.owner_.formatter_.format(selected
						.getShearAreaX3(1)));
				textfield46_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX1(1)));
				textfield27_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX2(1)));
				textfield28_.setText(owner_.owner_.formatter_.format(selected
						.getInertiaX3(1)));
				textfield47_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX1(1)));
				textfield29_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX2(1)));
				textfield39_.setText(owner_.owner_.formatter_.format(selected
						.getGyrationX3(1)));
				textfield40_.setText(owner_.owner_.formatter_.format(selected
						.getTorsionalConstant(1)));
				textfield41_.setText(owner_.owner_.formatter_.format(selected
						.getWarpingConstant(1)));

				// set default values for other textfields
				setDefaultText(textfield1_);
				setDefaultText(textfield2_);
				setDefaultText(textfield3_);
				setDefaultText(textfield4_);
				setDefaultText(textfield5_);
				setDefaultText(textfield6_);
				setDefaultText(textfield7_);
				setDefaultText(textfield8_);
				setDefaultText(textfield9_);
				setDefaultText(textfield10_);
				setDefaultText(textfield11_);
				setDefaultText(textfield12_);
				setDefaultText(textfield13_);
				setDefaultText(textfield14_);
				setDefaultText(textfield15_);
				setDefaultText(textfield30_);
				setDefaultText(textfield31_);
				setDefaultText(textfield32_);
				setDefaultText(textfield34_);
				setDefaultText(textfield35_);

				// compute values
				computeValues(0);
			}
		}

		// for areas
		else if (type == Section.thickness_ || type == Section.varThickness_) {

			// set name
			textfield8_.setText(name);

			// set first property textfield
			textfield9_.setText(owner_.owner_.formatter_.format(selected
					.getDimension(0)));

			// for constant thickness
			if (type == Section.thickness_) {
				radiobutton1_.setSelected(true);

				// set default text values
				setDefaultText(textfield30_);
				setDefaultText(textfield31_);
				setDefaultText(textfield32_);
			}

			// for variable thickness
			else if (type == Section.varThickness_) {
				radiobutton2_.setSelected(true);
				textfield30_.setText(owner_.owner_.formatter_.format(selected
						.getDimension(1)));
				textfield31_.setText(owner_.owner_.formatter_.format(selected
						.getDimension(2)));
				textfield32_.setText(owner_.owner_.formatter_.format(selected
						.getDimension(3)));
				textfield30_.setEnabled(true);
				textfield31_.setEnabled(true);
				textfield32_.setEnabled(true);
				label34_.setText("Thickness of corner I :");
				label35_.setEnabled(true);
				label36_.setEnabled(true);
				label37_.setEnabled(true);
			}

			// set default values for other textfields
			setDefaultText(textfield1_);
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
			setDefaultText(textfield10_);
			setDefaultText(textfield11_);
			setDefaultText(textfield12_);
			setDefaultText(textfield13_);
			setDefaultText(textfield14_);
			setDefaultText(textfield15_);
			setDefaultText(textfield17_);
			setDefaultText(textfield18_);
			setDefaultText(textfield19_);
			setDefaultText(textfield20_);
			setDefaultText(textfield21_);
			setDefaultText(textfield22_);
			setDefaultText(textfield24_);
			setDefaultText(textfield25_);
			setDefaultText(textfield26_);
			setDefaultText(textfield27_);
			setDefaultText(textfield28_);
			setDefaultText(textfield34_);
			setDefaultText(textfield35_);
			setDefaultText(textfield37_);
			setDefaultText(textfield38_);
			setDefaultText(textfield40_);
			setDefaultText(textfield41_);

			// compute values
			computeValues(0);
			computeValues(1);
		}
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

		// properties button clicked
		else if (e.getActionCommand() == " Section properties ") {

			try {

				// create section object from current data
				Section sec = createSection();

				// build properties dialog
				Section3 dialog = new Section3(this, sec);
				dialog.setVisible(true);
			}

			// error occurred
			catch (Exception error) {

				// display message
				JOptionPane.showMessageDialog(Section2.this, error
						.getLocalizedMessage(), "False data entry", 2);
			}
		}

		// copy button clicked
		else if (e.getActionCommand() == "Copy") {

			// check any item exists in combobox
			if (combobox2_.getItemCount() != 0) {

				// get name of selected section
				String name = combobox2_.getSelectedItem().toString();
				Section s = null;

				// get selected section
				for (int i = 0; i < owner_.temporary_.size(); i++)
					if (name.equals(owner_.temporary_.get(i).getName()))
						s = owner_.temporary_.get(i);

				// fill values for end I
				if (checkbox1_.isSelected()) {
					textfield18_.setText(owner_.owner_.formatter_.format(s
							.getArea(0)));
					textfield19_.setText(owner_.owner_.formatter_.format(s
							.getShearAreaX2(0)));
					textfield20_.setText(owner_.owner_.formatter_.format(s
							.getShearAreaX3(0)));
					textfield44_.setText(owner_.owner_.formatter_.format(s
							.getInertiaX1(0)));
					textfield21_.setText(owner_.owner_.formatter_.format(s
							.getInertiaX2(0)));
					textfield22_.setText(owner_.owner_.formatter_.format(s
							.getInertiaX3(0)));
					textfield45_.setText(owner_.owner_.formatter_.format(s
							.getGyrationX1(0)));
					textfield23_.setText(owner_.owner_.formatter_.format(s
							.getGyrationX2(0)));
					textfield36_.setText(owner_.owner_.formatter_.format(s
							.getGyrationX3(0)));
					textfield37_.setText(owner_.owner_.formatter_.format(s
							.getTorsionalConstant(0)));
					textfield38_.setText(owner_.owner_.formatter_.format(s
							.getWarpingConstant(0)));
				}

				// fill values for end J
				if (checkbox2_.isSelected()) {
					textfield24_.setText(owner_.owner_.formatter_.format(s
							.getArea(1)));
					textfield25_.setText(owner_.owner_.formatter_.format(s
							.getShearAreaX2(1)));
					textfield26_.setText(owner_.owner_.formatter_.format(s
							.getShearAreaX3(1)));
					textfield46_.setText(owner_.owner_.formatter_.format(s
							.getInertiaX1(1)));
					textfield27_.setText(owner_.owner_.formatter_.format(s
							.getInertiaX2(1)));
					textfield28_.setText(owner_.owner_.formatter_.format(s
							.getInertiaX3(1)));
					textfield47_.setText(owner_.owner_.formatter_.format(s
							.getGyrationX1(1)));
					textfield29_.setText(owner_.owner_.formatter_.format(s
							.getGyrationX2(1)));
					textfield39_.setText(owner_.owner_.formatter_.format(s
							.getGyrationX3(1)));
					textfield40_.setText(owner_.owner_.formatter_.format(s
							.getTorsionalConstant(1)));
					textfield41_.setText(owner_.owner_.formatter_.format(s
							.getWarpingConstant(1)));
				}
			}
		}

		// constant thickness selected
		else if (e.getSource().equals(radiobutton1_)) {

			label34_.setText("Thickness :");
			label35_.setEnabled(false);
			label36_.setEnabled(false);
			label37_.setEnabled(false);
			textfield30_.setEnabled(false);
			textfield31_.setEnabled(false);
			textfield32_.setEnabled(false);
		}

		// variable thickness selected
		else if (e.getSource().equals(radiobutton2_)) {

			label34_.setText("Thickness of corner I :");
			label35_.setEnabled(true);
			label36_.setEnabled(true);
			label37_.setEnabled(true);
			textfield30_.setEnabled(true);
			textfield31_.setEnabled(true);
			textfield32_.setEnabled(true);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// get type
		int type = tabbedpane1_.getSelectedIndex();
		int type1 = tabbedpane2_.getSelectedIndex();

		// get name textfield to be checked
		JTextField textfield = new JTextField();
		if (type == 0) {
			if (type1 == 0)
				textfield = textfield1_;
			else if (type1 == 1)
				textfield = textfield10_;
			else if (type1 == 2)
				textfield = textfield17_;
		} else if (type == 1)
			textfield = textfield8_;

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield, 1)) {
				try {
					actionOkAddModify(type);
					setVisible(false);
				} catch (Exception e) {

					// display message
					JOptionPane.showMessageDialog(Section2.this, e
							.getLocalizedMessage(), "False data entry", 2);
				}
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield.getText().equals(selected)) {
				try {
					actionOkAddModify(type);
					setVisible(false);
				} catch (Exception e) {

					// display message
					JOptionPane.showMessageDialog(Section2.this, e
							.getLocalizedMessage(), "False data entry", 2);
				}
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield, 1)) {
					try {
						actionOkAddModify(type);
						setVisible(false);
					} catch (Exception e) {

						// display message
						JOptionPane.showMessageDialog(Section2.this, e
								.getLocalizedMessage(), "False data entry", 2);
					}
				}
			}
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 * 
	 * @param type
	 *            The type of object (line -> 0, area -> 1).
	 */
	private Section actionOkAddModify(int type) {

		// initialize input object
		Section object = null;
		String name = null;
		double[] values = null;

		// for lines
		if (type == 0) {

			// for standard sections
			if (tabbedpane2_.getSelectedIndex() == 0) {

				// get name
				name = textfield1_.getText();

				// create section
				object = createSection();
			}

			// for general sections
			else if (tabbedpane2_.getSelectedIndex() == 1) {

				// get name
				name = textfield10_.getText();

				// get values
				values = new double[7];
				values[0] = Double.parseDouble(textfield11_.getText());
				values[1] = Double.parseDouble(textfield12_.getText());
				values[2] = Double.parseDouble(textfield13_.getText());
				values[3] = Double.parseDouble(textfield14_.getText());
				values[4] = Double.parseDouble(textfield15_.getText());
				values[5] = Double.parseDouble(textfield34_.getText());
				values[6] = Double.parseDouble(textfield35_.getText());

				// set input object
				UserCS sec = new UserCS(name, values[0]);
				sec.setShearX2(values[1]);
				sec.setShearX3(values[2]);
				sec.setInertiaX2(values[3]);
				sec.setInertiaX3(values[4]);
				sec.setTorsionalConstant(values[5]);
				sec.setWarpingConstant(values[6]);
				object = sec;
			}

			// for variable sections
			else if (tabbedpane2_.getSelectedIndex() == 2) {

				// get name
				name = textfield17_.getText();

				// get values for end I
				values = new double[14];
				values[0] = Double.parseDouble(textfield18_.getText());
				values[1] = Double.parseDouble(textfield19_.getText());
				values[2] = Double.parseDouble(textfield20_.getText());
				values[3] = Double.parseDouble(textfield21_.getText());
				values[4] = Double.parseDouble(textfield22_.getText());
				values[5] = Double.parseDouble(textfield37_.getText());
				values[6] = Double.parseDouble(textfield38_.getText());

				// get values for end J
				values[7] = Double.parseDouble(textfield24_.getText());
				values[8] = Double.parseDouble(textfield25_.getText());
				values[9] = Double.parseDouble(textfield26_.getText());
				values[10] = Double.parseDouble(textfield27_.getText());
				values[11] = Double.parseDouble(textfield28_.getText());
				values[12] = Double.parseDouble(textfield40_.getText());
				values[13] = Double.parseDouble(textfield41_.getText());

				// set input object
				VariableCS sec = new VariableCS(name);
				sec.setArea(values[0], values[7]);
				sec.setShearX2(values[1], values[8]);
				sec.setShearX3(values[2], values[9]);
				sec.setInertiaX2(values[3], values[10]);
				sec.setInertiaX3(values[4], values[11]);
				sec.setTorsionalConstant(values[5], values[12]);
				sec.setWarpingConstant(values[6], values[13]);
				object = sec;
			}
		}

		// for areas
		else if (type == 1) {

			// get name
			name = textfield8_.getText();

			// for constant thickness
			if (radiobutton1_.isSelected()) {
				double value = Double.parseDouble(textfield9_.getText());
				object = new Thickness(name, value);
			}

			// for variable thickness
			else if (radiobutton2_.isSelected()) {
				values = new double[4];
				values[0] = Double.parseDouble(textfield9_.getText());
				values[1] = Double.parseDouble(textfield30_.getText());
				values[2] = Double.parseDouble(textfield31_.getText());
				values[3] = Double.parseDouble(textfield32_.getText());
				object = new VarThickness(name, values[0], values[1],
						values[2], values[3]);
			}
		}

		// add button clicked
		if (add_) {

			// add object to temporary vector and names to list
			owner_.temporary_.addElement(object);
			owner_.listModel1_.addElement(name);
		}

		// modify button clicked
		else if (add_ == false) {

			// set object to temporary vector and names to list
			int index = owner_.list1_.getSelectedIndex();
			owner_.temporary_.setElementAt(object, index);
			owner_.listModel1_.setElementAt(name, index);
		}

		// return created section
		return object;
	}

	private Section createSection() {

		// initialize object
		Section object = null;

		// get name
		String name = textfield1_.getText();

		// get section type
		int sectionType = combobox1_.getSelectedIndex();

		// get values
		double[] values = new double[6];
		values[0] = Double.parseDouble(textfield2_.getText());
		values[1] = Double.parseDouble(textfield3_.getText());
		values[2] = Double.parseDouble(textfield4_.getText());
		values[3] = Double.parseDouble(textfield5_.getText());
		values[4] = Double.parseDouble(textfield6_.getText());
		values[5] = Double.parseDouble(textfield7_.getText());

		// for circle section
		if (sectionType == Section.circle_)
			object = new CircleCS(name, values[0]);

		// for ellipse section
		else if (sectionType == Section.ellipse_)
			object = new EllipseCS(name, values[0], values[1]);

		// for square section
		else if (sectionType == Section.square_)
			object = new SquareCS(name, values[0]);

		// for rectangle section
		else if (sectionType == Section.rectangular_)
			object = new RectangleCS(name, values[0], values[1]);

		// for equilateral triangle section
		else if (sectionType == Section.eTriangle_)
			object = new ETriangleCS(name, values[0]);

		// for isosceles triangle section
		else if (sectionType == Section.iTriangle_)
			object = new ITriangleCS(name, values[0], values[1]);

		// for circular segment section
		else if (sectionType == Section.cSegment_)
			object = new CSegmentCS(name, values[0], values[1]);

		// for circular sector section
		else if (sectionType == Section.cSector_)
			object = new CSectorCS(name, values[0], values[1]);

		// for trapezoid section
		else if (sectionType == Section.trapezoid_)
			object = new TrapezoidCS(name, values[0], values[1], values[2]);

		// for polygon section
		else if (sectionType == Section.polygon_)
			object = new PolygonCS(name, values[0], values[1], values[2]);

		// for hollow circle section
		else if (sectionType == Section.hCircle_)
			object = new HCircleCS(name, values[0], values[1]);

		// for hollow ellipse section
		else if (sectionType == Section.hEllipse_)
			object = new HEllipseCS(name, values[0], values[1], values[2],
					values[3]);

		// for T section
		else if (sectionType == Section.tee_)
			object = new TeeCS(name, values[0], values[1], values[2],
					values[3], values[4]);

		// for channel section
		else if (sectionType == Section.channel_)
			object = new ChannelCS(name, values[0], values[1], values[2],
					values[3], values[4]);

		// for I section
		else if (sectionType == Section.i_)
			object = new ICS(name, values[0], values[1], values[2], values[3],
					values[4]);

		// for L section
		else if (sectionType == Section.l_)
			object = new LCS(name, values[0], values[1], values[2], values[3]);

		// for Z section
		else if (sectionType == Section.z_)
			object = new ZCS(name, values[0], values[1], values[2], values[3]);

		// for thin walled hollow ellipse section
		else if (sectionType == Section.tWHEllipse_)
			object = new TWHEllipseCS(name, values[0], values[1], values[2]);

		// for thin walled hollow circle section
		else if (sectionType == Section.tWHCircle_)
			object = new TWHCircleCS(name, values[0], values[1]);

		// for thin walled hollow rectangle section
		else if (sectionType == Section.tWHRectangle_)
			object = new TWHRectangleCS(name, values[0], values[1], values[2],
					values[3]);

		// for thin walled channel section
		else if (sectionType == Section.tWChannel_)
			object = new TWChannelCS(name, values[0], values[1], values[2]);

		// for thin walled C section
		else if (sectionType == Section.c_)
			object = new CCS(name, values[0], values[1], values[2], values[3]);

		// for thin walled hat section
		else if (sectionType == Section.hat_)
			object = new HatCS(name, values[0], values[1], values[2], values[3]);

		// for thin walled inward twin channel section
		else if (sectionType == Section.iTwinChannel_)
			object = new ITwinChannelCS(name, values[0], values[1], values[2],
					values[3]);

		// for thin walled outward twin channel section
		else if (sectionType == Section.oTwinChannel_)
			object = new OTwinChannelCS(name, values[0], values[1], values[2],
					values[3]);

		// for thin walled equal flanged I section
		else if (sectionType == Section.eFI_)
			object = new EFICS(name, values[0], values[1], values[2], values[3]);

		// for thin walled unequal flanged I section
		else if (sectionType == Section.uFI_)
			object = new UFICS(name, values[0], values[1], values[2],
					values[3], values[4], values[5]);

		// for thin walled Z section
		else if (sectionType == Section.tWZ_)
			object = new TWZCS(name, values[0], values[1], values[2]);

		// for thin walled hollow circular sector section
		else if (sectionType == Section.hCSector_)
			object = new TWHCSectorCS(name, values[0], values[1], values[2]);

		// return section
		return object;
	}

	/**
	 * Calls comboSelection.
	 */
	public void itemStateChanged(ItemEvent event) {

		// determine whether any item selected
		if (event.getStateChange() == ItemEvent.SELECTED) {

			// for combobox1
			if (event.getSource().equals(combobox1_)) {
				comboSelection();
				draw();
			}
		}
	}

	/**
	 * Sets texts for labels, sets labels' visibility, sets default values for
	 * textfields, sets textfields' visibility and calls draw depending on item
	 * selected.
	 */
	private void comboSelection() {

		// highlight components
		highlightComponent();

		// get selected item
		String s = combobox1_.getSelectedItem().toString();

		// geometry type: Circle
		if (s.equalsIgnoreCase("Circle")) {

			// set texts
			label5_.setText("R :");
			setDefaultText(textfield2_);

			// disable others
			label6_.setEnabled(false);
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield3_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Ellipse
		else if (s.equalsIgnoreCase("Ellipse")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("b :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);

			// disable others
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Square
		else if (s.equalsIgnoreCase("Square")) {

			// set texts
			label5_.setText("a :");
			setDefaultText(textfield2_);

			// disable others
			label6_.setEnabled(false);
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield3_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Rectangle
		else if (s.equalsIgnoreCase("Rectangle")) {

			// set texts
			label5_.setText("d :");
			label6_.setText("b :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);

			// disable others
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Equilateral Triangle
		else if (s.equalsIgnoreCase("Equilateral Triangle")) {

			// set texts
			label5_.setText("a :");
			setDefaultText(textfield2_);

			// disable others
			label6_.setEnabled(false);
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield3_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Isosceles Triangle
		else if (s.equalsIgnoreCase("Isosceles Triangle")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("b :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);

			// disable others
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Circular Segment
		else if (s.equalsIgnoreCase("Circular Segment")) {

			// set texts
			label5_.setText("R :");
			label6_.setText("alpha :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);

			// disable others
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Circular Sector
		else if (s.equalsIgnoreCase("Circular Sector")) {

			// set texts
			label5_.setText("R :");
			label6_.setText("alpha :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);

			// disable others
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Trapezoid
		else if (s.equalsIgnoreCase("Trapezoid")) {

			// set texts
			label5_.setText("b :");
			label6_.setText("m :");
			label7_.setText("n :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);

			// disable others
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Polygon
		else if (s.equalsIgnoreCase("Polygon with n sides")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("alpha :");
			label7_.setText("n :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);

			// disable others
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Hollow Circle
		else if (s.equalsIgnoreCase("Hollow Circle")) {

			// set texts
			label5_.setText("ro :");
			label6_.setText("ri :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);

			// disable others
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Hollow Ellipse
		else if (s.equalsIgnoreCase("Hollow Ellipse")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("ao :");
			label7_.setText("b :");
			label8_.setText("bo :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: T
		else if (s.equalsIgnoreCase("T")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("b :");
			label7_.setText("c :");
			label8_.setText("d :");
			label9_.setText("r :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);

			// disable others
			label10_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Channel
		else if (s.equalsIgnoreCase("Channel")) {

			// set texts
			label5_.setText("b :");
			label6_.setText("d :");
			label7_.setText("tw :");
			label8_.setText("t :");
			label9_.setText("r :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);

			// disable others
			label10_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: I
		else if (s.equalsIgnoreCase("I")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("b :");
			label7_.setText("c :");
			label8_.setText("d :");
			label9_.setText("r :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);

			// disable others
			label10_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: L
		else if (s.equalsIgnoreCase("L")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("b :");
			label7_.setText("c :");
			label8_.setText("r :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Z
		else if (s.equalsIgnoreCase("Z")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("b :");
			label7_.setText("t :");
			label8_.setText("r :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Hollow Ellipse
		else if (s.equalsIgnoreCase("Thin Walled Hollow Ellipse")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("b :");
			label7_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);

			// disable others
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Hollow Circle
		else if (s.equalsIgnoreCase("Thin Walled Hollow Circle")) {

			// set texts
			label5_.setText("R :");
			label6_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);

			// disable others
			label7_.setEnabled(false);
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Hollow Rectangle
		else if (s.equalsIgnoreCase("Thin Walled Hollow Rectangle")) {

			// set texts
			label5_.setText("a :");
			label6_.setText("b :");
			label7_.setText("t :");
			label8_.setText("t1 :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Channel
		else if (s.equalsIgnoreCase("Thin Walled Channel")) {

			// set texts
			label5_.setText("h :");
			label6_.setText("b :");
			label7_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);

			// disable others
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled C
		else if (s.equalsIgnoreCase("Thin Walled C")) {

			// set texts
			label5_.setText("h :");
			label6_.setText("b :");
			label7_.setText("b1 :");
			label8_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Hat
		else if (s.equalsIgnoreCase("Thin Walled Hat")) {

			// set texts
			label5_.setText("h :");
			label6_.setText("b :");
			label7_.setText("b1 :");
			label8_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Inward Twin Channel
		else if (s.equalsIgnoreCase("Thin Walled Inward Twin Channel")) {

			// set texts
			label5_.setText("h :");
			label6_.setText("b :");
			label7_.setText("b1 :");
			label8_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Outward Twin Channel
		else if (s.equalsIgnoreCase("Thin Walled Outward Twin Channel")) {

			// set texts
			label5_.setText("h :");
			label6_.setText("b :");
			label7_.setText("b1 :");
			label8_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Equal Flanged I
		else if (s.equalsIgnoreCase("Thin Walled Equal Flanged I")) {

			// set texts
			label5_.setText("h :");
			label6_.setText("b :");
			label7_.setText("tw :");
			label8_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);

			// disable others
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled Unequal Flanged I
		else if (s.equalsIgnoreCase("Thin Walled Unequal Flanged I")) {

			// set texts
			label5_.setText("h :");
			label6_.setText("b1 :");
			label7_.setText("b2 :");
			label8_.setText("tw :");
			label9_.setText("t1 :");
			label10_.setText("t2 :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
		}

		// geometry type: Thin Walled Z
		else if (s.equalsIgnoreCase("Thin Walled Z")) {

			// set texts
			label5_.setText("h :");
			label6_.setText("b :");
			label7_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);

			// disable others
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}

		// geometry type: Thin Walled hollow circular sector
		else if (s.equalsIgnoreCase("Thin Walled Hollow Circular Sector")) {

			// set texts
			label5_.setText("R :");
			label6_.setText("alpha :");
			label7_.setText("t :");
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);

			// disable others
			label8_.setEnabled(false);
			label9_.setEnabled(false);
			label10_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
			textfield7_.setEnabled(false);
		}
	}

	/**
	 * Sets components to be editable and seen.
	 */
	private void highlightComponent() {

		// set textfields edittable
		textfield2_.setEnabled(true);
		textfield3_.setEnabled(true);
		textfield4_.setEnabled(true);
		textfield5_.setEnabled(true);
		textfield6_.setEnabled(true);
		textfield7_.setEnabled(true);

		// set labels' foreground color to black
		label5_.setEnabled(true);
		label6_.setEnabled(true);
		label7_.setEnabled(true);
		label8_.setEnabled(true);
		label9_.setEnabled(true);
		label10_.setEnabled(true);
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// get source and dependently set message type
				JTextField textfield = (JTextField) e.getSource();
				if (textfield.isEditable()) {
					int messageType = 2;
					if (textfield.equals(textfield1_))
						messageType = 0;
					else if (textfield.equals(textfield8_))
						messageType = 0;
					else if (textfield.equals(textfield10_))
						messageType = 0;
					else if (textfield.equals(textfield17_))
						messageType = 0;

					// check textfield
					checkText(textfield, messageType);

					// compute values for general section
					if (textfield.equals(textfield11_)
							|| textfield.equals(textfield14_)
							|| textfield.equals(textfield15_))
						computeValues(0);

					// compute values for variable section
					if (textfield.equals(textfield18_)
							|| textfield.equals(textfield21_)
							|| textfield.equals(textfield22_)
							|| textfield.equals(textfield24_)
							|| textfield.equals(textfield27_)
							|| textfield.equals(textfield28_))
						computeValues(1);
				}
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * If false data has been entered displays message on screen.
	 * 
	 * @param textfield
	 *            The textfield that the false data has been entered.
	 * @param messageType
	 *            The type of message to be displayed. No name given -> 0, Name
	 *            exists -> 1, Illegal value (non-numeric, negative) -> 2.
	 * @return True if the data entered is correct, False if not.
	 */
	private boolean checkText(JTextField textfield, int messageType) {

		// boolean value for checking if data entered is correct or not
		boolean isCorrect = true;

		// get the entered text
		String text = textfield.getText();

		// No name given
		if (messageType == 0) {

			// check if no name given
			if (text.equals("")) {

				// display message
				JOptionPane.showMessageDialog(Section2.this, "No name given!",
						"False data entry", 2);

				// set default text
				setDefaultText(textfield);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(Section2.this,
						"Name already exists!", "False data entry", 2);

				// set default text
				setDefaultText(textfield);
				isCorrect = false;
			}
		}

		// Illegal value (non-numeric,zero or negative)
		else if (messageType == 2) {

			// check for non-numeric values
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check if its negative or zero
				if (value <= 0) {

					// display message
					JOptionPane.showMessageDialog(Section2.this,
							"Illegal value!", "False data entry", 2);

					// set default text
					setDefaultText(textfield);
					isCorrect = false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(Section2.this, "Illegal value!",
						"False data entry", 2);

				// set default text
				setDefaultText(textfield);
				isCorrect = false;
			}
		}

		// return check result
		return isCorrect;
	}

	/**
	 * Sets default values for textfields.
	 * 
	 * @param textfield
	 *            The textfield to be set. If null is given, sets all
	 *            textfields.
	 */
	private void setDefaultText(JTextField textfield) {

		// The default values for textfields
		String defaultName = "Section1";
		String defaultValue = owner_.owner_.formatter_.format(1.0);

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue);

		// set to textfield3
		else if (textfield.equals(textfield3_))
			textfield3_.setText(defaultValue);

		// set to textfield4
		else if (textfield.equals(textfield4_))
			textfield4_.setText(defaultValue);

		// set to textfield5
		else if (textfield.equals(textfield5_))
			textfield5_.setText(defaultValue);

		// set to textfield6
		else if (textfield.equals(textfield6_))
			textfield6_.setText(defaultValue);

		// set to textfield7
		else if (textfield.equals(textfield7_))
			textfield7_.setText(defaultValue);

		// set to textfield8
		else if (textfield.equals(textfield8_))
			textfield8_.setText(defaultName);

		// set to textfield9
		else if (textfield.equals(textfield9_))
			textfield9_.setText(defaultValue);

		// set to textfield10
		else if (textfield.equals(textfield10_))
			textfield10_.setText(defaultName);

		// set to textfield11
		else if (textfield.equals(textfield11_))
			textfield11_.setText(defaultValue);

		// set to textfield12
		else if (textfield.equals(textfield12_))
			textfield12_.setText(defaultValue);

		// set to textfield13
		else if (textfield.equals(textfield13_))
			textfield13_.setText(defaultValue);

		// set to textfield14
		else if (textfield.equals(textfield14_))
			textfield14_.setText(defaultValue);

		// set to textfield15
		else if (textfield.equals(textfield15_))
			textfield15_.setText(defaultValue);

		// set to textfield17
		else if (textfield.equals(textfield17_))
			textfield17_.setText(defaultName);

		// set to textfield18
		else if (textfield.equals(textfield18_))
			textfield18_.setText(defaultValue);

		// set to textfield19
		else if (textfield.equals(textfield19_))
			textfield19_.setText(defaultValue);

		// set to textfield20
		else if (textfield.equals(textfield20_))
			textfield20_.setText(defaultValue);

		// set to textfield21
		else if (textfield.equals(textfield21_))
			textfield21_.setText(defaultValue);

		// set to textfield22
		else if (textfield.equals(textfield22_))
			textfield22_.setText(defaultValue);

		// set to textfield24
		else if (textfield.equals(textfield24_))
			textfield24_.setText(defaultValue);

		// set to textfield25
		else if (textfield.equals(textfield25_))
			textfield25_.setText(defaultValue);

		// set to textfield26
		else if (textfield.equals(textfield26_))
			textfield26_.setText(defaultValue);

		// set to textfield27
		else if (textfield.equals(textfield27_))
			textfield27_.setText(defaultValue);

		// set to textfield28
		else if (textfield.equals(textfield28_))
			textfield28_.setText(defaultValue);

		// set to textfield30
		else if (textfield.equals(textfield30_))
			textfield30_.setText(defaultValue);

		// set to textfield31
		else if (textfield.equals(textfield31_))
			textfield31_.setText(defaultValue);

		// set to textfield32
		else if (textfield.equals(textfield32_))
			textfield32_.setText(defaultValue);

		// set to textfield34
		else if (textfield.equals(textfield34_))
			textfield34_.setText(defaultValue);

		// set to textfield35
		else if (textfield.equals(textfield35_))
			textfield35_.setText(defaultValue);

		// set to textfield37
		else if (textfield.equals(textfield37_))
			textfield37_.setText(defaultValue);

		// set to textfield38
		else if (textfield.equals(textfield38_))
			textfield38_.setText(defaultValue);

		// set to textfield40
		else if (textfield.equals(textfield40_))
			textfield40_.setText(defaultValue);

		// set to textfield41
		else if (textfield.equals(textfield41_))
			textfield41_.setText(defaultValue);

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(defaultValue);
			textfield3_.setText(defaultValue);
			textfield4_.setText(defaultValue);
			textfield5_.setText(defaultValue);
			textfield6_.setText(defaultValue);
			textfield7_.setText(defaultValue);
			textfield8_.setText(defaultName);
			textfield9_.setText(defaultValue);
			textfield10_.setText(defaultName);
			textfield11_.setText(defaultValue);
			textfield12_.setText(defaultValue);
			textfield13_.setText(defaultValue);
			textfield14_.setText(defaultValue);
			textfield15_.setText(defaultValue);
			textfield17_.setText(defaultName);
			textfield18_.setText(defaultValue);
			textfield19_.setText(defaultValue);
			textfield20_.setText(defaultValue);
			textfield21_.setText(defaultValue);
			textfield22_.setText(defaultValue);
			textfield24_.setText(defaultValue);
			textfield25_.setText(defaultValue);
			textfield26_.setText(defaultValue);
			textfield27_.setText(defaultValue);
			textfield28_.setText(defaultValue);
			textfield30_.setText(defaultValue);
			textfield31_.setText(defaultValue);
			textfield32_.setText(defaultValue);
			textfield34_.setText(defaultValue);
			textfield35_.setText(defaultValue);
			textfield37_.setText(defaultValue);
			textfield38_.setText(defaultValue);
			textfield40_.setText(defaultValue);
			textfield41_.setText(defaultValue);
		}
	}

	/**
	 * Draws the section picture related with the combobox selection.
	 */
	private void draw() {

		// get selected index of combobox
		String s = combobox1_.getSelectedItem().toString();

		// create image
		ImageIcon image = ImageHandler.createImageIcon(s + ".JPG");

		// set image to label
		label38_.setIcon(image);
	}

	/**
	 * Computes inertiaX1, gyrationX1, gyrationX2 and gyrationX3 for either
	 * general or variable section.
	 * 
	 * @param type
	 *            Section type to be computed (general -> 0, variable -> 1).
	 */
	private void computeValues(int type) {

		// general section
		if (type == 0) {

			// get values
			double a = Double.parseDouble(textfield11_.getText());
			double i2 = Double.parseDouble(textfield14_.getText());
			double i3 = Double.parseDouble(textfield15_.getText());

			// compute polar moment of inertia
			textfield42_.setText(owner_.owner_.formatter_.format(i2 + i3));

			// compute radii of gyration
			double r2 = Math.sqrt(i2 / a);
			double r3 = Math.sqrt(i3 / a);
			double r1 = Math.sqrt(r2 * r2 + r3 * r3);
			textfield16_.setText(owner_.owner_.formatter_.format(r2));
			textfield33_.setText(owner_.owner_.formatter_.format(r3));
			textfield43_.setText(owner_.owner_.formatter_.format(r1));
		}

		// variable section
		else if (type == 1) {

			// get values for end I
			double a = Double.parseDouble(textfield18_.getText());
			double i2 = Double.parseDouble(textfield21_.getText());
			double i3 = Double.parseDouble(textfield22_.getText());

			// set values for end I
			textfield44_.setText(owner_.owner_.formatter_.format(i2 + i3));
			double r2 = Math.sqrt(i2 / a);
			double r3 = Math.sqrt(i3 / a);
			double r1 = Math.sqrt(r2 * r2 + r3 * r3);
			textfield23_.setText(owner_.owner_.formatter_.format(r2));
			textfield36_.setText(owner_.owner_.formatter_.format(r3));
			textfield45_.setText(owner_.owner_.formatter_.format(r1));

			// get values for end J
			a = Double.parseDouble(textfield24_.getText());
			i2 = Double.parseDouble(textfield27_.getText());
			i3 = Double.parseDouble(textfield28_.getText());

			// set values for end J
			textfield46_.setText(owner_.owner_.formatter_.format(i2 + i3));
			r2 = Math.sqrt(i2 / a);
			r3 = Math.sqrt(i3 / a);
			r1 = Math.sqrt(r2 * r2 + r3 * r3);
			textfield29_.setText(owner_.owner_.formatter_.format(r2));
			textfield39_.setText(owner_.owner_.formatter_.format(r3));
			textfield47_.setText(owner_.owner_.formatter_.format(r1));
		}
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
