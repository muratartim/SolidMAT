package dialogs.library;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JDialog; // import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;

import main.Commons; // import main.ImageHandler;

import section.*;

/**
 * Class for Section Properties menu.
 * 
 * @author Murat
 * 
 */
public class Section3 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_,
			textfield10_, textfield11_, textfield12_, textfield13_,
			textfield14_, textfield15_;

	protected Section2 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 * @param section
	 *            The section to be exposed.
	 */
	public Section3(Section2 owner, Section section) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Section Properties", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1_ = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Properties", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Cross section area :");
		JLabel label3 = new JLabel("Shear area in 2 direction :");
		JLabel label4 = new JLabel("Shear area in 3 direction :");
		JLabel label5 = new JLabel("Moment of inertia about 2 axis :");
		JLabel label6 = new JLabel("Moment of inertia about 3 axis :");
		JLabel label7 = new JLabel("Radius of gyration about 2 axis :");
		JLabel label8 = new JLabel("Radius of gyration about 3 axis :");
		JLabel label9 = new JLabel("Torsional constant :");
		JLabel label10 = new JLabel("Warping constant :");
		JLabel label11 = new JLabel("Polar moment of inertia :");
		JLabel label12 = new JLabel("Polar radius of gyration :");
		JLabel label13 = new JLabel("Location of centroid - Xc :");
		JLabel label14 = new JLabel("Location of centroid - Yc :");
		JLabel label15 = new JLabel("Distance to shear center - e :");

		// build text fields, set font and make them unedittable
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
		textfield14_ = new JTextField();
		textfield15_ = new JTextField();
		textfield1_.setEditable(false);
		textfield2_.setEditable(false);
		textfield2_.setEditable(false);
		textfield3_.setEditable(false);
		textfield4_.setEditable(false);
		textfield5_.setEditable(false);
		textfield6_.setEditable(false);
		textfield7_.setEditable(false);
		textfield8_.setEditable(false);
		textfield9_.setEditable(false);
		textfield10_.setEditable(false);
		textfield11_.setEditable(false);
		textfield12_.setEditable(false);
		textfield13_.setEditable(false);
		textfield14_.setEditable(false);
		textfield15_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(184, 20));
		textfield2_.setPreferredSize(new Dimension(100, 20));

		// build buttons and give font
		JButton button1 = new JButton("  OK  ");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, label4, 2, 0, 1, 1);
		Commons.addComponent(panel4, label11, 3, 0, 1, 1);
		Commons.addComponent(panel4, label5, 4, 0, 1, 1);
		Commons.addComponent(panel4, label6, 5, 0, 1, 1);
		Commons.addComponent(panel4, label12, 6, 0, 1, 1);
		Commons.addComponent(panel4, label7, 7, 0, 1, 1);
		Commons.addComponent(panel4, label8, 8, 0, 1, 1);
		Commons.addComponent(panel4, label9, 9, 0, 1, 1);
		Commons.addComponent(panel4, label13, 10, 0, 1, 1);
		Commons.addComponent(panel4, label14, 11, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel4, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel4, textfield11_, 3, 1, 1, 1);
		Commons.addComponent(panel4, textfield5_, 4, 1, 1, 1);
		Commons.addComponent(panel4, textfield6_, 5, 1, 1, 1);
		Commons.addComponent(panel4, textfield12_, 6, 1, 1, 1);
		Commons.addComponent(panel4, textfield7_, 7, 1, 1, 1);
		Commons.addComponent(panel4, textfield8_, 8, 1, 1, 1);
		Commons.addComponent(panel4, textfield9_, 9, 1, 1, 1);
		Commons.addComponent(panel4, textfield13_, 10, 1, 1, 1);
		Commons.addComponent(panel4, textfield14_, 11, 1, 1, 1);

		int type = section.getType();
		if (type == Section.tWChannel_ || type == Section.c_
				|| type == Section.hat_ || type == Section.iTwinChannel_
				|| type == Section.oTwinChannel_ || type == Section.eFI_
				|| type == Section.uFI_ || type == Section.tWZ_
				|| type == Section.hCSector_) {
			Commons.addComponent(panel4, label10, 12, 0, 1, 1);
			Commons.addComponent(panel4, textfield10_, 12, 1, 1, 1);
			Commons.addComponent(panel4, label15, 13, 0, 1, 1);
			Commons.addComponent(panel4, textfield15_, 13, 1, 1, 1);
		}

		// add sub-panels to main panels
		Commons.addComponent(panel1_, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1_, panel4, 1, 0, 1, 1);
		panel2.add(button1);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1_);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);

		// set texts
		setTexts(section);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets textfields according to the section passed to it.
	 * 
	 * @param section
	 *            The section to be displayed.
	 */
	private void setTexts(Section section) {
		textfield1_.setText(section.getName());
		textfield2_.setText(owner_.owner_.owner_.formatter_.format(section
				.getArea(0)));
		textfield3_.setText(owner_.owner_.owner_.formatter_.format(section
				.getShearAreaX2(0)));
		textfield4_.setText(owner_.owner_.owner_.formatter_.format(section
				.getShearAreaX3(0)));
		textfield11_.setText(owner_.owner_.owner_.formatter_.format(section
				.getInertiaX1(0)));
		textfield5_.setText(owner_.owner_.owner_.formatter_.format(section
				.getInertiaX2(0)));
		textfield6_.setText(owner_.owner_.owner_.formatter_.format(section
				.getInertiaX3(0)));
		textfield12_.setText(owner_.owner_.owner_.formatter_.format(section
				.getGyrationX1(0)));
		textfield7_.setText(owner_.owner_.owner_.formatter_.format(section
				.getGyrationX2(0)));
		textfield8_.setText(owner_.owner_.owner_.formatter_.format(section
				.getGyrationX3(0)));
		textfield9_.setText(owner_.owner_.owner_.formatter_.format(section
				.getTorsionalConstant(0)));
		textfield10_.setText(owner_.owner_.owner_.formatter_.format(section
				.getWarpingConstant(0)));
		textfield13_.setText(owner_.owner_.owner_.formatter_.format(section
				.getCentroid(0)));
		textfield14_.setText(owner_.owner_.owner_.formatter_.format(section
				.getCentroid(1)));
		textfield15_.setText(owner_.owner_.owner_.formatter_.format(section
				.getShearCenter()));
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
