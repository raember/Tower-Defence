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

import com.sun.glass.ui.Application;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.net.URLDecoder;
import javax.swing.*;

/**
 * @author Raphael
 * @date 29.09.2016
 */
public class GameForm extends Canvas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameForm game = new GameForm();
        game.startGame();
    }

    public ListOf<Enemy> listEnemies = new ListOf();
    public ListOf<Tower> listTowers = new ListOf();
    public ListOf<Bullet> listBullets = new ListOf();
    public Map currentMap;

    private final String GAMETITLE = "Tower Defense";
    private final JFrame FRAME;
    private final JPanel PANEL;
    private final BufferStrategy BUFFER_STRATEGY;
    private int width = 800;
    private int height = 600;
    private final int WIDTHCORRECTION = -10;
    private final int HEIGHTCORRECTION = -10;
    private final int XCENTER = width / 2;
    private final int YCENTER = height / 2;

    private final long NSPS = 1000000000;
    private final int NSPMS = 1000000;
    private final int TARGET_FPS = 60;
    private int fps;
    private final long TARGET_NSPF = NSPS / TARGET_FPS;
    private boolean isGameRunning;

    private boolean waitingForKeyPress = true;

    public GameForm() {
        FRAME = new JFrame(GAMETITLE);
        PANEL = (JPanel) FRAME.getContentPane();
        PANEL.add(this);
        resize();
        setIgnoreRepaint(true);
        FRAME.setResizable(false);
        FRAME.setLocationRelativeTo(null);
        FRAME.setVisible(true);
        FRAME.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        addKeyListener(new KeyInputHandler());
        requestFocus();
        createBufferStrategy(2);
        BUFFER_STRATEGY = getBufferStrategy();
    }

    private void resize() {
        PANEL.setPreferredSize(new Dimension(width + WIDTHCORRECTION, height
                + HEIGHTCORRECTION));
        PANEL.setLayout(null);
        setBounds(0, 0, width, height);
        FRAME.pack();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (currentMap == null) {
            return;
        }
        currentMap.paint(g2);
        listEnemies.forEach(e -> e.paint(g2));
        listTowers.forEach(t -> t.paint(g2));
        listBullets.forEach(b -> b.paint(g2));
    }

    public void update(double deltatime, double abstime) {
        listEnemies.forEach(e -> e.update(deltatime, abstime));
        listTowers.forEach(t -> t.update(deltatime, abstime));
        listBullets.forEach(b -> b.update(deltatime, abstime));
    }

    public ListOf<Enemy> findNearestEnemies(Point from, double range) {
        ListOf<Enemy> nearestEnemy = new ListOf();
        double distance = range;
        for (Enemy e : listEnemies) {
            double tempDistance = from.distance(e.center);
            if (!nearestEnemy.any() || tempDistance < distance) {
                nearestEnemy.add(e);
                distance = tempDistance;
            }
        }
        return nearestEnemy.sortAll((Enemy e1, Enemy e2)
                -> Double.compare(e1.center.distance(from),
                        e2.center.distance(from)));
    }

    public void startGame() {
        isGameRunning = true;
        String path = GameForm.class.getProtectionDomain().getCodeSource().
                getLocation().getPath();
        currentMap = Map.load(new File(path + "Map1.csv"), this);
        width = currentMap.getWidth() + 30;
        height = currentMap.getHeight() + 30;
        resize();
        doGameLoop();
    }

    private void doGameLoop() {
        long nanoSecPerSec = 0;
        long absLastFrameBegin = System.nanoTime();
        boolean isFirstRun = true;
        double absTime = 0d;
        while (isGameRunning) {
            long absFrameBegin = System.nanoTime();
            long lastFrameDuration = absFrameBegin - absLastFrameBegin;
            double deltaSec = (double) lastFrameDuration / TARGET_FPS / NSPS;
            nanoSecPerSec += lastFrameDuration;
            fps++;
            if (nanoSecPerSec >= NSPS) {
                FRAME.setTitle(GAMETITLE + " (FPS: " + fps + ")");
                nanoSecPerSec = 0;
                fps = 0;
            }

            if (!isFirstRun) {
                Graphics2D g = (Graphics2D) BUFFER_STRATEGY.getDrawGraphics();
                g.setColor(new Color(46, 46, 48));
                g.fillRect(0, 0, width, height);
//                g.setColor(Color.red);
//                g.translate(XCENTER, YCENTER);
//                g.drawLine(-10, 0, 10, 0);
//                g.drawLine(0, -10, 0, 10);
//                g.translate(-XCENTER, -YCENTER);
                g.translate(10, 10);
                paint(g);
                absTime += deltaSec;
                update(deltaSec, absTime);
            }
            BUFFER_STRATEGY.show();

            long absNow = System.nanoTime();
            try {
                Thread.sleep((TARGET_NSPF - (absNow - absFrameBegin)) / NSPMS);
            } catch (Exception ex) {
                System.out.println(
                        "Dad! I couldn't sleep properly. I tried to sleep for "
                        + (TARGET_NSPF - (absNow - absFrameBegin)) / NSPMS
                        + "ms");
                //It's ok, darling. Let daddy fix it for you. <3
            } finally {
                absLastFrameBegin = absFrameBegin;
                isFirstRun = false;
            }
        }
    }

    /**
     * A class to handle keyboard input from the user. The class handles both
     * dynamic input during game play, i.e. left/right and shoot, and more
     * static type input (i.e. press any key to continue)
     *
     * This has been implemented as an inner class more through habbit then
     * anything else. Its perfectly normal to implement this as seperate class
     * if slight less convienient.
     *
     * @author Kevin Glass
     */
    private class KeyInputHandler extends KeyAdapter {

        /**
         * The number of key presses we've had while waiting for an "any key"
         * press
         */
        private int pressCount = 1;

        /**
         * Notification from AWT that a key has been pressed. Note that a key
         * being pressed is equal to being pushed down but *NOT* released. Thats
         * where keyTyped() comes in.
         *
         * @param e The details of the key that was pressed
         */
        @Override
        public void keyPressed(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't 
            // want to do anything with just a "press"
            if (waitingForKeyPress) {
                return;
            }

//            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//                leftPressed = true;
//            }
//            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//                rightPressed = true;
//            }
//            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//                firePressed = true;
//            } 
        }

        /**
         * Notification from AWT that a key has been released.
         *
         * @param e The details of the key that was released
         */
        @Override
        public void keyReleased(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't 
            // want to do anything with just a "released"
            if (waitingForKeyPress) {
                return;
            }

//            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//                leftPressed = false;
//            }
//            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//                rightPressed = false;
//            }
//            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//                firePressed = false;
//            }
        }

        /**
         * Notification from AWT that a key has been typed. Note that typing a
         * key means to both press and then release it.
         *
         * @param e The details of the key that was typed.
         */
        @Override
        public void keyTyped(KeyEvent e) {
            // if we're waiting for a "any key" type then
            // check if we've recieved any recently. We may

            // have had a keyType() event from the user releasing
            // the shoot or move keys, hence the use of the "pressCount"
            // counter.
            if (waitingForKeyPress) {
                if (pressCount == 1) {
                    // since we've now recieved our key typed

                    // event we can mark it as such and start 
                    // our new game
                    waitingForKeyPress = false;
                    //startGame();
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }

            // if we hit escape, then quit the game
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }
}
