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
 * Object representing an enemy
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class Enemy extends PositionableObject {

    /**
     * Radius within a bullet triggers damage
     */
    public double radiusOfVulnerability;
    /**
     * Health of the enemy
     */
    public int health;
    /**
     * Amount of wealth to be added to the players balance in case the enemy
     * gets destroyed
     */
    public int wealth;
    /**
     * Direction towards the enemy is driving
     */
    public Direction facingAngle;
    protected double speed;
    protected final Color colInterior = new Color(250, 80, 40, 150);
    protected final Color colBorder = new Color(250, 80, 40);
    private Point oldPoint;
    private Point newPoint;
    private double pos;

    /**
     * Constructor of an enemy
     * @param game game object for backreference
     */
    public Enemy(GameForm game) {
        super(game);
    }

    @Override
    public void update(double deltatime, double abstime) {
        if (health <= 0) {
            destroy();
            return;
        }
        GameMap theMap = Game.getMap();
        pos += deltatime * speed;
        boolean firstRun = false;
        if (oldPoint == null) {
            //first run
            oldPoint = position;
            newPoint = oldPoint;
            firstRun = true;
        }
        if (pos >= 1d || firstRun) {
            //new point reached or first run
            firstRun = false;
            if (theMap.getTile(newPoint).isEndTile()) {
                //reached end tile.
                Game.dealDamage();
                super.destroy();
                return;
            }
            oldPoint = newPoint;
            newPoint = theMap.calcNewWayPoint(this, oldPoint);
            pos = pos % 1d;
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
        position = (Point) oldPoint.clone();
    }

    @Override
    public Point getAbsPosition() {
        Point absPos = Game.getMap().TFMapCoordinateToMapCenter(position);
        switch (facingAngle) {
            case EAST:
                absPos.translate((int) (pos * Game.TILEWIDTH), 0);
                break;
            case NORTH:
                absPos.translate(0, (int) (-pos * Game.TILEWIDTH));
                break;
            case WEST:
                absPos.translate((int) (-pos * Game.TILEWIDTH), 0);
                break;
            case SOUTH:
                absPos.translate(0, (int) (pos * Game.TILEWIDTH));
                break;
        }
        return absPos;
    }

    @Override
    public void paint(Graphics2D g) {
        Point base = getAbsPosition();
        g.translate(base.x, base.y);
        g.rotate(-(double) facingAngle.getNewdegree() / 2 * Math.PI);
        paintEnemy(g);
        g.rotate((double) facingAngle.getNewdegree() / 2 * Math.PI);
        g.translate(-base.x, -base.y);
    }

    protected abstract void paintEnemy(Graphics2D g);

    @Override
    public void destroy() {
        Game.setBalance(Game.getBalance() + wealth);
        super.destroy();
    }
}
