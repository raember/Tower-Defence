/*
 * Copyright (C) 2016 Raphael
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package base;

import java.awt.*;

/**
 * Specifies game object that can be drawn
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class DrawableObject extends GameObject {

    protected Point position;

    /**
     * Constructor of a drawable object
     * @param game game object for backreference
     */
    public DrawableObject(GameForm game) {
        super(game);
    }

    /**
     * Paint delegate for drawing the object
     * @param g Graphics object
     */
    public abstract void paint(Graphics2D g);
}
