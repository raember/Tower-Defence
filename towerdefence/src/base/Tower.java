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

import base.ListOf;
import base.ListOf;
import java.awt.Point;

/**
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class Tower extends DrawableObject {

    protected double facingangle = 0d;
    protected double angularspeed = 0d;
    protected double maxangularspeed = 0d;
    protected double range;
    protected int level;
    public int health;

    public Tower(GameForm game) {
        super(game);
    }

    protected void handleEnemies() {
        ListOf<Enemy> nearestEnemies = Game.findNearestEnemies(center, range);
        if (!nearestEnemies.any()) {
            angularspeed = 0d;
            return;
        }
        Enemy nearestEnemy = nearestEnemies.first();
        if (!isFacing(nearestEnemy.center)) {
            angularspeed = maxangularspeed;
            return;
        }
        shootEnemy(nearestEnemy);
    }

    protected abstract void initiate();

    protected boolean isFacing(Point p) {
        return Math.abs(Math.atan2(p.y - center.y,
                p.x - center.x) - facingangle)
                <= Double.MIN_NORMAL;
    }

    protected void shootEnemy(Enemy enemy) {
        //TODO: Implement method to shoot an enemy.
        Bullet tempBullet = createBullet();
        tempBullet.center=center;
        tempBullet.facingangle=facingangle;
        Game.listBullets.add(tempBullet);
    }

    @Override
    public void update(double deltatime, double abstime) {
        facingangle *= deltatime * angularspeed;
        if (health <= 0) {
            destroy();
        }
        handleEnemies();
    }

    protected void destroy() {
        Game.listTowers.remove(this);
    }
    
    protected abstract Bullet createBullet();
}
