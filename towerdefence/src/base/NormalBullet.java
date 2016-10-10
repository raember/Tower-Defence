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

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Raphael
 * @date 10.10.2016
 */
public class NormalBullet extends Bullet {

    public NormalBullet(GameForm Game) {
        super(Game);
        rangeOfImpact = 15d;
        damage = 20;
        speed = 100d;
    }

    @Override
    protected void paintBullet(Graphics2D g) {
        g.setColor(Color.red);
        g.fillOval(0, 0, 5, 5);
    }

}
