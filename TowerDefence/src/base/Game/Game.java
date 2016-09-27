package base.Game;

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
import base.Game.Tower;
import base.Game.GameObject;
import base.Game.Enemy;
import base.ListOf;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * @author Raphael
 * @date 21.09.2016
 */
public class Game {

    public ListOf<GameObject> listGO = new ListOf();
    public ListOf<Enemy> listEnemies = new ListOf();
    public ListOf<Tower> listTowers = new ListOf();
    public ListOf<GameObject> listBullets = new ListOf();

    public void render(Graphics2D g) {

    }

    public ListOf<Enemy> findNearestEnemies(Point from, double range) {
        ListOf<Enemy> nearestEnemy = new ListOf();
        double distance = range;
        for (Enemy e : listEnemies) {
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
}
