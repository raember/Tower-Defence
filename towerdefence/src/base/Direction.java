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

/**
 * Represents a direction
 * @author raphael
 * @date 10.10.2016
 */
public enum Direction {

    NORTH(1), EAST(0), SOUTH(3), WEST(2);
    private int newdegree;

    private Direction(int newdegree) {
        this.newdegree = newdegree;
    }

    /**
     * Gets the angle in newdegree
     * @return
     */
    public int getNewdegree() {
        return newdegree;
    }
}
