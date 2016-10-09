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
import java.awt.geom.*;

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
    private double pos;

    public Enemy(GameForm game) {
        super(game);
    }

    @Override
    public void update(double deltatime, double abstime) {
        GameMap theMap = Game.getMap();
        pos += deltatime * speed;
        boolean firstRun = false;
        if (oldPoint == null) {
            //first run
            oldPoint = center;
            newPoint = oldPoint;
            firstRun = true;
        }
        if (pos >= Game.TILEWIDTH || firstRun) {
            //new point reached or first run
            firstRun = false;
            oldPoint = newPoint;
            newPoint = theMap.transformFromMapCoordinateToMapCenter(
                    theMap.calcNewPoint(this, 
                            theMap.transformFromScreenToMap(oldPoint)));
            pos = pos % Game.TILEWIDTH;
            if (oldPoint.x < newPoint.x) {
                facingAngle = 0;
            } else if (oldPoint.y < newPoint.y) {
                facingAngle = 1;
            } else if (oldPoint.x > newPoint.x) {
                facingAngle = 2;
            } else if (oldPoint.y > newPoint.y) {
                facingAngle = 3;
            }
        }
        center = (Point) oldPoint.clone();
        switch (facingAngle) {
            case 0:
                center.translate((int) pos, 0);
                break;
            case 1:
                center.translate(0, (int) -pos);
                break;
            case 2:
                center.translate((int) -pos, 0);
                break;
            case 3:
                center.translate(0, (int) pos);
                break;
        }
    }

    @Override
    public void paint(Graphics2D g) {
        g.translate(center.x, center.y);
        g.rotate(-facingAngle / 2 * Math.PI);
        paintEnemy(g);
        g.rotate(facingAngle / 2 * Math.PI);
        g.translate(-center.x, -center.y);
    }

    protected abstract void paintEnemy(Graphics2D g);
}
