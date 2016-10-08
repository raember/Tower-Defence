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
 * @author Raphael
 * @date 02.10.2016
 */
public class NormalTower extends Tower {

    public NormalTower(GameForm game) {
        super(game);
        cost = 100;
        health = 100;
        level = 1;
        range = 300d;
        maxAngularSpeed = 0.8;
        colBase = new Color(0, 120, 255, 150);
        colBaseBorder = new Color(0, 120, 255, 150);
        colHead = colBase;
        colHeadBorder = colBaseBorder;
    }

    @Override
    protected void paintHead(Graphics2D g, int width) {
        g.setColor(colHead);
        //frame
        g.setStroke(new BasicStroke(3));
        g.fillRect(-width / 4, -width / 4, width / 2, width / 2); //center square
        g.fillRect(width / 4, -width / 8, width / 2, width / 4); //cannon
        g.setColor(colHeadBorder);
        //interior
        g.setStroke(new BasicStroke(1));
        g.drawRect(-width / 4, -width / 4, width / 2, width / 2); //center square
        g.drawRect(width / 4, -width / 8, width / 2, width / 4); //cannon
    }

    @Override
    public boolean levelUp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Bullet createBullet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
