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
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Raphael
 * @date 21.09.2016
 */
public class Game {

    public ListOf<Enemy> listEnemies = new ListOf();
    public ListOf<Tower> listTowers = new ListOf();
    public ListOf<Bullet> listBullets = new ListOf();

    public boolean loadMap(File csv) {
        if (!csv.exists()) {
            return false;
        }
        String content = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(csv.getAbsolutePath()));
            content = new String(encoded, Charset.defaultCharset());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public void render(Graphics2D g) {
        listEnemies.forEach(e -> e.paint(g));
        listTowers.forEach(t -> t.paint(g));
        listBullets.forEach(b -> b.paint(g));
    }

    public void update(double deltatime, double abstime) {
        listEnemies.forEach(e -> e.update(deltatime, abstime));
        listTowers.forEach(t -> t.update(deltatime, abstime));
        listBullets.forEach(b -> b.update(deltatime, abstime));
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
