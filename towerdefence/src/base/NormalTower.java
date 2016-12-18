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
 * Represents a normal tower
 * @author Raphael
 * @date 02.10.2016
 */
public class NormalTower extends Tower {

    /**
     * Constructor of a normal tower
     * @param game game object for backreference
     */
    public NormalTower(GameForm game) {
        super(game);
        costs.add(100);
        costs.add(250);
        costs.add(600);
        costs.add(1500);
        range = 5d;
        shootingCooldown = 1.4;
        maxAngularSpeed = 1.8;
        colBase = new Color(0, 120, 255, 150);
        colBaseBorder = new Color(0, 120, 255, 150);
        colHead = colBase;
        colHeadBorder = colBaseBorder;
    }

    @Override
    protected void paintHead(Graphics2D g, int width) {
        //interior
        g.setColor(colHead);
        g.setStroke(new BasicStroke(3));
        g.fillRect(-width / 4, -width / 4, width / 2, width / 2); //center square
        g.fillRect(width / 4, -width / 8, width / 2, width / 4); //cannon
        //frame
        g.setColor(colHeadBorder);
        g.setStroke(new BasicStroke(1));
        g.drawRect(-width / 4, -width / 4, width / 2, width / 2); //center square
        g.drawRect(width / 4, -width / 8, width / 2, width / 4); //cannon
    }

    @Override
    protected void paintBase(Graphics2D g, int width) {
        g.setColor(Game.colWallTile);
        g.fillRect(-width / 2, -width / 2, width, width);
        g.setColor(colBase);
        g.fillRect(-width / 2, -width / 2, width, width);
        g.setColor(colBaseBorder);
        g.setStroke(new BasicStroke(2));
        g.drawRect(-width / 2, -width / 2, width, width);
        g.setStroke(new BasicStroke(1));
    }

    @Override
    public boolean levelUp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Bullet createBullet() {
        return new NormalBullet(Game, position);
    }

}
