package dialogs.model;

import data.Group;
import element.*;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
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
import javax.swing.WindowConstants;

import node.Node;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DMat;
import matrix.DVec;

/**
 * Class for Replicate Elements Model menu.
 * 
 * @author Murat
 * 
 */
public class ReplicateElement1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_,
			textfield10_, textfield11_, textfield12_, textfield13_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_, radiobutton5_, radiobutton6_;

	private JComboBox combobox1_, combobox2_;

	private JCheckBox checkbox1_, checkbox2_;

	private JButton button1_, button2_, button3_, button4_;

	private JTabbedPane tabbedpane1_;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** Vector for storing element ids to be moved. */
	private Vector<Integer> elementIds_ = new Vector<Integer>();

	/** Vector for storing elements to be moved. */
	private Vector<Element> elements1_ = new Vector<Element>();

	/**
	 * Array storing the replicate options. The sequence is; material, section,
	 * mechanial loads, temperature loads, springs, masses, local axes.
	 */
	protected boolean[] options_ = { true, true, true, true, true, true, true };

	/** The progress monitor of processses that take place. */
	private Progressor progressor_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public ReplicateElement1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Replicate Elements", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);
		JPanel panel3 = Commons.getPanel(null, Commons.gridbag_);

		// build sub-panels
		JPanel panel4 = Commons.getPanel("Elements", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Replicate Elements by",
				Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Elements", Commons.gridbag_);
		JPanel panel7 = Commons.getPanel("Replicate Elements by",
				Commons.gridbag_);
		JPanel panel8 = Commons.getPanel("Replicate Options", Commons.gridbag_);
		JPanel panel9 = Commons.getPanel("Replicate Options", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Delta X :");
		JLabel label2 = new JLabel("Delta Y :");
		JLabel label3 = new JLabel("Delta Z :");
		JLabel label4 = new JLabel("Centroid :");
		JLabel label5 = new JLabel("Rotations :");
		JLabel label6 = new JLabel("     X");
		JLabel label7 = new JLabel("     Y");
		JLabel label8 = new JLabel("     Z");
		JLabel label9 = new JLabel("Repetitions :");
		JLabel label10 = new JLabel("Repetitions :");
		label6.setPreferredSize(new Dimension(40, 19));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield8_ = new JTextField();
		textfield9_ = new JTextField();
		textfield10_ = new JTextField();
		textfield11_ = new JTextField();
		textfield12_ = new JTextField();
		textfield13_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(118, 20));
		textfield5_.setPreferredSize(new Dimension(118, 20));
		textfield6_.setPreferredSize(new Dimension(40, 20));
		textfield7_.setPreferredSize(new Dimension(40, 20));
		textfield8_.setPreferredSize(new Dimension(40, 20));
		textfield12_.setPreferredSize(new Dimension(140, 20));
		textfield13_.setPreferredSize(new Dimension(140, 20));

		// get the groups
		String[] groups = getGroups();

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Element IDs :", true);
		radiobutton2_ = new JRadioButton("Groups :", false);
		radiobutton5_ = new JRadioButton("All existing elements", false);
		radiobutton3_ = new JRadioButton("Element IDs :", true);
		radiobutton4_ = new JRadioButton("Groups :", false);
		radiobutton6_ = new JRadioButton("All existing elements", false);

		// check if there is any group
		if (groups.length == 0) {
			radiobutton2_.setEnabled(false);
			radiobutton4_.setEnabled(false);
		}

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		ButtonGroup buttongroup2 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton5_);
		buttongroup2.add(radiobutton3_);
		buttongroup2.add(radiobutton4_);
		buttongroup2.add(radiobutton6_);

		// build comboboxes
		combobox1_ = new JComboBox(groups);
		combobox2_ = new JComboBox(groups);
		combobox1_.setMaximumRowCount(3);
		combobox2_.setMaximumRowCount(3);
		combobox1_.setEnabled(false);
		combobox2_.setEnabled(false);

		// build checkboxes
		checkbox1_ = new JCheckBox("Pass assignments");
		checkbox2_ = new JCheckBox("Pass assignments");
		checkbox1_.setSelected(true);
		checkbox2_.setSelected(true);

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");
		button3_ = new JButton("Options");
		button4_ = new JButton("Options");

		// add components to sub-panels
		Commons.addComponent(panel4, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel4, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton5_, 2, 0, 2, 1);
		Commons.addComponent(panel5, label1, 0, 0, 1, 1);
		Commons.addComponent(panel5, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel5, label2, 1, 0, 1, 1);
		Commons.addComponent(panel5, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel5, label3, 2, 0, 1, 1);
		Commons.addComponent(panel5, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel5, label9, 3, 0, 1, 1);
		Commons.addComponent(panel5, textfield12_, 3, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton3_, 0, 0, 1, 1);
		Commons.addComponent(panel6, textfield5_, 0, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton4_, 1, 0, 1, 1);
		Commons.addComponent(panel6, combobox2_, 1, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton6_, 2, 0, 2, 1);
		Commons.addComponent(panel7, label6, 0, 1, 1, 1);
		Commons.addComponent(panel7, label7, 0, 2, 1, 1);
		Commons.addComponent(panel7, label8, 0, 3, 1, 1);
		Commons.addComponent(panel7, label4, 1, 0, 1, 1);
		Commons.addComponent(panel7, label5, 2, 0, 1, 1);
		Commons.addComponent(panel7, label10, 3, 0, 1, 1);
		Commons.addComponent(panel7, textfield6_, 1, 1, 1, 1);
		Commons.addComponent(panel7, textfield7_, 1, 2, 1, 1);
		Commons.addComponent(panel7, textfield8_, 1, 3, 1, 1);
		Commons.addComponent(panel7, textfield9_, 2, 1, 1, 1);
		Commons.addComponent(panel7, textfield10_, 2, 2, 1, 1);
		Commons.addComponent(panel7, textfield11_, 2, 3, 1, 1);
		Commons.addComponent(panel7, textfield13_, 3, 1, 3, 1);
		Commons.addComponent(panel8, checkbox1_, 0, 0, 1, 1);
		Commons.addComponent(panel8, button3_, 0, 1, 1, 1);
		Commons.addComponent(panel9, checkbox2_, 0, 0, 1, 1);
		Commons.addComponent(panel9, button4_, 0, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel4, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel8, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		Commons.addComponent(panel3, panel6, 0, 0, 1, 1);
		Commons.addComponent(panel3, panel9, 1, 0, 1, 1);
		Commons.addComponent(panel3, panel7, 2, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// build tabbedpane and set font
		tabbedpane1_ = new JTabbedPane();

		// add panels to tabbedpane
		tabbedpane1_.addTab("Linear", panel1);
		tabbedpane1_.addTab("Radial", panel3);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", tabbedpane1_);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4_.addActionListener(this);
		radiobutton1_.addItemListener(this);
		radiobutton2_.addItemListener(this);
		radiobutton3_.addItemListener(this);
		radiobutton4_.addItemListener(this);
		radiobutton5_.addItemListener(this);
		radiobutton6_.addItemListener(this);
		checkbox1_.addItemListener(this);
		checkbox2_.addItemListener(this);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					actionOk();
					return null;
				}
			};

			// display progressor and still frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
		}

		// cancel button clicked
		else if (e.getSource().equals(button2_)) {

			// set dialog unvisible
			setVisible(false);
		}

		// options buttons clicked
		else if (e.getSource().equals(button3_)
				|| e.getSource().equals(button4_)) {

			// create dialog and set visible
			ReplicateElement2 dialog = new ReplicateElement2(this);
			dialog.setVisible(true);
		}
	}

	/**
	 * Sets the dialog still for displaying progressor.
	 * 
	 * @param arg0
	 *            True if still, False if not.
	 */
	private void setStill(boolean arg0) {

		// still
		if (arg0) {

			// disable buttons
			button1_.setEnabled(false);
			button2_.setEnabled(false);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// radiobutton1 event
		if (event.getSource().equals(radiobutton1_)) {

			// set textfields editable and combobox disabled
			textfield1_.setEditable(true);
			combobox1_.setEnabled(false);
		}

		// radiobutton2 event
		else if (event.getSource().equals(radiobutton2_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(true);
		}

		// radiobutton3 event
		else if (event.getSource().equals(radiobutton3_)) {

			// set textfields editable and combobox disabled
			textfield5_.setEditable(true);
			combobox2_.setEnabled(false);
		}

		// radiobutton4 event
		else if (event.getSource().equals(radiobutton4_)) {

			// set textfield disabled and combobox enabled
			textfield5_.setEditable(false);
			combobox2_.setEnabled(true);
		}

		// radiobutton5 event
		else if (event.getSource().equals(radiobutton5_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(false);
		}

		// radiobutton6 event
		else if (event.getSource().equals(radiobutton6_)) {

			// set textfield disabled and combobox enabled
			textfield5_.setEditable(false);
			combobox2_.setEnabled(false);
		}

		// checkbox1 event
		else if (event.getSource().equals(checkbox1_)) {

			// selected
			if (checkbox1_.isSelected())

				// set button3 disabled
				button3_.setEnabled(true);

			// not selected
			else if (checkbox1_.isSelected() == false)

				// set button3 disabled
				button3_.setEnabled(false);
		}

		// checkbox2 event
		else if (event.getSource().equals(checkbox2_)) {

			// selected
			if (checkbox2_.isSelected())

				// set button4 disabled
				button4_.setEnabled(true);

			// not selected
			else if (checkbox2_.isSelected() == false)

				// set button4 disabled
				button4_.setEnabled(false);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check element
		progressor_.setStatusMessage("Checking data...");
		if (checkIds()) {

			// check texts
			if (checkTexts()) {

				// linear option selected
				if (tabbedpane1_.getSelectedIndex() == 0) {

					// check if options clicked
					if (checkbox1_.isSelected() == false)
						for (int i = 0; i < options_.length; i++)
							options_[i] = false;

					// get entered values
					double val1 = Double.parseDouble(textfield2_.getText());
					double val2 = Double.parseDouble(textfield3_.getText());
					double val3 = Double.parseDouble(textfield4_.getText());
					int val4 = Integer.parseInt(textfield12_.getText());

					// move elements
					progressor_.setStatusMessage("Replicating elements...");
					if (Math.abs(val1) + Math.abs(val2) + Math.abs(val3) != 0.0)
						moveElements(val1, val2, val3, val4);

					// draw
					progressor_.setStatusMessage("Drawing...");
					owner_.drawPre();

					// close progressor
					progressor_.close();

					// close dialog
					setVisible(false);
				}

				// radial option is selected
				else if (tabbedpane1_.getSelectedIndex() == 1) {

					// check if options clicked
					if (checkbox2_.isSelected() == false)
						for (int i = 0; i < options_.length; i++)
							options_[i] = false;

					// get entered values
					double val1 = Double.parseDouble(textfield6_.getText());
					double val2 = Double.parseDouble(textfield7_.getText());
					double val3 = Double.parseDouble(textfield8_.getText());
					double val4 = Double.parseDouble(textfield9_.getText());
					double val5 = Double.parseDouble(textfield10_.getText());
					double val6 = Double.parseDouble(textfield11_.getText());
					int val7 = Integer.parseInt(textfield13_.getText());

					// move elements
					progressor_.setStatusMessage("Replicating elements...");
					double val8 = Math.abs(val1) + Math.abs(val2)
							+ Math.abs(val3);
					double val9 = Math.abs(val4) + Math.abs(val5)
							+ Math.abs(val6);
					if (val8 != 0.0 || val9 != 0.0)
						rotateElements(val1, val2, val3, val4, val5, val6, val7);

					// draw
					progressor_.setStatusMessage("Drawing...");
					owner_.drawPre();

					// close progressor
					progressor_.close();

					// close dialog
					setVisible(false);
				}
			}
		}
	}

	/**
	 * Rotates given elements by the given centroid and rotation angles.
	 * 
	 * @param val1
	 *            X coordinate of centroid.
	 * @param val2
	 *            Y coordinate of centroid.
	 * @param val3
	 *            Z coordinate of centroid.
	 * @param val4
	 *            Rotation about X axis.
	 * @param val5
	 *            Rotation about Y axis.
	 * @param val6
	 *            Rotation about Z axis.
	 * @param rep
	 *            The number of repetitions.
	 */
	private void rotateElements(double val1, double val2, double val3,
			double val4, double val5, double val6, int rep) {

		// create position vector of centroid
		DVec centroid = new DVec(3);
		centroid.set(0, val1);
		centroid.set(1, val2);
		centroid.set(2, val3);

		// create transformation matrix from rotation angles
		DMat tr = new DMat(val4, val5, val6, DMat.xyz_);

		// loop over would be rotated elements list
		for (int i = 0; i < elements1_.size(); i++) {

			// get element and its nodes
			Element e = elements1_.get(i);
			Node[] nodes = e.getNodes();

			// loop over repetitions
			for (int j = 0; j < rep; j++) {

				// create nodes
				nodes = createNodes(nodes, centroid, tr);

				// create new element
				createElement(nodes, e);
			}
		}
	}

	/**
	 * Replicates given elements by the given coordinate changes.
	 * 
	 * @param val1
	 *            Delta X value.
	 * @param val2
	 *            Delta Y value.
	 * @param val3
	 *            Delta Z value.
	 * @param rep
	 *            Number of repetitions.
	 */
	private void moveElements(double val1, double val2, double val3, int rep) {

		// create vector from change of coordinates
		DVec delta = new DVec(3);
		delta.set(0, val1);
		delta.set(1, val2);
		delta.set(2, val3);

		// loop over would be moved elements list
		for (int i = 0; i < elements1_.size(); i++) {

			// get element and its nodes
			Element e = elements1_.get(i);
			Node[] nodes = e.getNodes();

			// loop over repetitions
			for (int j = 0; j < rep; j++) {

				// create nodes
				nodes = createNodes(nodes, delta, null);

				// create new element
				createElement(nodes, e);
			}
		}
	}

	/**
	 * Creates and replaces the demanded element with the old one.
	 * 
	 */
	private void createElement(Node[] nod, Element e) {

		// get type of element
		int type = e.getType();

		// initialize replicate element
		Element elm = null;

		// element0
		if (type == ElementLibrary.element0_)
			elm = new Element0(nod[0], nod[1]);

		// element1
		else if (type == ElementLibrary.element1_)
			elm = new Element1(nod[0], nod[1], nod[2]);

		// element2
		else if (type == ElementLibrary.element2_)
			elm = new Element2(nod[0], nod[1], nod[2], nod[3]);

		// element3
		else if (type == ElementLibrary.element3_)
			elm = new Element3(nod[0], nod[1], nod[2], nod[3]);

		// element4
		else if (type == ElementLibrary.element4_)
			elm = new Element4(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7]);

		// element5
		else if (type == ElementLibrary.element5_)
			elm = new Element5(nod[0], nod[1], nod[2]);

		// element6
		else if (type == ElementLibrary.element6_)
			elm = new Element6(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5]);

		// element7
		else if (type == ElementLibrary.element7_)
			elm = new Element7(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7], nod[8], nod[9], nod[10], nod[11]);

		// element8
		else if (type == ElementLibrary.element8_)
			elm = new Element8(nod[0], nod[1], nod[2], nod[3]);

		// element9
		else if (type == ElementLibrary.element9_)
			elm = new Element9(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7]);

		// element10
		else if (type == ElementLibrary.element10_)
			elm = new Element10(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7], nod[8], nod[9], nod[10], nod[11]);

		// element11
		else if (type == ElementLibrary.element11_)
			elm = new Element11(nod[0], nod[1], nod[2]);

		// element12
		else if (type == ElementLibrary.element12_)
			elm = new Element12(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5]);

		// element13
		else if (type == ElementLibrary.element13_)
			elm = new Element13(nod[0], nod[1]);

		// element14
		else if (type == ElementLibrary.element14_)
			elm = new Element14(nod[0], nod[1], nod[2]);

		// element15
		else if (type == ElementLibrary.element15_)
			elm = new Element15(nod[0], nod[1], nod[2], nod[3]);

		// element16
		else if (type == ElementLibrary.element16_)
			elm = new Element16(nod[0], nod[1], nod[2], nod[3]);

		// element17
		else if (type == ElementLibrary.element17_)
			elm = new Element17(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7]);

		// element18
		else if (type == ElementLibrary.element18_)
			elm = new Element18(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5]);

		// element19
		else if (type == ElementLibrary.element19_)
			elm = new Element19(nod[0], nod[1], nod[2], nod[3]);

		// element20
		else if (type == ElementLibrary.element20_)
			elm = new Element20(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7]);

		// element21
		else if (type == ElementLibrary.element21_)
			elm = new Element21(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5]);

		// element22
		else if (type == ElementLibrary.element22_)
			elm = new Element22(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7]);

		// element23
		else if (type == ElementLibrary.element23_)
			elm = new Element23(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7], nod[8], nod[9], nod[10], nod[11], nod[12],
					nod[13], nod[14], nod[15], nod[16], nod[17], nod[18],
					nod[19]);

		// element24
		else if (type == ElementLibrary.element24_)
			elm = new Element24(nod[0], nod[1], nod[2], nod[3]);

		// element25
		else if (type == ElementLibrary.element25_)
			elm = new Element25(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7]);

		// element26
		else if (type == ElementLibrary.element26_)
			elm = new Element26(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5]);

		// element27
		else if (type == ElementLibrary.element27_)
			elm = new Element27(nod[0], nod[1]);

		// element28
		else if (type == ElementLibrary.element28_)
			elm = new Element28(nod[0], nod[1], nod[2]);

		// element29
		else if (type == ElementLibrary.element29_)
			elm = new Element29(nod[0], nod[1], nod[2], nod[3]);

		// element30
		else if (type == ElementLibrary.element30_)
			elm = new Element30(nod[0], nod[1], nod[2], nod[3]);

		// element31
		else if (type == ElementLibrary.element31_)
			elm = new Element31(nod[0], nod[1], nod[2], nod[3], nod[4], nod[5],
					nod[6], nod[7], nod[8], nod[9]);

		// element32
		else if (type == ElementLibrary.element32_)
			elm = new Element32(nod[0], nod[1]);

		// element33
		else if (type == ElementLibrary.element33_)
			elm = new Element33(nod[0], nod[1], nod[2]);

		// element34
		else if (type == ElementLibrary.element34_)
			elm = new Element34(nod[0], nod[1], nod[2], nod[3]);

		// element35
		else if (type == ElementLibrary.element35_)
			elm = new Element35(nod[0], nod[1]);

		// element36
		else if (type == ElementLibrary.element36_)
			elm = new Element36(nod[0], nod[1], nod[2]);

		// element37
		else if (type == ElementLibrary.element37_)
			elm = new Element37(nod[0], nod[1], nod[2], nod[3]);

		// element38
		else if (type == ElementLibrary.element38_)
			elm = new Element38(nod[0], nod[1]);

		// pass element properties to new element
		passElementProperties(e, elm);

		// add new element to structure
		owner_.structure_.addElement(elm);

		// add new element to groups
		addToGroups(e, elm);
	}

	/**
	 * Adds newly created element to original element's groups.
	 * 
	 * @param e1
	 *            The original element.
	 * @param e2
	 *            The new element.
	 */
	private void addToGroups(Element e1, Element e2) {

		// loop over groups
		for (int i = 0; i < owner_.inputData_.getGroup().size(); i++) {

			// get group
			Group group = owner_.inputData_.getGroup().get(i);

			// check if group contains original element
			if (group.containsElement(e1)) {

				// add new element to the group
				group.addElement(e2);
			}
		}
	}

	/**
	 * Passes element properties to newly created element.
	 * 
	 * @param e1
	 *            The original element.
	 * @param e2
	 *            The replicate element.
	 */
	private void passElementProperties(Element e1, Element e2) {

		// material
		if (options_[0])
			if (e1.getMaterial() != null)
				e2.setMaterial(e1.getMaterial());

		// section
		if (options_[1])
			if (e1.getSection() != null)
				e2.setSection(e1.getSection());

		// mechanical loads
		if (options_[2])
			try {
				if (e1.getAllMechLoads() != null)
					e2.setMechLoads(e1.getAllMechLoads());
			} catch (Exception excep) {
			}

		// temperature loads
		if (options_[3])
			e2.setTempLoads(e1.getAllTempLoads());

		// springs
		if (options_[4])
			try {
				if (e1.getSprings() != null)
					e2.setSprings(e1.getSprings());
			} catch (Exception excep) {
			}

		// masses
		if (options_[5])
			try {
				if (e1.getAdditionalMasses() != null)
					e2.setMasses(e1.getAdditionalMasses());
			} catch (Exception excep) {
			}

		// local axes
		if (options_[6])
			if (e1.getDimension() == ElementLibrary.oneDimensional_) {

				// get 1D elements
				Element1D e1D1 = (Element1D) e1;
				Element1D e1D2 = (Element1D) e2;

				// set local axes
				if (e1D1.getLocalAxis() != null)
					e1D2.setLocalAxis(e1D1.getLocalAxis());
			}

		// radii of curvatures
		int typ = e1.getType();
		if (typ == ElementLibrary.element19_
				|| typ == ElementLibrary.element20_
				|| typ == ElementLibrary.element21_
				|| typ == ElementLibrary.element27_
				|| typ == ElementLibrary.element28_
				|| typ == ElementLibrary.element29_)
			e2.setParameters(e1.getParameters());
	}

	/**
	 * Creates new element nodes and returns them.
	 * 
	 * @param nod
	 *            The nodes to be moved.
	 * @param delta
	 *            Vector storing the change of coordinates.
	 * @param tr
	 *            The transformation matrix used for rotating elements.
	 * @return The array storing the newly created element nodes.
	 */
	private Node[] createNodes(Node[] nod, DVec delta, DMat tr) {

		// create vector for storing positions of element nodes
		DVec[] pos = new DVec[nod.length];

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// compute position vectors of new nodes
			for (int i = 0; i < pos.length; i++)
				pos[i] = nod[i].getPosition().add(delta);
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// get position vectors of element nodes
			for (int i = 0; i < pos.length; i++) {
				pos[i] = nod[i].getPosition().subtract(delta);
				pos[i] = pos[i].transform(tr, DMat.toGlobal_);
				pos[i] = pos[i].add(delta);
				for (int j = 0; j < 3; j++)
					if (Math.abs(pos[i].get(j)) <= tolerance_)
						pos[i].set(j, 0.0);
			}
		}

		// loop over nodes
		Node[] nodes = new Node[pos.length];
		for (int i = 0; i < nodes.length; i++) {

			// check if there is any node at that coordinate
			nodes[i] = checkCoordinates(pos[i]);

			// if there is no, create and add node to structure
			if (nodes[i] == null) {
				nodes[i] = new Node(pos[i].get(0), pos[i].get(1), pos[i].get(2));

				// add node to structure
				owner_.structure_.addNode(nodes[i]);
			}
		}

		// return nodes array
		return nodes;
	}

	/**
	 * Checks whether given coordinates are occupied by a node of the structure
	 * or not.
	 * 
	 * @param pos
	 *            The coordinates to be checked.
	 * @return True if no nodes are at the given coordinates, False vice versa.
	 */
	private Node checkCoordinates(DVec pos) {

		// loop over nodes of structure
		for (int i = 0; i < owner_.structure_.getNumberOfNodes(); i++) {

			// get nodal position vector and length
			Node node = owner_.structure_.getNode(i);
			DVec pos1 = node.getPosition();

			// check coordinates
			if (pos.subtract(pos1).l2Norm() <= tolerance_)
				return node;
		}

		// no node exists at the same coordinates
		return null;
	}

	/**
	 * Checks whether the delta textfields are correct or not.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkTexts() {

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// get the entered texts
			String text1 = textfield2_.getText();
			String text2 = textfield3_.getText();
			String text3 = textfield4_.getText();
			String text4 = textfield12_.getText();

			// check for non-numeric values
			try {

				// convert texts to double values
				@SuppressWarnings("unused")
				double value = Double.parseDouble(text1);
				value = Double.parseDouble(text2);
				value = Double.parseDouble(text3);

				// convert text to integer value
				int value1 = Integer.parseInt(text4);

				// check if zero or negative
				if (value1 <= 0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(ReplicateElement1.this,
							"Illegal value for repetitions!",
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(ReplicateElement1.this,
						"Illegal values for coordinate changes!",
						"False data entry", 2);
				return false;
			}
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// get the entered texts
			String text1 = textfield6_.getText();
			String text2 = textfield7_.getText();
			String text3 = textfield8_.getText();
			String text4 = textfield9_.getText();
			String text5 = textfield10_.getText();
			String text6 = textfield11_.getText();
			String text7 = textfield13_.getText();

			// check for non-numeric values
			try {

				// convert texts to integer values
				@SuppressWarnings("unused")
				double value = Double.parseDouble(text1);
				value = Double.parseDouble(text2);
				value = Double.parseDouble(text3);
				value = Double.parseDouble(text4);
				value = Double.parseDouble(text5);
				value = Double.parseDouble(text6);

				// convert text to integer value
				int value1 = Integer.parseInt(text7);

				// check if zero or negative
				if (value1 <= 0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(ReplicateElement1.this,
							"Illegal value for repetitions!",
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(ReplicateElement1.this,
						"Illegal values!", "False data entry", 2);
				return false;
			}
		}

		// entered values are correct
		return true;
	}

	/**
	 * Checks whether any element of the structure has the given coordinates or
	 * not.
	 * 
	 * @return True if the given element ids are valid for moving, False vice
	 *         versa.
	 */
	private boolean checkIds() {

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// set element ids from textfield
			if (radiobutton1_.isSelected()) {
				if (setIdsFromText() == false)
					return false;
			}

			// set element ids from groups combobox
			else if (radiobutton2_.isSelected())
				setIdsFromGroup();

			// set element ids for all elements
			else if (radiobutton5_.isSelected())
				setIdsForAll();
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// set element ids from textfield
			if (radiobutton3_.isSelected()) {
				if (setIdsFromText() == false)
					return false;
			}

			// set element ids from groups combobox
			else if (radiobutton4_.isSelected())
				setIdsFromGroup();

			// set element ids for all elements
			else if (radiobutton6_.isSelected())
				setIdsForAll();
		}

		// check for existance
		elements1_.clear();
		try {

			// check if given elements exist
			for (int i = 0; i < elementIds_.size(); i++) {

				// get element
				Element e = owner_.structure_.getElement(elementIds_.get(i));

				// add to would be moved element list
				elements1_.add(e);
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(ReplicateElement1.this,
					"Given elements do not exist!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Checks and sets division element ids from textfield.
	 * 
	 * @return True if entered text is correct, False vice versa.
	 */
	private boolean setIdsFromText() {

		String text = null;

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// get the entered text
			text = textfield1_.getText();
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// get the entered text
			text = textfield5_.getText();
		}

		// eliminate spaces
		String elText = "";
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c != " ".charAt(0))
				elText += c;
		}

		// check for non-numeric or negative values
		try {

			// seperate components
			String[] comp = elText.split(",");

			// convert text to integer value
			elementIds_.clear();
			for (int i = 0; i < comp.length; i++) {
				elementIds_.add(Integer.parseInt(comp[i]));

				// check if negative
				if (elementIds_.get(i) < 0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(ReplicateElement1.this,
							"Illegal values!", "False data entry", 2);
					return false;
				}
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(ReplicateElement1.this,
					"Given elements do not exist!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Sets division element ids from groups combobox.
	 * 
	 */
	private void setIdsFromGroup() {

		int index = 0;

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// get the selected group
			index = combobox1_.getSelectedIndex();
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// get the selected group
			index = combobox2_.getSelectedIndex();
		}
		Group group = owner_.inputData_.getGroup().get(index);

		// get elements of group
		Vector<Element> elements = group.getElements();

		// get indices of group elements
		elementIds_.clear();
		for (int i = 0; i < elements.size(); i++) {

			// get the index of group element
			int n = owner_.structure_.indexOfElement(elements.get(i));

			// add to values vector
			elementIds_.add(n);
		}
	}

	/**
	 * Sets mesh element ids for all elements.
	 * 
	 */
	private void setIdsForAll() {

		// get indices of all elements
		elementIds_.clear();
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++)
			elementIds_.add(i);
	}

	/**
	 * Returns group names array.
	 * 
	 * @return The group names array.
	 */
	private String[] getGroups() {

		// initialize group names array
		int n = owner_.inputData_.getGroup().size();
		String[] groups = new String[n];

		// loop over groups
		for (int i = 0; i < n; i++) {

			// get name of group
			groups[i] = owner_.inputData_.getGroup().get(i).getName();
		}

		// return group names array
		return groups;
	}
}
