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

import java.awt.Graphics;
import java.awt.Graphics2D;
import javafx.scene.paint.Color;

/**
 * @author Raphael
 * @date 21.09.2016
 */
public class Player extends CollidableObject {

    private double angle;

    public void Face(int x, int y) {
        angle = Math.atan2(X - x, Y - y);
    }

    @Override
    public boolean CollidesWith(CollidableObject obj) {
        return false;
    }

    @Override
    public void CheckCollision(ListOf<CollidableObject> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Draw(Graphics g) {
        g.setColor(java.awt.Color.white);
        Graphics2D g2 = (Graphics2D) g;
        g.translate(X, Y);
        g2.rotate(-angle);
        g.fillOval(-10, -10, 20, 20);
        g.setColor(java.awt.Color.red);
        g.fillOval(7, -5, 10, 10);
        g.setColor(java.awt.Color.green);
        g.fillOval(-17, -5, 10, 10);
    }

}
