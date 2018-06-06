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
package main;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.JDialog;

/**
 * Class for performing common operations for gui.
 * 
 * @author Murat Artim
 * 
 */
public class Commons {

	/** Static variable for the layout manager of panel. */
	public static final int flow_ = 0, gridbag_ = 1;

	/**
	 * Sets visualization for dialog.
	 * 
	 * @param dialog
	 *            The dialog to be visualized.
	 */
	public static void visualize(JDialog dialog) {

		// pack dialog
		dialog.pack();

		// do not allow resizing
		dialog.setResizable(false);

		// set default close operation as hide
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		// set location to center of screen
		dialog.setLocationRelativeTo(null);
	}

	/**
	 * Creates and returns panel depending on the given parameters.
	 * 
	 * @param borderTitle
	 *            The title to be set for the border of the panel. If null value
	 *            is given, no border will be assigned.
	 * @param layoutType
	 *            The layout type of panel.
	 * @return The created panel.
	 */
	public static JPanel getPanel(String borderTitle, int layoutType) {

		// build panel
		JPanel panel = new JPanel();

		// set border to panel
		if (borderTitle != null)
			panel.setBorder(BorderFactory.createTitledBorder(borderTitle));

		// create flow layout and set
		if (layoutType == Commons.flow_) {

			// create flow layout
			FlowLayout layout = new FlowLayout();

			// set specifications
			layout.setAlignment(FlowLayout.CENTER);
			layout.setHgap(5);
			layout.setVgap(5);

			// set layout to panel
			panel.setLayout(layout);
		}

		else if (layoutType == Commons.gridbag_) {

			// create gridbag layout
			GridBagLayout layout = new GridBagLayout();

			// set layout to panel
			panel.setLayout(layout);
		}

		// return panel
		return panel;
	}

	/**
	 * Sets gridbag layout constraints and adds components to panel.
	 * 
	 * @param panel
	 *            The panel for the components to be added.
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
	public static void addComponent(JPanel panel, Component component, int row,
			int column, int width, int height) {

		// gridbag layout
		if (panel.getLayout() instanceof GridBagLayout) {

			// cast to gridbag layout
			GridBagLayout layout = (GridBagLayout) panel.getLayout();

			// create gridbag constraints
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.insets = new Insets(5, 5, 5, 5);

			// set gridx and gridy
			constraints.gridx = column;
			constraints.gridy = row;

			// set gridwidth and gridheight
			constraints.gridwidth = width;
			constraints.gridheight = height;

			// set constraints to panel
			layout.setConstraints(component, constraints);
		}

		// add component to panel
		panel.add(component);
	}
}
