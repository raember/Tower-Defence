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
 * @author raphael
 * @date 10.10.2016
 */
public enum MapData {

    WALL(0),
    PATH(1),
    START(2),
    END(3),
    TOWER(4);

    private int data;

    private MapData(int data) {
        this.data = data;
    }

    public static MapData parse(int data) {
        for (MapData md : MapData.values()) {
            if (md.data == data) {
                return md;
            }
        }
        return null;
    }

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

    public boolean isWalkable() {
        return isPathTile() || isEndTile() || isStartTile();
    }

    public boolean isEndTile() {
        return data == 3;
    }

    public boolean isPathTile() {
        return data == 1;
    }

    public boolean isStartTile() {
        return data == 2;
    }
}
