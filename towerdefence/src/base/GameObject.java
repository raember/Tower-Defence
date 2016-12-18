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

import java.awt.Graphics;

/**
 * Specifies a game object
 * @author Raphael
 * @date 21.09.2016
 */
public abstract class GameObject {

    protected GameForm Game;

    /**
     * Constructor of a game object
     * @param game Game object for backreference
     */
    public GameObject(GameForm game) {
        this.Game = game;
    }

    /**
     * Update method for calculating the actions between the frames
     * @param deltatime time difference between last call and current call
     * @param abstime   absolute time
     */
    public abstract void update(double deltatime, double abstime);
}
