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
 * Represents a bullet
 * @author Raphael
 * @date 26.09.2016
 */
public abstract class Bullet extends PositionableObject {

    /**
     * Specifies how much damage the bullet inflicts an enemy when hit
     */
    public int damage;
    /**
     * The angle at which the bullet has been shot
     */
    public double facingAngle;
    /**
     * The speed at which the bullet travels
     */
    public double speed;
    private Point start;
    private double pos = 0d;

    /**
     * Constructor of a bullet
     * @param game game object for backreference
     */
    public Bullet(GameForm Game, Point start) {
        super(Game);
        this.start = start;
    }

    @Override
    public void update(double deltatime, double abstime) {
        pos += deltatime * speed;
        Rectangle theMap = Game.getMap().getRectangle();
        Point pnt = Game.getMap().TLMapToAbs(getAbsPosition());
        if (!theMap.contains(pnt)) {
            destroy();
            return;
        }

        for (Enemy e : Game.getEnemies()) {
            if (e.getAbsPosition().distance(getAbsPosition())
                    <= e.radiusOfVulnerability * Game.TILEWIDTH) {
                encounterEnemy(e);
            }
        }
    }

    /**
     * Specifies behaviour when hitting an enemy
     * @param e
     */
    public abstract void encounterEnemy(Enemy e);

    @Override
    public void paint(Graphics2D g) {
        double cos = Math.cos(facingAngle);
        double sin = Math.sin(facingAngle);
        Point base = (Point) position.clone();
        base = Game.getMap().TFMapCoordinateToMapCenter(base);
        base.translate((int) (pos * cos * Game.TILEWIDTH), (int) (pos * -sin
                * Game.TILEWIDTH));
        g.translate(base.x, base.y);
        g.rotate(-facingAngle, 0, 0);
        paintBullet(g);
        g.rotate(facingAngle, 0, 0);
        g.translate(-base.x, -base.y);
    }

    @Override
    public Point getAbsPosition() {
        double cos = Math.cos(facingAngle);
        double sin = Math.sin(facingAngle);
        Point pnt = Game.getMap().TFMapCoordinateToMapCenter(position);
        pnt.translate((int) (pos * cos * Game.TILEWIDTH), (int) (pos * -sin
                * Game.TILEWIDTH));
        return pnt;
    }

    protected abstract void paintBullet(Graphics2D g);

    protected void damageEnemy(Enemy enemy) {
        enemy.health -= damage;
    }
}
