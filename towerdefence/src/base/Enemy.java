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

import java.awt.Color;
import java.awt.Point;

/**
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class Enemy extends DrawableObject {

    public double radiusOfVulnerability;
    public int health;
    public int facingAngle;
    protected double speed;
    protected final Color colInterior = new Color(250, 80, 40, 150);
    protected final Color colBorder = new Color(250, 80, 40);
    private Point oldPoint;
    private Point newPoint;

    public Enemy(GameForm game) {
        super(game);
    }

    @Override
    public void update(double deltatime, double abstime) {
        GameMap theMap = Game.getMap();
        if (oldPoint == null) {
            oldPoint = theMap.getMapCoordinate(center);
            newPoint = theMap.calcNewPoint(this, oldPoint);
        }
    }

}
