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

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.*;

/**
 *
 * @author raphael
 */
public final class Map extends DrawableObject {

    public static Map load(File csv, GameForm game) {
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
        Map newMap = new Map(game);
        String[] lines = content.split("(?!0)\\r\\n");
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
                j++;
            }
            if (j == characters.length - 1) {
                j = 0;
            }
            i++;
        }
        return newMap;
    }

    private int tileWidth = 30;
    private MapData[][] data;

    private Map(GameForm game) {
        super(game);
    }

    public int getWidth() {
        return tileWidth * data[0].length;
    }

    public int getHeight() {
        return tileWidth * data.length;
    }

    @Override
    public void paint(Graphics2D g) {
        int y = 0;
        for (MapData[] mdl : data) {
            int x = 0;
            for (MapData md : mdl) {
                md.paint(g, x, y, tileWidth);
                g.setColor(new Color(46, 46, 48));
                g.drawLine(x, y, x, y + tileWidth);
                g.drawLine(x, y, x + tileWidth, y);
                x += tileWidth;
            }
            x = 0;
            y += tileWidth;
        }
    }

    @Override
    public void update(double deltatime, double abstime) {
    }


    private enum MapData {

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

        public void paint(Graphics2D g, int x, int y, int width) {
            switch (this) {
                case WALL:
                    g.setColor(new Color(66, 66, 68));
                    g.fillRect(x, y, width, width);
                    return;
                case PATH:
                    return;
                case START:
                case END:
                    g.setColor(new Color(56, 56, 58));
                    g.fillRect(x, y, width, width);
                    return;
                case TOWER:
                    g.setColor(new Color(0, 200, 255));
                    g.fillRect(x, y, width, width);
                    return;
            }
        }
    }
}
