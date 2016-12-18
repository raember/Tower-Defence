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

import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Random;

/**
 * Represents a game map
 * @author raphael
 * @date 02.10.2016
 */
public final class GameMap extends DrawableObject {

    /**
     * Constructor of a game map
     * @param game game object for backreference
     */
    public static GameMap load(File csv, GameForm game) {
        if (!csv.exists()) {
            return null;
        }
        String content = null;
        try {
            byte[] encoded = Files.
                    readAllBytes(Paths.get(csv.getAbsolutePath()));
            content = new String(encoded, Charset.defaultCharset());
        } catch (Exception ex) {
            return null;
        }
        GameMap newMap = new GameMap(game);
        String[] lines = content.split("(?!0)(\\r|)\\n");
        String[] characters = lines[0].split(";|\\r");
        String test = String.join(";", lines);
        newMap.data = new MapData[lines.length][characters.length];
        int i = 0;
        for (String line : lines) {
            int j = 0;
            for (String character : line.split(";")) {
                if (character.equals("")) {
                    continue;
                }
                if (!character.matches("^\\d$")) {
                    return null;
                }
                newMap.data[i][j] = MapData.parse(Integer.parseInt(character));
                if (newMap.data[i][j] == MapData.START) {
                    newMap.startPoint = new Point(j, i);
                }
                j++;
            }
            if (j == characters.length - 1) {
                j = 0;
            }
            i++;
        }
        return newMap;
    }
    private MapData[][] data;
    private Point startPoint;

    private GameMap(GameForm game) {
        super(game);
    }

    /**
     * Gets the absolut map width
     * @return the map width
     */
    public int getWidth() {
        return Game.TILEWIDTH * data[0].length;
    }

    /**
     * Gets the absolute map height
     * @return the map height
     */
    public int getHeight() {
        return Game.TILEWIDTH * data.length;
    }

    /**
     * Transforms an absolute coordinate to a map coordinate
     * @param p Point on map area
     * @return Map coordinate
     */
    public Point TFScreenToMap(Point p) {
        return new Point(p.x / Game.TILEWIDTH, p.y / Game.TILEWIDTH);
    }

    /**
     * Transforms a map coordinate to an absolute coordinate
     * @param p Map coordinate
     * @return Point on map area
     */
    public Point TFMapCoordinateToMapCenter(Point p) {
        return new Point(p.x * Game.TILEWIDTH + Game.TILEWIDTH / 2,
                p.y * Game.TILEWIDTH + Game.TILEWIDTH / 2);
    }

    /**
     * Translates a point from the map area onto the screen
     * @param p Point on map
     * @return Point on screen
     */
    public Point TLMapToAbs(Point p) {
        Point temp = (Point) p.clone();
        temp.translate(Game.MAPOFFSETX, Game.MAPOFFSETY);
        return temp;
    }

    /**
     * Translates a point from the screen onto the map area
     * @param p Point on screen
     * @return Point on map
     */
    public Point TLAbsToMap(Point p) {
        Point temp = (Point) p.clone();
        temp.translate(-Game.MAPOFFSETX, -Game.MAPOFFSETY);
        return temp;
    }

    /**
     * Checks whether the player can place a tower at specific map coordinates
     * @param mapCoordinate Point where new tower should be placed
     * @return true, if placement possible, else false
     */
    public boolean canPlaceTower(Point mapCoordinate) {
        return data[mapCoordinate.y][mapCoordinate.x] == MapData.TOWER;
    }

    /**
     * Sends enemy at start tile
     * @param e The enemy to send
     */
    public void sendEnemy(Enemy e) {
        e.position = new Point(startPoint.x, startPoint.y);
    }

    @Override
    public void paint(Graphics2D g) {
        int y = 0;
        for (MapData[] mdl : data) {
            int x = 0;
            for (MapData md : mdl) {
                md.paint(g, Game, x, y, Game.TILEWIDTH);
                g.setColor(Game.colBackground);
                g.drawLine(x, y, x, y + Game.TILEWIDTH);
                g.drawLine(x, y, x + Game.TILEWIDTH, y);
                x += Game.TILEWIDTH;
            }
            x = 0;
            y += Game.TILEWIDTH;
        }
    }

    @Override
    public void update(double deltatime, double abstime) {
    }

    /**
     * Gets a rectangle representing a border around the map
     * @return map border
     */
    public Rectangle getRectangle() {
        return new Rectangle(Game.MAPOFFSETX, Game.MAPOFFSETY,
                Game.getWidth(), Game.getHeight());
    }

    /**
     * Gets a tile from a specific map coordinate
     * @param mapcoordinate coordinate of tile to get
     * @return tile
     */
    public MapData getTile(Point mapcoordinate) {
        return data[mapcoordinate.y][mapcoordinate.x];
    }

    /**
     * Calculates new way point for enemy
     * @param e        Enemy
     * @param oldPoint the enemys last point
     * @return new coordinate
     */
    public Point calcNewWayPoint(Enemy e, Point oldPoint) {
        int maxX = data[0].length - 1;
        int maxY = data.length - 1;
        Point newPoint = null;
        ListOf<Point> possiblePoints = new ListOf<>();
        if (oldPoint.y == 18 && oldPoint.x == 3) {
            int i = 0;
        }
        for (int i = Math.max(0, oldPoint.x - 1);
                i <= Math.min(maxX, oldPoint.x + 1);
                i++) {
            for (int j = Math.max(0, oldPoint.y - 1);
                    j <= Math.min(maxY, oldPoint.y + 1);
                    j++) {
                if ((i != oldPoint.x || j != oldPoint.y)
                        && Math.abs(i - oldPoint.x) != Math.abs(j - oldPoint.y)) {
                    if (data[j][i].isWalkable()) {
                        possiblePoints.add(new Point(i, j));
                    }
                }
            }
        }
        switch (e.facingAngle) {
            case EAST:
                possiblePoints.removeAllWhere(p -> p.x < oldPoint.x);
                break;
            case NORTH:
                possiblePoints.removeAllWhere(p -> p.y > oldPoint.y);
                break;
            case WEST:
                possiblePoints.removeAllWhere(p -> p.x > oldPoint.x);
                break;
            case SOUTH:
                possiblePoints.removeAllWhere(p -> p.y < oldPoint.y);
        }
        if (possiblePoints.size() > 1) {
            Random r = new Random();
            newPoint = possiblePoints.get(r.nextInt(possiblePoints.size()));
        } else {
            newPoint = possiblePoints.first();
        }
        return newPoint;
    }
}
