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
package base.Game;

/**
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class Bullet extends DrawableObject {

    public double rangeOfImpact;
    protected int damage;

    public Bullet(Game theGame) {
        super(theGame);
    }

    @Override
    public void update(double deltatime, double abstime) {
        for (Enemy tempEnemy : theGame.listEnemies.where(e
                -> e.center.distance(center)
                <= rangeOfImpact + e.radiusOfVulnerability)) {
            damageEnemy(tempEnemy);
        }
    }

    protected void damageEnemy(Enemy enemy) {
        enemy.health -= damage;
    }
}
