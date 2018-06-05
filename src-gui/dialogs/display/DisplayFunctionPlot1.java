package dialogs.display;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import math.Function;
import math.GraphPlot2D;
import main.Commons;

/**
 * Class for Function Plot Display menu.
 * 
 * @author Murat
 * 
 */
public class DisplayFunctionPlot1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private GridBagLayout layout1_;

	private GridBagConstraints constraints1_;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_;

	private JRadioButton radiobutton1_, radiobutton2_;

	private JList list1_;

	private DefaultListModel listModel1_;

	private JButton button1_, button2_, button3_;

	/** Plotter of this dialog. */
	protected GraphPlot2D graph_ = new GraphPlot2D();

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
	public DisplayFunctionPlot1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Function Plot", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		// build sub-panels
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();

		// build layout managers and set layout managers to panels
		layout1_ = new GridBagLayout();
		FlowLayout layout2 = new FlowLayout();
		panel1.setLayout(layout1_);
		panel2.setLayout(layout2);
		panel3.setLayout(layout1_);
		panel4.setLayout(layout1_);

		// set border to panels
		panel3.setBorder(BorderFactory.createTitledBorder("Plot Data"));
		panel4.setBorder(BorderFactory.createTitledBorder("Library"));

		// build gridbag constraints, make components extend in both directions
		// and determine gaps between components
		constraints1_ = new GridBagConstraints();
		constraints1_.insets = new Insets(5, 5, 5, 5);
		layout2.setAlignment(FlowLayout.CENTER);
		layout2.setHgap(5);
		layout2.setVgap(5);

		// build labels
		JLabel label1 = new JLabel("Number of increments :");
		JLabel label2 = new JLabel("Minimum abscissa :");

		// build text fields and set font
		textfield1_ = new JTextField("100");
		textfield2_ = new JTextField("0.0");
		textfield3_ = new JTextField("0.01");
		textfield4_ = new JTextField("1.0");
		textfield4_.setEnabled(false);
		textfield1_.setPreferredSize(new Dimension(100, 20));

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Increment size :", true);
		radiobutton2_ = new JRadioButton("Maximum abscissa :", false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(10);
		list1_.setFixedCellWidth(150);
		list1_.setFixedCellHeight(15);

		// build scroll panes and add lists to them
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");
		button3_ = new JButton("Options");

		// add components to sub-panels
		addComponent(panel3, label1, 0, 0, 1, 1);
		addComponent(panel3, label2, 1, 0, 1, 1);
		addComponent(panel3, radiobutton1_, 2, 0, 1, 1);
		addComponent(panel3, radiobutton2_, 3, 0, 1, 1);
		addComponent(panel3, textfield1_, 0, 1, 1, 1);
		addComponent(panel3, textfield2_, 1, 1, 1, 1);
		addComponent(panel3, textfield3_, 2, 1, 1, 1);
		addComponent(panel3, textfield4_, 3, 1, 1, 1);
		addComponent(panel3, button3_, 4, 0, 2, 1);
		addComponent(panel4, scrollpane1, 0, 0, 1, 1);

		// add sub-panels to main panels
		addComponent(panel1, panel3, 0, 0, 1, 1);
		addComponent(panel1, panel4, 0, 1, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		radiobutton1_.addItemListener(this);
		radiobutton2_.addItemListener(this);

		// call initialize
		initialize();

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets gridbag layout constraints and adds components to layout.
	 * 
	 * @param component
	 *            Component to be added into layout.
	 * @param row
	 *            Y coordinate of the upper-left corner of the component.
	 * @param column
	 *            X coordinate of the upper-left corner of the component.
	 * @param width
	 *            Cell number captured by the component's width.
	 * @param height
	 *            Cell number captured by the component's height.
	 */
	private void addComponent(JPanel panel, Component component, int row,
			int column, int width, int height) {

		// set gridx and gridy
		constraints1_.gridx = column;
		constraints1_.gridy = row;

		// set gridwidth and gridheight
		constraints1_.gridwidth = width;
		constraints1_.gridheight = height;

		if (component.equals(button3_)) {
			constraints1_.fill = GridBagConstraints.NONE;
			constraints1_.anchor = GridBagConstraints.WEST;
		} else
			constraints1_.fill = GridBagConstraints.BOTH;

		// set constraints and add component to panel1
		layout1_.setConstraints(component, constraints1_);
		panel.add(component);
	}

	/**
	 * Sets the input data vector to temporary vector. Copies names to list.
	 */
	private void initialize() {

		// set the input data vector to list
		Vector<Function> object = owner_.inputData_.getFunction();
		for (int i = 0; i < object.size(); i++)
			listModel1_.addElement(object.get(i).getName());
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

		// options button clicked
		else if (e.getSource().equals(button3_)) {

			// create and set dialog visible
			DisplayFunctionPlot2 dialog = new DisplayFunctionPlot2(this);
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
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// radiobutton1 event
		if (event.getSource().equals(radiobutton1_)) {

			// set textfields
			textfield3_.setEnabled(true);
			textfield4_.setEnabled(false);
		}

		// radiobutton2 event
		else if (event.getSource().equals(radiobutton2_)) {

			// set textfields
			textfield3_.setEnabled(false);
			textfield4_.setEnabled(true);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check stepping numbers
		progressor_.setStatusMessage("Checking data...");
		if (checkText()) {

			// get selected function
			int index = list1_.getSelectedIndex();
			Function f = owner_.inputData_.getFunction().get(index);

			// get number of increments
			int numInc = Integer.parseInt(textfield1_.getText());

			// get minimum abscissa
			double minAbs = Double.parseDouble(textfield2_.getText());

			// get increment size
			double incSize = 0.01;
			if (textfield3_.isEnabled())
				incSize = Double.parseDouble(textfield3_.getText());
			else if (textfield4_.isEnabled()) {
				double maxAbs = Double.parseDouble(textfield4_.getText());
				incSize = (maxAbs - minAbs) / numInc;
			}

			// check for illegal values
			try {

				// initialize values arrays
				double[] xVal = new double[numInc + 1];
				double[] yVal = new double[numInc + 1];
				xVal[0] = minAbs;
				yVal[0] = f.getValue(xVal[0]);

				// store values of function
				progressor_.setStatusMessage("Computing function...");
				for (int i = 1; i < numInc + 1; i++) {
					xVal[i] = xVal[i - 1] + incSize;
					yVal[i] = f.getValue(xVal[i]);
				}

				// set x and y values to plotter
				graph_.setValues(xVal, yVal);

				// set labels to plotter
				graph_.setLabels("x", "F(x)");

				// plot
				progressor_.setStatusMessage("Plotting...");
				graph_.plot(owner_.viewer_);

				// close progressor
				progressor_.close();

				// set dialog unvisible
				setVisible(false);
			}

			// illegal values entered
			catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						excep.getLocalizedMessage(), "False data entry", 2);
			}
		}
	}

	/**
	 * Checks entered textfields.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkText() {

		// function selected
		if (list1_.isSelectionEmpty() == false) {

			// check for non-numeric or negative values
			try {

				// convert texts to integer values
				int val1 = Integer.parseInt(textfield1_.getText());
				double val2 = Double.parseDouble(textfield2_.getText());
				double val3 = 0.01;
				if (textfield3_.isEnabled())
					val3 = Double.parseDouble(textfield3_.getText());
				else
					val3 = Double.parseDouble(textfield4_.getText());

				// check number of increments
				if (val1 < 10) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this,
							"Number of increments should be at least 10!",
							"False data entry", 2);
					return false;
				}

				// check increment size
				if (textfield3_.isEnabled() && val3 <= 0.0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this,
							"Increment size should be greater than zero!",
							"False data entry", 2);
					return false;
				}

				// check maximum abscissa
				if (textfield4_.isEnabled() && val3 <= val2) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane
							.showMessageDialog(
									this,
									"Maximum abscissa should be greater than minimum abscissa!",
									"False data entry", 2);
					return false;
				}
			}

			// illegal values entered
			catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						"Illegal values for plot data!", "False data entry", 2);
				return false;
			}
		}

		// no function selected
		else {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, "No function selected!",
					"False data entry", 2);
			return false;
		}

		// entered data is correct
		return true;
	}
}
