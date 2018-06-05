package dialogs.display;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DMat;
import matrix.DVec;

import element.Element;
import element.Element1D;
import element.ElementLibrary;

/**
 * Class for Display Table Line Results menu.
 * 
 * @author Murat
 * 
 */
public class DisplayTLineResult1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JSpinner spinner1_;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_;

	private JComboBox combobox1_;

	private JLabel label4_, label5_, label6_, label7_, label8_, label9_;

	private JButton button1_, button2_;

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
	public DisplayTLineResult1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Line Results", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Line", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Options", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Average Results", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Line ID :");
		JLabel label2 = new JLabel("Step :");
		JLabel label3 = new JLabel("Type :");
		label4_ = new JLabel("U1 :");
		label5_ = new JLabel("U2 :");
		label6_ = new JLabel("U3 :");
		label7_ = new JLabel("R1 :");
		label8_ = new JLabel("R2 :");
		label9_ = new JLabel("R3 :");
		label4_.setPreferredSize(new Dimension(33, 20));

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types[] = { "Displacements", "Elastic strains", "Stresses",
				"Internal forces", "Principle strains", "Principle stresses",
				"Mises stress" };
		combobox1_ = new JComboBox(types);
		combobox1_.setMaximumRowCount(4);

		// build spinner
		SpinnerNumberModel spinnerModel1 = new SpinnerNumberModel();
		spinnerModel1.setMinimum(0);
		spinnerModel1.setMaximum(owner_.structure_.getNumberOfSteps() - 1);
		spinner1_ = new JSpinner(spinnerModel1);
		spinner1_.setPreferredSize(new Dimension(158, 20));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield2_.setEditable(false);
		textfield3_.setEditable(false);
		textfield4_.setEditable(false);
		textfield5_.setEditable(false);
		textfield6_.setEditable(false);
		textfield7_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(80, 20));
		textfield2_.setPreferredSize(new Dimension(156, 20));

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Show");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button2_, 0, 2, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, spinner1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel5, label4_, 0, 0, 1, 1);
		Commons.addComponent(panel5, label5_, 1, 0, 1, 1);
		Commons.addComponent(panel5, label6_, 2, 0, 1, 1);
		Commons.addComponent(panel5, label7_, 3, 0, 1, 1);
		Commons.addComponent(panel5, label8_, 4, 0, 1, 1);
		Commons.addComponent(panel5, label9_, 5, 0, 1, 1);
		Commons.addComponent(panel5, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel5, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel5, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel5, textfield5_, 3, 1, 1, 1);
		Commons.addComponent(panel5, textfield6_, 4, 1, 1, 1);
		Commons.addComponent(panel5, textfield7_, 5, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		panel2.add(button1_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		combobox1_.addItemListener(this);

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_)) {

			// set dialog unvisible
			setVisible(false);
		}

		// show button clicked
		else if (e.getSource().equals(button2_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					showLine();
					return null;
				}
			};

			// display progressor and disable frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
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
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void showLine() {

		// check step number
		progressor_.setStatusMessage("Checking data...");
		if (checkText()) {

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

					// get 1D element
					Element1D e1D = (Element1D) e;

					// compute stationary points in natural coordinates
					double distance = 2.0 / (10 - 1.0);
					double[] eps1 = new double[10];
					eps1[0] = -1.0;
					for (int i = 0; i < 10 - 1; i++)
						eps1[i + 1] = eps1[i] + distance;

					// get demanded step number
					int step = (Integer) spinner1_.getValue();

					// set step number to element nodes
					progressor_.setStatusMessage("Setting step number...");
					owner_.structure_.setStepToElement(owner_.path_, e1D, step);

					// displacements
					progressor_
							.setStatusMessage("Computing demanded values...");
					if (combobox1_.getSelectedIndex() == 0)
						computeDisplacements(e1D, eps1);

					// elastic strains
					else if (combobox1_.getSelectedIndex() == 1)
						computeStrains(e1D, eps1);

					// stresses
					else if (combobox1_.getSelectedIndex() == 2)
						computeStresses(e1D, eps1);

					// internal forces
					else if (combobox1_.getSelectedIndex() == 3)
						computeInternalForces(e1D, eps1);

					// principle strains
					else if (combobox1_.getSelectedIndex() == 4)
						computePrincipleStrains(e1D, eps1);

					// principle stresses
					else if (combobox1_.getSelectedIndex() == 5)
						computePrincipleStresses(e1D, eps1);

					// mises stress
					else if (combobox1_.getSelectedIndex() == 6)
						computeMisesStress(e1D, eps1);

					// close progressor
					progressor_.close();
					setStill(false);
				}

				// not one dimensional
				else {

					// close progressor and enable dialog
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this,
							"Given element is not one dimensional!",
							"False data entry", 2);

					// set textfields
					textfield2_.setText("");
					textfield3_.setText("");
					textfield4_.setText("");
					textfield5_.setText("");
					textfield6_.setText("");
					textfield7_.setText("");
				}
			} catch (Exception excep) {

				// close progressor and enable dialog
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						"Given element does not exist!", "False data entry", 2);

				// set textfields
				textfield2_.setText("");
				textfield3_.setText("");
				textfield4_.setText("");
				textfield5_.setText("");
				textfield6_.setText("");
				textfield7_.setText("");
			}
		}
	}

	/**
	 * If the related checkbox is selected, sets default value to textfield and
	 * makes it editable. If the checkbox is deselected, clears textfield and
	 * makes it uneditable.
	 */
	public void itemStateChanged(ItemEvent event) {

		// combobox1 event
		if (event.getSource().equals(combobox1_)) {

			// get selected index
			int index = combobox1_.getSelectedIndex();

			// displacements
			if (index == 0) {

				// set labels
				label4_.setText("U1 :");
				label5_.setText("U2 :");
				label6_.setText("U3 :");
				label7_.setText("R1 :");
				label8_.setText("R2 :");
				label9_.setText("R3 :");
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(true);
				label7_.setVisible(true);
				label8_.setVisible(true);
				label9_.setVisible(true);
			}

			// elastic strains
			else if (index == 1) {

				// set labels
				label4_.setText("E11 :");
				label5_.setText("E22 :");
				label6_.setText("E33 :");
				label7_.setText("E12 :");
				label8_.setText("E13 :");
				label9_.setText("E23 :");
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(true);
				label7_.setVisible(true);
				label8_.setVisible(true);
				label9_.setVisible(true);
			}

			// stresses
			else if (index == 2) {

				// set labels
				label4_.setText("S11 :");
				label5_.setText("S22 :");
				label6_.setText("S33 :");
				label7_.setText("S12 :");
				label8_.setText("S13 :");
				label9_.setText("S23 :");
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(true);
				label7_.setVisible(true);
				label8_.setVisible(true);
				label9_.setVisible(true);
			}

			// internal forces
			else if (index == 3) {

				// set labels
				label4_.setText("N1 :");
				label5_.setText("V2 :");
				label6_.setText("V3 :");
				label7_.setText("T1 :");
				label8_.setText("M2 :");
				label9_.setText("M3 :");
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(true);
				label7_.setVisible(true);
				label8_.setVisible(true);
				label9_.setVisible(true);
			}

			// principle strains
			else if (index == 4) {

				// set labels
				label4_.setText("Emin :");
				label5_.setText("Emid :");
				label6_.setText("Emax :");
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(true);
				label7_.setVisible(false);
				label8_.setVisible(false);
				label9_.setVisible(false);
			}

			// principle stresses
			else if (index == 5) {

				// set labels
				label4_.setText("Smin :");
				label5_.setText("Smid :");
				label6_.setText("Smax :");
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(true);
				label7_.setVisible(false);
				label8_.setVisible(false);
				label9_.setVisible(false);
			}

			// mises stress
			else if (index == 6) {

				// set labels
				label4_.setText("SVM :");
				label4_.setVisible(true);
				label5_.setVisible(false);
				label6_.setVisible(false);
				label7_.setVisible(false);
				label8_.setVisible(false);
				label9_.setVisible(false);
			}

			// set textfields
			textfield2_.setText("");
			textfield3_.setText("");
			textfield4_.setText("");
			textfield5_.setText("");
			textfield6_.setText("");
			textfield7_.setText("");
		}
	}

	/**
	 * Computes average element displacements.
	 * 
	 * @param e
	 *            Element for displacement computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 */
	private void computeDisplacements(Element1D e, double[] eps1) {

		// initialize displacement vector
		DVec avDisp = new DVec(6);

		// loop over stations
		for (int i = 0; i < eps1.length; i++) {

			// get element displacement vector
			DVec disp = e.getDisplacement(eps1[i], 0.0, 0.0);

			// add to average displacement vector
			avDisp = avDisp.add(disp.scale(1.0 / eps1.length));
		}

		// set textfields
		textfield2_.setText(owner_.formatter_.format(avDisp.get(0)));
		textfield3_.setText(owner_.formatter_.format(avDisp.get(1)));
		textfield4_.setText(owner_.formatter_.format(avDisp.get(2)));
		textfield5_.setText(owner_.formatter_.format(avDisp.get(3)));
		textfield6_.setText(owner_.formatter_.format(avDisp.get(4)));
		textfield7_.setText(owner_.formatter_.format(avDisp.get(5)));
	}

	/**
	 * Computes average element elastic strains.
	 * 
	 * @param e
	 *            Element for strain computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 */
	private void computeStrains(Element1D e, double[] eps1) {

		// initialize strain vector
		DVec avStrain = new DVec(6);

		// loop over stations
		for (int i = 0; i < eps1.length; i++) {

			// get element strain tensor
			DMat strain = e.getStrain(eps1[i], 0.0, 0.0);

			// add to average strain vector
			avStrain.add(0, strain.get(0, 0) / eps1.length);
			avStrain.add(1, strain.get(1, 1) / eps1.length);
			avStrain.add(2, strain.get(2, 2) / eps1.length);
			avStrain.add(3, strain.get(0, 1) / eps1.length);
			avStrain.add(4, strain.get(0, 2) / eps1.length);
			avStrain.add(5, strain.get(1, 2) / eps1.length);
		}

		// set textfields
		textfield2_.setText(owner_.formatter_.format(avStrain.get(0)));
		textfield3_.setText(owner_.formatter_.format(avStrain.get(1)));
		textfield4_.setText(owner_.formatter_.format(avStrain.get(2)));
		textfield5_.setText(owner_.formatter_.format(avStrain.get(3)));
		textfield6_.setText(owner_.formatter_.format(avStrain.get(4)));
		textfield7_.setText(owner_.formatter_.format(avStrain.get(5)));
	}

	/**
	 * Computes average element stresses.
	 * 
	 * @param e
	 *            Element for stress computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 */
	private void computeStresses(Element1D e, double[] eps1) {

		// initialize stress vector
		DVec avStress = new DVec(6);

		// loop over stations
		for (int i = 0; i < eps1.length; i++) {

			// get element stress tensor
			DMat stress = e.getStress(eps1[i], 0.0, 0.0);

			// add to average stress vector
			avStress.add(0, stress.get(0, 0) / eps1.length);
			avStress.add(1, stress.get(1, 1) / eps1.length);
			avStress.add(2, stress.get(2, 2) / eps1.length);
			avStress.add(3, stress.get(0, 1) / eps1.length);
			avStress.add(4, stress.get(0, 2) / eps1.length);
			avStress.add(5, stress.get(1, 2) / eps1.length);
		}

		// set textfields
		textfield2_.setText(owner_.formatter_.format(avStress.get(0)));
		textfield3_.setText(owner_.formatter_.format(avStress.get(1)));
		textfield4_.setText(owner_.formatter_.format(avStress.get(2)));
		textfield5_.setText(owner_.formatter_.format(avStress.get(3)));
		textfield6_.setText(owner_.formatter_.format(avStress.get(4)));
		textfield7_.setText(owner_.formatter_.format(avStress.get(5)));
	}

	/**
	 * Computes average element internal forces.
	 * 
	 * @param e
	 *            Element for internal force computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 */
	private void computeInternalForces(Element1D e, double[] eps1) {

		// initialize internal force vector
		DVec avForce = new DVec(6);

		// loop over stations
		for (int j = 0; j < eps1.length; j++) {

			// get element internal forces
			double n1 = e.getInternalForce(Element1D.N1_, eps1[j], 0.0, 0.0);
			double v2 = e.getInternalForce(Element1D.V2_, eps1[j], 0.0, 0.0);
			double v3 = e.getInternalForce(Element1D.V3_, eps1[j], 0.0, 0.0);
			double t1 = e.getInternalForce(Element1D.T1_, eps1[j], 0.0, 0.0);
			double m2 = e.getInternalForce(Element1D.M2_, eps1[j], 0.0, 0.0);
			double m3 = e.getInternalForce(Element1D.M3_, eps1[j], 0.0, 0.0);

			// add to average internal force vector
			avForce.add(0, n1 / eps1.length);
			avForce.add(1, v2 / eps1.length);
			avForce.add(2, v3 / eps1.length);
			avForce.add(3, t1 / eps1.length);
			avForce.add(4, m2 / eps1.length);
			avForce.add(5, m3 / eps1.length);
		}

		// set textfields
		textfield2_.setText(owner_.formatter_.format(avForce.get(0)));
		textfield3_.setText(owner_.formatter_.format(avForce.get(1)));
		textfield4_.setText(owner_.formatter_.format(avForce.get(2)));
		textfield5_.setText(owner_.formatter_.format(avForce.get(3)));
		textfield6_.setText(owner_.formatter_.format(avForce.get(4)));
		textfield7_.setText(owner_.formatter_.format(avForce.get(5)));
	}

	/**
	 * Computes average element principle strains.
	 * 
	 * @param e
	 *            Element for principle strain computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 */
	private void computePrincipleStrains(Element1D e, double[] eps1) {

		// initialize principle strain vector
		DVec avStrain = new DVec(3);

		// loop over stations
		for (int i = 0; i < eps1.length; i++) {

			// get element principle strains
			double emin = e.getPrincipalStrain(eps1[i], 0.0, 0.0,
					Element.minPrincipal_);
			double emid = e.getPrincipalStrain(eps1[i], 0.0, 0.0,
					Element.midPrincipal_);
			double emax = e.getPrincipalStrain(eps1[i], 0.0, 0.0,
					Element.maxPrincipal_);

			// add to average principle strains vector
			avStrain.add(0, emin / eps1.length);
			avStrain.add(1, emid / eps1.length);
			avStrain.add(2, emax / eps1.length);
		}

		// set textfields
		textfield2_.setText(owner_.formatter_.format(avStrain.get(0)));
		textfield3_.setText(owner_.formatter_.format(avStrain.get(1)));
		textfield4_.setText(owner_.formatter_.format(avStrain.get(2)));
	}

	/**
	 * Computes average element principle stresses.
	 * 
	 * @param e
	 *            Element for principle stress computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 */
	private void computePrincipleStresses(Element1D e, double[] eps1) {

		// initialize principle stress vector
		DVec avStress = new DVec(3);

		// loop over stations
		for (int i = 0; i < eps1.length; i++) {

			// get element principle stress
			double smin = e.getPrincipalStress(eps1[i], 0.0, 0.0,
					Element.minPrincipal_);
			double smid = e.getPrincipalStress(eps1[i], 0.0, 0.0,
					Element.midPrincipal_);
			double smax = e.getPrincipalStress(eps1[i], 0.0, 0.0,
					Element.maxPrincipal_);

			// add to average principle strains vector
			avStress.add(0, smin / eps1.length);
			avStress.add(1, smid / eps1.length);
			avStress.add(2, smax / eps1.length);
		}

		// set textfields
		textfield2_.setText(owner_.formatter_.format(avStress.get(0)));
		textfield3_.setText(owner_.formatter_.format(avStress.get(1)));
		textfield4_.setText(owner_.formatter_.format(avStress.get(2)));
	}

	/**
	 * Computes average element mises stress.
	 * 
	 * @param e
	 *            Element for mises stress computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 */
	private void computeMisesStress(Element1D e, double[] eps1) {

		// initialize mises stress
		double stress = 0.0;

		// loop over stations
		for (int i = 0; i < eps1.length; i++)
			stress += e.getVonMisesStress(eps1[i], 0.0, 0.0) / eps1.length;

		// set textfields
		textfield2_.setText(owner_.formatter_.format(stress));
	}

	/**
	 * Cgecks entered textfields.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkText() {

		// check for non-numeric or negative values
		try {

			// convert text1 to integer value
			int val1 = (Integer) spinner1_.getValue();

			// check if negative
			if (val1 < 0) {

				// close progressor and enable dialog
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane
						.showMessageDialog(this,
								"Illegal value for step number!",
								"False data entry", 2);

				// set textfields
				textfield2_.setText("");
				textfield3_.setText("");
				textfield4_.setText("");
				textfield5_.setText("");
				textfield6_.setText("");
				textfield7_.setText("");

				return false;
			}

			// check step number
			if (val1 >= owner_.structure_.getNumberOfSteps()) {

				// close progressor and enable dialog
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane
						.showMessageDialog(this,
								"Illegal value for step number!",
								"False data entry", 2);

				// set textfields
				textfield2_.setText("");
				textfield3_.setText("");
				textfield4_.setText("");
				textfield5_.setText("");
				textfield6_.setText("");
				textfield7_.setText("");

				return false;
			}
		} catch (Exception excep) {

			// close progressor and enable dialog
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this,
					"Illegal value for step number!", "False data entry", 2);

			// set textfields
			textfield2_.setText("");
			textfield3_.setText("");
			textfield4_.setText("");
			textfield5_.setText("");
			textfield6_.setText("");
			textfield7_.setText("");

			return false;
		}

		// entered values are correct
		return true;
	}
}
