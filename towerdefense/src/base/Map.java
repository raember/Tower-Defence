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
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.*;

/**
 *
 * @author raphael
 */
public final class Map extends DrawableObject {

    private Map(GameForm game) {
        super(game);
    }

    public static Map load(File csv) {
        if (!csv.exists()) {
            return null;
        }
        String content = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(csv.getAbsolutePath()));
            content = new String(encoded, Charset.defaultCharset());
        } catch (Exception ex) {
            return null;
        }
        //TODO: Parse the map data.
        return null;
    }

    @Override
    public void paint(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(double deltatime, double abstime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
