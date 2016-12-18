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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Represents a tower object
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class Tower extends PositionableObject {

    protected double facingAngle;
    protected double desiredAngle;
    protected double angularSpeed;
    protected double maxAngularSpeed;
    protected double range;
    protected double shootingCooldown;
    private double lastShot;
    protected int level = 0;
    protected ListOf<Integer> costs = new ListOf();
    protected Color colBase;
    protected Color colBaseBorder;
    protected Color colHead;
    protected Color colHeadBorder;

    /**
     * Constructor of a tower
     * @param game game object for backreference
     */
    public Tower(GameForm game) {
        super(game);
    }

    protected void handleEnemies(double deltatime, double abstime) {
        Enemy nearestEnemy = findNearestEnemies();
        if (nearestEnemy == null) {
            angularSpeed = 0d;
            return;
        }
        if (isFacing(nearestEnemy) && (abstime - lastShot) >= shootingCooldown) {
            lastShot = abstime;
            shootEnemy(nearestEnemy);
        }
        face(nearestEnemy);
    }

    protected Enemy findNearestEnemies() {
        Enemy nearestEnemy = null;
        double maxDist = range;
        for (Enemy e : Game.getEnemies()) {
            double tempDistance = getAbsPosition().distance(e.getAbsPosition())
                    / Game.TILEWIDTH;
            if (maxDist > tempDistance) {
                maxDist = tempDistance;
                nearestEnemy = e;
            }
        }
        return nearestEnemy;
    }

    /**
     * Calculated the exact position on the map in absolute coordinates
     * @return Object position
     */
    public Point getAbsPosition() {
        return Game.getMap().TFMapCoordinateToMapCenter(position);
    }

    protected boolean isFacing(Enemy e) {
        return Math.abs(getAngleTo(e) - facingAngle) <= 0.05d;
    }

    private double getAngleTo(Enemy e) {
        Point pEnemy = e.getAbsPosition();
        Point pTower = getAbsPosition();
        return Math.atan2(pTower.y - pEnemy.y, pEnemy.x - pTower.x);
    }

    private double getRemainingAngle() {
        double angle = desiredAngle - facingAngle;
        if (angle >= Math.PI) {
            angle = angle - 2 * Math.PI;
        }
        return angle;
    }

    /**
     * Gets the position of the object in map coordinates
     * @return Object position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets the position of the object in map coordinates
     * @param position Object position
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Gets the costs for building/upgrading the tower
     * @return costs
     */
    public int getCosts() {
        return costs.get(level);
    }

    protected void face(Enemy e) {
        desiredAngle = getAngleTo(e);
        double deltaPhi = getRemainingAngle();
        angularSpeed = Math.signum(deltaPhi) * maxAngularSpeed;
    }

    protected void shootEnemy(Enemy enemy) {
        Bullet tempBullet = createBullet();
        tempBullet.position = position;
        tempBullet.facingAngle = facingAngle;
        Game.getBullets().add(tempBullet);
    }

    @Override
    public void update(double deltatime, double abstime) {
        double deltaAngle = deltatime * angularSpeed;
        //Adjust the angle if deltaAngle would exceed the desired one
        if (Math.abs(desiredAngle - facingAngle) <= deltaAngle) {
            facingAngle = desiredAngle;
        } else {
            facingAngle += deltaAngle;
        }
        facingAngle = ((facingAngle - Math.PI) % (2 * Math.PI)) + Math.PI;
        handleEnemies(deltatime, abstime);
    }

    @Override
    public void paint(Graphics2D g) {
        int width = Game.TILEWIDTH;
        Point base = Game.getMap().TFMapCoordinateToMapCenter(position);
        g.translate(base.x, base.y);
        paintBase(g, width);
        g.rotate(-facingAngle, 0, 0);
        paintHead(g, width);
        g.rotate(facingAngle, 0, 0);
        g.translate(-base.x, -base.y);
    }

    protected abstract void paintBase(Graphics2D g, int width);

    protected abstract void paintHead(Graphics2D g, int width);

    /**
     * Specifies how a levelup alters the tower
     * @return true, if upgrade succeeds, else false
     */
    public abstract boolean levelUp();

    protected abstract Bullet createBullet();
}
