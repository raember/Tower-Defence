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
 * Building manager to allow building tower
 * @author Raphael
 * @date 02.10.2016
 */
public class BuildingManager extends GameObject {

    /**
     * Constructor of a building manager
     * @param game game object for backreference
     */
    public BuildingManager(GameForm game) {
        super(game);
    }

    @Override
    public void update(double deltatime, double abstime) {
    }

    /**
     * Handles a click on the map signalising the need for building a tower or
     * upgrading one.
     * @param position Position of clicked tile
     */
    public void clickOnMap(Point position) {
        GameMap theMap = Game.getMap();
        position = theMap.TFScreenToMap(position);
        if (theMap.canPlaceTower(position)) {
            Tower twr = getTower(position);
            int bal = Game.getBalance();
            if (twr == null) {
                //Place new tower
                twr = new NormalTower(Game);
                if (twr.getCosts() <= bal) {
                    twr.setPosition(position);
                    Game.setBalance(bal - twr.getCosts());
                    Game.getTowers().add(twr);
                }
            } else {
                //TODO: Implement level up
            }
        }
    }

    /**
     * Gets a tower on a certain map coordinate if present
     * @param position Coordinate of tower to get
     * @return The tower if found, else null
     */
    public Tower getTower(Point position) {
        return Game.getTowers().first(t -> t.getPosition().equals(position));
    }
}
