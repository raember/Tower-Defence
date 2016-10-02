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

import java.awt.Point;

/**
 * @author Raphael
 * @date 02.10.2016
 */
public class BuildingManager extends GameObject {

    public BuildingManager(GameForm game) {
        super(game);
    }

    @Override
    public void update(double deltatime, double abstime) {
    }

    public void placeTower(Tower tower, Point at) {
        if (Game.balance >= tower.cost) {
            Game.balance -= tower.cost;
            tower.center = at;
            Game.listTowers.add(tower);
        }
    }

}
