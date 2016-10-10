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
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class Tower extends DrawableObject {

    protected double facingAngle;
    protected double desiredAngle;
    protected double angularSpeed;
    protected double maxAngularSpeed;
    protected double range;
    protected int level = 1;
    public int health;
    public int cost;
    protected Color colBase;
    protected Color colBaseBorder;
    protected Color colHead;
    protected Color colHeadBorder;
    protected Enemy lastEnemy;

    public Tower(GameForm game) {
        super(game);
    }

    protected void handleEnemies() {
        ListOf<Enemy> nearestEnemies = findNearestEnemies(center, range);
        if (!nearestEnemies.any()) {
            angularSpeed = 0d;
            return;
        }
        if (lastEnemy == null || !nearestEnemies.contains(lastEnemy)) {
            lastEnemy = nearestEnemies.first();
        }
        if (isFacing(lastEnemy.center)) {
//            shootEnemy(lastEnemy);
        } else {
            angularSpeed = face(lastEnemy.center);
        }
    }

    protected ListOf<Enemy> findNearestEnemies(Point from, double range) {
        ListOf<Enemy> nearestEnemy = new ListOf();
        double distance = range;
        for (Enemy e : Game.getEnemies()) {
            double tempDistance = from.distance(e.center);
            if (!nearestEnemy.any() || tempDistance < distance) {
                nearestEnemy.add(e);
                distance = tempDistance;
            }
        }
        return nearestEnemy.sortAll((Enemy e1, Enemy e2)
                -> Double.compare(e1.center.distance(from),
                        e2.center.distance(from)));
    }

    protected boolean isFacing(Point p) {
        return Math.abs(Math.atan2(p.y - center.y,
                p.x - center.x) - facingAngle)
                <= Double.MIN_NORMAL;
    }

    protected double face(Point p) {
        double desiredAngle = Math.atan2(center.y - p.y, p.x - center.x);
        return Math.signum(desiredAngle - facingAngle) * maxAngularSpeed;
    }

    protected void shootEnemy(Enemy enemy) {
        Bullet tempBullet = createBullet();
        tempBullet.center = center;
        tempBullet.facingangle = facingAngle;
        Game.getBullets().add(tempBullet);
    }

    @Override
    public void update(double deltatime, double abstime) {
        if (Math.abs(desiredAngle - facingAngle) > Double.MIN_NORMAL) {
            angularSpeed = maxAngularSpeed;
        }
        double deltaAngle = deltatime * angularSpeed;
        if (Math.abs(desiredAngle - facingAngle) <= deltaAngle) {
            facingAngle = desiredAngle;
            angularSpeed = 0d;
            System.out.println(desiredAngle);
        } else {
            facingAngle += deltaAngle;
        }
        if (health <= 0) {
            destroy();
        }
        handleEnemies();
    }

    @Override
    public void paint(Graphics2D g) {
        int width = Game.TILEWIDTH;
        g.translate(center.x, center.y);
        paintBase(g, width);
        g.rotate(-facingAngle, 0, 0);
        paintHead(g, width);
        g.rotate(facingAngle, 0, 0);
        g.translate(-center.x, -center.y);
    }

    private void paintBase(Graphics2D g, int width) {
        g.setColor(Game.colWallTile);
        g.fillRect(-width / 2, -width / 2, width, width);
        g.setColor(colBase);
        g.fillRect(-width / 2, -width / 2, width, width);
        g.setColor(colBaseBorder);
        g.setStroke(new BasicStroke(2));
        g.drawRect(-width / 2, -width / 2, width, width);
        g.setStroke(new BasicStroke(1));
    }

    protected abstract void paintHead(Graphics2D g, int width);

    protected void destroy() {
        Game.getTowers().remove(this);
    }

    public abstract boolean levelUp();

    protected abstract Bullet createBullet();
}
