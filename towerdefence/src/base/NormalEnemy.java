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

import java.awt.BasicStroke;
import java.awt.Graphics2D;

/**
 * Represents a normal enemy
 * @author Raphael
 * @date 08.10.2016
 */
public class NormalEnemy extends Enemy {

    /**
     * Constructor of a normal enemy
     * @param game game object for backreference
     */
    public NormalEnemy(GameForm game) {
        super(game);
        radiusOfVulnerability = 0.9d;
        health = 50;
        wealth = 20;
        facingAngle = Direction.NORTH;
        speed = 1.5d;
    }

    @Override
    protected void paintEnemy(Graphics2D g) {
        int width = Game.TILEWIDTH;
        g.setColor(colInterior);
        int[] xpoints = new int[4];
        xpoints[0] = -width / 6;
        xpoints[1] = -width / 4;
        xpoints[2] = width / 3;
        xpoints[3] = -width / 4;
        int[] ypoints = new int[4];
        ypoints[0] = 0;
        ypoints[1] = -width / 4;
        ypoints[2] = 0;
        ypoints[3] = width / 4;
        g.fillPolygon(xpoints, ypoints, 4);
        g.setColor(colBorder);
        g.setStroke(new BasicStroke(3));
        g.drawPolygon(xpoints, ypoints, 4);
        g.setStroke(new BasicStroke(1));
    }

}
