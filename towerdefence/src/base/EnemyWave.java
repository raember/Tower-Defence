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

/**
 * Class representing a custom enemy wave
 * @author Raphael
 * @date 18.12.2016
 */
public class EnemyWave extends GameObject {

    private boolean isWaveRunning;
    private ListOf<Enemy> deployingEnemies = new ListOf<>();
    private double lastDeloyment;

    /**
     * Constructor of an enemy wave
     * @param game game object for backreference
     */
    public EnemyWave(GameForm game) {
        super(game);
    }

    @Override
    public void update(double deltatime, double abstime) {
        if (isWaveRunning) {
            if (abstime - lastDeloyment >= 0.8d) {
                lastDeloyment = abstime;
                Enemy en = deployingEnemies.first();
                Game.getMap().sendEnemy(en);
                Game.getEnemies().add(en);
                deployingEnemies.remove(0);
                if (!deployingEnemies.any()) {
                    isWaveRunning = false;
                }
            }
        }
    }

    /**
     * Specifies whether a wave is running right now
     * @return true if a wave is running right now, else false
     */
    public boolean isWaveRunning() {
        return isWaveRunning;
    }

    /**
     * Starts a wave
     */
    public void runWave() {
        deployingEnemies.clear();
        for (int i = 0; i < 30; i++) {
            deployingEnemies.add(new NormalEnemy(Game));
        }
        isWaveRunning = true;
    }
}
