package dialogs.display;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import analysis.Analysis;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import math.GraphPlot2D;
import matrix.DMat;
import matrix.DVec;
import element.Element;
import element.Element1D;
import element.ElementLibrary;

/**
 * Class for Display Line History Plot menu.
 * 
 * @author Murat
 * 
 */
public class DisplayLinePlot1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JSpinner spinner1_, spinner2_;

	private JTextField textfield1_;

	private JComboBox combobox1_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_, radiobutton5_, radiobutton6_;

	/** Plotter of this dialog. */
	protected GraphPlot2D graph_ = new GraphPlot2D();

	private JButton button1_, button2_, button3_;

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
	public DisplayLinePlot1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Line History Plot", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Line and Options", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Stepping Info", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Result Options", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Component", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Line ID :");
		JLabel label2 = new JLabel("Initial step :");
		JLabel label3 = new JLabel("Final step :");
		JLabel label4 = new JLabel("Type :");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types[] = { "Displacements", "Elastic strains", "Stresses",
				"Internal forces", "Principle strains", "Principle stresses",
				"Mises stress" };
		combobox1_ = new JComboBox(types);
		combobox1_.setMaximumRowCount(4);
		combobox1_.setPreferredSize(new Dimension(168, 23));

		// build spinner
		SpinnerNumberModel spinnerModel1 = new SpinnerNumberModel();
		spinnerModel1.setMinimum(0);
		spinnerModel1.setMaximum(owner_.structure_.getNumberOfSteps() - 1);
		SpinnerNumberModel spinnerModel2 = new SpinnerNumberModel();
		spinnerModel2.setMinimum(0);
		spinnerModel2.setMaximum(owner_.structure_.getNumberOfSteps() - 1);
		spinner1_ = new JSpinner(spinnerModel1);
		spinner2_ = new JSpinner(spinnerModel2);
		spinner1_.setPreferredSize(new Dimension(139, 20));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(80, 20));

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Options");
		button3_ = new JButton("Cancel");

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("U1", true);
		radiobutton2_ = new JRadioButton("U2", false);
		radiobutton3_ = new JRadioButton("U3", false);
		radiobutton4_ = new JRadioButton("R1", false);
		radiobutton5_ = new JRadioButton("R2", false);
		radiobutton6_ = new JRadioButton("R3", false);
		radiobutton1_.setPreferredSize(new Dimension(55, 23));
		radiobutton2_.setPreferredSize(new Dimension(55, 23));
		radiobutton3_.setPreferredSize(new Dimension(55, 23));

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton3_);
		buttongroup1.add(radiobutton4_);
		buttongroup1.add(radiobutton5_);
		buttongroup1.add(radiobutton6_);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button2_, 0, 2, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, spinner1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, spinner2_, 1, 1, 1, 1);
		Commons.addComponent(panel5, label4, 0, 0, 1, 1);
		Commons.addComponent(panel5, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel6, radiobutton2_, 0, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton3_, 0, 2, 1, 1);
		Commons.addComponent(panel6, radiobutton4_, 1, 0, 1, 1);
		Commons.addComponent(panel6, radiobutton5_, 1, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton6_, 1, 2, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		Commons.addComponent(panel1, panel6, 3, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button3_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
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

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					plotLine();
					return null;
				}
			};

			// display progressor and still frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
		}

		// options button clicked
		else if (e.getSource().equals(button2_)) {

			// create and set dialog visible
			DisplayLinePlot2 dialog = new DisplayLinePlot2(this);
			dialog.setVisible(true);
		}

		// cancel button clicked
		else if (e.getSource().equals(button3_)) {

			// set dialog unvisible
			setVisible(false);
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
			button3_.setEnabled(false);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);
			button3_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void plotLine() {

		// check stepping numbers
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

					// get step numbers
					int step1 = (Integer) spinner1_.getValue();
					int step2 = (Integer) spinner2_.getValue();

					// get demanded component
					int comp = 0;
					if (radiobutton1_.isSelected())
						comp = 0;
					else if (radiobutton2_.isSelected())
						comp = 1;
					else if (radiobutton3_.isSelected())
						comp = 2;
					else if (radiobutton4_.isSelected())
						comp = 3;
					else if (radiobutton5_.isSelected())
						comp = 4;
					else if (radiobutton6_.isSelected())
						comp = 5;

					// get analysis type
					int type = (Integer) owner_.structure_.getAnalysisInfo()
							.get(1);

					// initialize x and y value vectors
					DVec xVal = new DVec(step2 - step1 + 1);
					DVec yVal = new DVec(step2 - step1 + 1);

					// set x and y labels
					String xLabel = getXLabel(type);
					String yLabel = getYLabel();

					// initialize vector index
					int i = 0;

					// loop over demanded steps
					progressor_.setStatusMessage("Forming graph data...");
					for (int j = step1; j <= step2; j++) {

						// set x values
						setXValues(type, xVal, i, j);

						// set y values
						setYValues(e1D, comp, yVal, i, j, eps1);

						// renew result index
						i++;
					}

					// set x and y values to plotter
					graph_.setValues(xVal.get1DArray(), yVal.get1DArray());

					// set labels to plotter
					graph_.setLabels(xLabel, yLabel);

					// plot
					progressor_.setStatusMessage("Plotting...");
					graph_.plot(owner_.viewer_);

					// close progressor
					progressor_.close();

					// set dialog unvisible
					setVisible(false);
				}

				// not one dimensional
				else {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this,
							"Given element is not one dimensional!",
							"False data entry", 2);
				}
			}

			// line does not exist
			catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						"Given line does not exist!", "False data entry", 2);
			}
		}
	}

	/**
	 * Sets y values vector.
	 * 
	 * @param e1D
	 *            One dimensional element subjected for computation.
	 * @param comp
	 *            The selected component of the result.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param i
	 *            Index for setting the value.
	 * @param j
	 *            Index for the step number.
	 */
	private void setYValues(Element1D e1D, int comp, DVec yVal, int i, int j,
			double[] eps1) {

		// set step number to element
		owner_.structure_.setStepToElement(owner_.path_, e1D, j);

		// displacements
		if (combobox1_.getSelectedIndex() == 0)
			computeDisplacements(e1D, eps1, yVal, comp, i);

		// elastic strains
		else if (combobox1_.getSelectedIndex() == 1)
			computeStrains(e1D, eps1, yVal, comp, i);

		// stresses
		else if (combobox1_.getSelectedIndex() == 2)
			computeStresses(e1D, eps1, yVal, comp, i);

		// internal forces
		else if (combobox1_.getSelectedIndex() == 3)
			computeInternalForces(e1D, eps1, yVal, comp, i);

		// principle strains
		else if (combobox1_.getSelectedIndex() == 4)
			computePrincipleStrains(e1D, eps1, yVal, comp, i);

		// principle stresses
		else if (combobox1_.getSelectedIndex() == 5)
			computePrincipleStresses(e1D, eps1, yVal, comp, i);

		// mises stress
		else if (combobox1_.getSelectedIndex() == 6)
			computeMisesStress(e1D, eps1, yVal, comp, i);
	}

	/**
	 * Computes average element displacements.
	 * 
	 * @param e
	 *            Element for displacement computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param comp
	 *            The selected component of the result.
	 * @param i
	 *            Index for setting the value.
	 */
	private void computeDisplacements(Element1D e, double[] eps1, DVec yVal,
			int comp, int i) {

		// initialize displacement vector
		DVec avDisp = new DVec(6);

		// loop over stations
		for (int j = 0; j < eps1.length; j++) {

			// get element displacement vector
			DVec disp = e.getDisplacement(eps1[j], 0.0, 0.0);

			// add to average displacement vector
			avDisp = avDisp.add(disp.scale(1.0 / eps1.length));
		}

		// set to vector
		yVal.set(i, avDisp.get(comp));
	}

	/**
	 * Computes average element elastic strains.
	 * 
	 * @param e
	 *            Element for displacement computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param comp
	 *            The selected component of the result.
	 * @param i
	 *            Index for setting the value.
	 */
	private void computeStrains(Element1D e, double[] eps1, DVec yVal,
			int comp, int i) {

		// initialize strain vector
		DVec avStrain = new DVec(6);

		// loop over stations
		for (int j = 0; j < eps1.length; j++) {

			// get element strain tensor
			DMat strain = e.getStrain(eps1[j], 0.0, 0.0);

			// add to average strain vector
			avStrain.add(0, strain.get(0, 0) / eps1.length);
			avStrain.add(1, strain.get(1, 1) / eps1.length);
			avStrain.add(2, strain.get(2, 2) / eps1.length);
			avStrain.add(3, strain.get(0, 1) / eps1.length);
			avStrain.add(4, strain.get(0, 2) / eps1.length);
			avStrain.add(5, strain.get(1, 2) / eps1.length);
		}

		// set to vector
		yVal.set(i, avStrain.get(comp));
	}

	/**
	 * Computes average element stresses.
	 * 
	 * @param e
	 *            Element for displacement computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param comp
	 *            The selected component of the result.
	 * @param i
	 *            Index for setting the value.
	 */
	private void computeStresses(Element1D e, double[] eps1, DVec yVal,
			int comp, int i) {

		// initialize stress vector
		DVec avStress = new DVec(6);

		// loop over stations
		for (int j = 0; j < eps1.length; j++) {

			// get element stress tensor
			DMat stress = e.getStress(eps1[j], 0.0, 0.0);

			// add to average stress vector
			avStress.add(0, stress.get(0, 0) / eps1.length);
			avStress.add(1, stress.get(1, 1) / eps1.length);
			avStress.add(2, stress.get(2, 2) / eps1.length);
			avStress.add(3, stress.get(0, 1) / eps1.length);
			avStress.add(4, stress.get(0, 2) / eps1.length);
			avStress.add(5, stress.get(1, 2) / eps1.length);
		}

		// set to vector
		yVal.set(i, avStress.get(comp));
	}

	/**
	 * Computes average element internal forces.
	 * 
	 * @param e
	 *            Element for displacement computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param comp
	 *            The selected component of the result.
	 * @param i
	 *            Index for setting the value.
	 */
	private void computeInternalForces(Element1D e, double[] eps1, DVec yVal,
			int comp, int i) {

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

		// set to vector
		yVal.set(i, avForce.get(comp));
	}

	/**
	 * Computes average element principle strains.
	 * 
	 * @param e
	 *            Element for displacement computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param comp
	 *            The selected component of the result.
	 * @param i
	 *            Index for setting the value.
	 */
	private void computePrincipleStrains(Element1D e, double[] eps1, DVec yVal,
			int comp, int i) {

		// initialize principle strain vector
		DVec avStrain = new DVec(3);

		// loop over stations
		for (int j = 0; j < eps1.length; j++) {

			// get element principle strains
			double emin = e.getPrincipalStrain(eps1[j], 0.0, 0.0,
					Element.minPrincipal_);
			double emid = e.getPrincipalStrain(eps1[j], 0.0, 0.0,
					Element.midPrincipal_);
			double emax = e.getPrincipalStrain(eps1[j], 0.0, 0.0,
					Element.maxPrincipal_);

			// add to average principle strains vector
			avStrain.add(0, emin / eps1.length);
			avStrain.add(1, emid / eps1.length);
			avStrain.add(2, emax / eps1.length);
		}

		// set to vector
		yVal.set(i, avStrain.get(comp));
	}

	/**
	 * Computes average element principle stresses.
	 * 
	 * @param e
	 *            Element for displacement computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param comp
	 *            The selected component of the result.
	 * @param i
	 *            Index for setting the value.
	 */
	private void computePrincipleStresses(Element1D e, double[] eps1,
			DVec yVal, int comp, int i) {

		// initialize principle stress vector
		DVec avStress = new DVec(3);

		// loop over stations
		for (int j = 0; j < eps1.length; j++) {

			// get element principle stress
			double smin = e.getPrincipalStress(eps1[j], 0.0, 0.0,
					Element.minPrincipal_);
			double smid = e.getPrincipalStress(eps1[j], 0.0, 0.0,
					Element.midPrincipal_);
			double smax = e.getPrincipalStress(eps1[j], 0.0, 0.0,
					Element.maxPrincipal_);

			// add to average principle strains vector
			avStress.add(0, smin / eps1.length);
			avStress.add(1, smid / eps1.length);
			avStress.add(2, smax / eps1.length);
		}

		// set to vector
		yVal.set(i, avStress.get(comp));
	}

	/**
	 * Computes average element mises stress.
	 * 
	 * @param e
	 *            Element for displacement computation.
	 * @param eps1
	 *            Array storing the natural coordinates of stationary points.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param comp
	 *            The selected component of the result.
	 * @param i
	 *            Index for setting the value.
	 */
	private void computeMisesStress(Element1D e, double[] eps1, DVec yVal,
			int comp, int i) {

		// initialize mises stress
		double stress = 0.0;

		// loop over stations
		for (int j = 0; j < eps1.length; j++)
			stress += e.getVonMisesStress(eps1[j], 0.0, 0.0) / eps1.length;

		// set to vector
		yVal.set(i, stress);
	}

	/**
	 * Sets x values vector depending on the analysis type.
	 * 
	 * @param type
	 *            The type of analysis.
	 * @param xVal
	 *            X values vector to be set.
	 * @param i
	 *            Index for setting the value.
	 * @param j
	 *            Index for the step number.
	 */
	private void setXValues(int type, DVec xVal, int i, int j) {

		// linear dynamic analysis
		if (type == Analysis.linearTransient_) {

			// get time step size
			double dt = (Double) owner_.structure_.getAnalysisInfo().get(6);

			// set value
			xVal.set(i, j * dt + dt);
		}

		// modal analysis
		else if (type == Analysis.modal_) {

			// set value
			xVal.set(i, j);
		}
	}

	/**
	 * Returns label for the y values vector.
	 * 
	 * @return Label for the y values vector.
	 */
	private String getYLabel() {

		// get combobox selection
		int index = combobox1_.getSelectedIndex();

		// get text of selected radiobutton
		if (index != 6) {
			if (radiobutton1_.isSelected())
				return radiobutton1_.getText();
			else if (radiobutton2_.isSelected())
				return radiobutton2_.getText();
			else if (radiobutton3_.isSelected())
				return radiobutton3_.getText();
			else if (radiobutton4_.isSelected())
				return radiobutton4_.getText();
			else if (radiobutton5_.isSelected())
				return radiobutton5_.getText();
			else if (radiobutton6_.isSelected())
				return radiobutton6_.getText();
		} else
			return "SVM";
		return null;
	}

	/**
	 * Returns label for the x values vector depending on the analysis type.
	 * 
	 * @param type
	 *            The type of analysis.
	 * @return The label for the x values vector.
	 */
	private String getXLabel(int type) {

		// linear dynamic analysis
		if (type == Analysis.linearTransient_)
			return "Time";

		// modal analysis
		else if (type == Analysis.modal_)
			return "Mode";
		return null;
	}

	/**
	 * Checks entered textfields.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkText() {

		// set error message
		String message = "Illegal values for step numbers!";

		// check for non-numeric or negative values
		try {

			// convert texts to integer values
			int val1 = (Integer) spinner1_.getValue();
			int val2 = (Integer) spinner2_.getValue();

			// check if negative
			if (val1 < 0 || val2 <= 0) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, message,
						"False data entry", 2);
				return false;
			}

			// check mutual suitability
			if (val2 <= val1) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, message,
						"False data entry", 2);
				return false;
			}

			// check if they are out of limit
			int limit = owner_.structure_.getNumberOfSteps();
			if (val1 >= limit || val2 >= limit) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, message,
						"False data entry", 2);
				return false;
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, message, "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * If the related checkbox is selected, sets default value to textfield and
	 * makes it editable. If the checkbox is deselected, clears textfield and
	 * makes it uneditable.
	 */
	public void itemStateChanged(ItemEvent event) {

		// get selected item
		int index = combobox1_.getSelectedIndex();

		// displacements
		if (index == 0) {
			radiobutton1_.setText("U1");
			radiobutton2_.setText("U2");
			radiobutton3_.setText("U3");
			radiobutton4_.setText("R1");
			radiobutton5_.setText("R2");
			radiobutton6_.setText("R3");
			radiobutton1_.setSelected(true);
			radiobutton1_.setEnabled(true);
			radiobutton2_.setEnabled(true);
			radiobutton3_.setEnabled(true);
			radiobutton4_.setEnabled(true);
			radiobutton5_.setEnabled(true);
			radiobutton6_.setEnabled(true);
		}

		// elastic strains
		else if (index == 1) {
			radiobutton1_.setText("E11");
			radiobutton2_.setText("E22");
			radiobutton3_.setText("E33");
			radiobutton4_.setText("E12");
			radiobutton5_.setText("E13");
			radiobutton6_.setText("E23");
			radiobutton1_.setSelected(true);
			radiobutton1_.setEnabled(true);
			radiobutton2_.setEnabled(true);
			radiobutton3_.setEnabled(true);
			radiobutton4_.setEnabled(true);
			radiobutton5_.setEnabled(true);
			radiobutton6_.setEnabled(true);
		}

		// stresses
		else if (index == 2) {
			radiobutton1_.setText("S11");
			radiobutton2_.setText("S22");
			radiobutton3_.setText("S33");
			radiobutton4_.setText("S12");
			radiobutton5_.setText("S13");
			radiobutton6_.setText("S23");
			radiobutton1_.setSelected(true);
			radiobutton1_.setEnabled(true);
			radiobutton2_.setEnabled(true);
			radiobutton3_.setEnabled(true);
			radiobutton4_.setEnabled(true);
			radiobutton5_.setEnabled(true);
			radiobutton6_.setEnabled(true);
		}

		// internal forces
		else if (index == 3) {
			radiobutton1_.setText("N1");
			radiobutton2_.setText("V2");
			radiobutton3_.setText("V3");
			radiobutton4_.setText("T1");
			radiobutton5_.setText("M2");
			radiobutton6_.setText("M3");
			radiobutton1_.setSelected(true);
			radiobutton1_.setEnabled(true);
			radiobutton2_.setEnabled(true);
			radiobutton3_.setEnabled(true);
			radiobutton4_.setEnabled(true);
			radiobutton5_.setEnabled(true);
			radiobutton6_.setEnabled(true);
		}

		// principle strains
		else if (index == 4) {
			radiobutton1_.setText("Emin");
			radiobutton2_.setText("Emid");
			radiobutton3_.setText("Emax");
			radiobutton1_.setSelected(true);
			radiobutton1_.setEnabled(true);
			radiobutton2_.setEnabled(true);
			radiobutton3_.setEnabled(true);
			radiobutton4_.setEnabled(false);
			radiobutton5_.setEnabled(false);
			radiobutton6_.setEnabled(false);
		}

		// principle stresses
		else if (index == 5) {
			radiobutton1_.setText("Smin");
			radiobutton2_.setText("Smid");
			radiobutton3_.setText("Smax");
			radiobutton1_.setSelected(true);
			radiobutton1_.setEnabled(true);
			radiobutton2_.setEnabled(true);
			radiobutton3_.setEnabled(true);
			radiobutton4_.setEnabled(false);
			radiobutton5_.setEnabled(false);
			radiobutton6_.setEnabled(false);
		}

		// mises stress
		else if (index == 6) {
			radiobutton1_.setEnabled(false);
			radiobutton2_.setEnabled(false);
			radiobutton3_.setEnabled(false);
			radiobutton4_.setEnabled(false);
			radiobutton5_.setEnabled(false);
			radiobutton6_.setEnabled(false);
		}
	}
}
