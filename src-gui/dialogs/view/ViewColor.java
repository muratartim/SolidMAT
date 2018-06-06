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
package dialogs.view;

import java.awt.Color;

import javax.swing.JColorChooser;

import main.SolidMAT;


/**
 * Class for Set Background Color (View) menu.
 * 
 * @author Murat
 * 
 */
public class ViewColor {

	/**
	 * Shows color chooser for setting up the background color of viewer.
	 * 
	 * @param owner
	 *            The owner dialog of color chooser.
	 */
	public static void show(SolidMAT owner) {

		// get background color
		Double red = owner.viewer_.getCanvas().GetRenderer().GetBackground()[0] * 255.0;
		Double green = owner.viewer_.getCanvas().GetRenderer().GetBackground()[1] * 255.0;
		Double blue = owner.viewer_.getCanvas().GetRenderer().GetBackground()[2] * 255.0;
		Color c = new Color(red.intValue(), green.intValue(), blue.intValue());

		// show color chooser and get selected color
		c = JColorChooser.showDialog(owner.viewer_, "Choose Background Color",
				c);

		// check if any selected
		if (c != null) {

			// get r,g,b values of color
			double r = c.getRed();
			double g = c.getGreen();
			double b = c.getBlue();

			// set to viewer
			owner.viewer_.getCanvas().GetRenderer().SetBackground(r / 255.0,
					g / 255.0, b / 255.0);
		}
	}
}
