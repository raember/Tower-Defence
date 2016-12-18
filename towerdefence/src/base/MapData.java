/*
 * Copyright (C) 2016 raphael
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

import java.awt.Graphics2D;

/**
 * Represents a map tile
 * @author raphael
 * @date 10.10.2016
 */
public enum MapData {

    WALL(0),
    PATH(1),
    START(2),
    END(3),
    TOWER(4);

    /**
     * Tile constructor
     * @param data Tile data number. Refer to constants
     * @return An object representing a tile
     */
    public static MapData parse(int data) {
        for (MapData md : MapData.values()) {
            if (md.data == data) {
                return md;
            }
        }
        return null;
    }

    private int data;

    private MapData(int data) {
        this.data = data;
    }

    /**
     * Paints a tile
     * @param g     The graphics object
     * @param game  The game object
     * @param x     The x coordinate
     * @param y     The y coordinate
     * @param width The tile width
     */
    public void paint(Graphics2D g, GameForm game, int x, int y, int width) {
        switch (this) {
            case WALL:
                g.setColor(game.colWallTile);
                g.fillRect(x, y, width, width);
                return;
            case PATH:
                //The Path-color remains the same as the background.
                return;
            case START:
                g.setColor(game.colStartTile);
                g.fillRect(x, y, width, width);
                return;
            case END:
                g.setColor(game.colEndTile);
                g.fillRect(x, y, width, width);
                return;
            case TOWER:
                g.setColor(game.colTowerTile);
                g.fillRect(x, y, width, width);
                return;
        }
    }

    /**
     * Specifies whether the tile is walkable
     * @return true if walkable, else false
     */
    public boolean isWalkable() {
        return isPathTile() || isEndTile() || isStartTile();
    }

    /**
     * Specifies whether the tile is an end tile
     * @return true if end tile, else false
     */
    public boolean isEndTile() {
        return data == 3;
    }

    /**
     * Specifies whether the tile is a path tile
     * @return true if path tile, else false
     */
    public boolean isPathTile() {
        return data == 1;
    }

    /**
     * Specifies whether the tile is a start tile
     * @return true if start tile, else false
     */
    public boolean isStartTile() {
        return data == 2;
    }

}
