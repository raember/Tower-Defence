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
package base.Game;

import java.awt.*;

/**
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class DrawableObject extends GameObject {

    protected Point center;
    
    public DrawableObject(Game theGame) {
        super(theGame);
    }

    public abstract void paint(Graphics2D g);
}