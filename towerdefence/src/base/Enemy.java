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
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class Enemy extends DrawableObject {

    public double radiusOfVulnerability;
    public int health;
    public Direction facingAngle;
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
            if (theMap.getTile(theMap.transformFromScreenToMap(newPoint)).isEndTile()) {
                //reached end tile.
                //TODO: Deal damage to the player.
                Game.dealDamage();
                destroy();
                return;
            }
            oldPoint = newPoint;
            newPoint = theMap.transformFromMapCoordinateToMapCenter(
                    theMap.calcNewPoint(this,
                            theMap.transformFromScreenToMap(oldPoint)));
            pos = pos % Game.TILEWIDTH;
            if (oldPoint.x < newPoint.x) {
                facingAngle = Direction.EAST;
            } else if (oldPoint.y > newPoint.y) {
                facingAngle = Direction.NORTH;
            } else if (oldPoint.x > newPoint.x) {
                facingAngle = Direction.WEST;
            } else if (oldPoint.y < newPoint.y) {
                facingAngle = Direction.SOUTH;
            }
        }
        center = (Point) oldPoint.clone();
        switch (facingAngle) {
            case EAST:
                center.translate((int) pos, 0);
                break;
            case NORTH:
                center.translate(0, (int) -pos);
                break;
            case WEST:
                center.translate((int) -pos, 0);
                break;
            case SOUTH:
                center.translate(0, (int) pos);
                break;
        }
    }

    @Override
    public void paint(Graphics2D g) {
        g.translate(center.x, center.y);
        g.rotate(-(double) facingAngle.getNewdegree() / 2 * Math.PI);
        paintEnemy(g);
        g.rotate((double) facingAngle.getNewdegree() / 2 * Math.PI);
        g.translate(-center.x, -center.y);
    }

    protected abstract void paintEnemy(Graphics2D g);

    private void destroy() {
        health = 0;
    }
}
