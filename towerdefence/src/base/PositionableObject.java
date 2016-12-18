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

import java.awt.Point;

/**
 * Object which have a position within the game tile map
 * @author Raphael
 * @date 18.12.2016
 */
public abstract class PositionableObject extends DrawableObject {

    protected boolean isDestroyed;

    /**
     * Constructor of a positionable object
     * @param game game object for backreference
     */
    public PositionableObject(GameForm game) {
        super(game);
    }

    /**
     * Calculated the exact position on the map in absolute coordinates
     * @return Object position
     */
    public Point getAbsPosition() {
        return Game.getMap().TFMapCoordinateToMapCenter(position);
    }

    /**
     * Specifies whether the object has been destroyed and is to be removed
     * @return true if the object will be removed, else false
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Destroys the object and marks it to be disposed of
     */
    protected void destroy() {
        isDestroyed = true;
    }
}
