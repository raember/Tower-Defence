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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

    private ListOf<Enemy> listEnemies = new ListOf();
    private ListOf<Tower> listTowers = new ListOf();
    private ListOf<Bullet> listBullets = new ListOf();
    private GameMap currentMap;
    private BuildingManager buildManager = new BuildingManager(this);

    private final String GAMETITLE = "Tower Defence";
    private final JFrame FRAME;
    private final JPanel PANEL;
    private final BufferStrategy BUFFER_STRATEGY;
    private final MouseInputHandler MOUSE = new MouseInputHandler();
    private final KeyInputHandler KEYBOARD = new KeyInputHandler();

    private boolean isRunning;
    private int balance = 500;

    private int width = 800;
    private int height = 600;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public final int MAPOFFSETX = 54;
    public final int MAPOFFSETY = 23;
    public final int PAINTMARGIN = 10;
    private final int WIDTHCORRECTION = -10;
    private final int HEIGHTCORRECTION = -10;

    private final long NSPS = 1000000000;
    private final int NSPMS = 1000000;
    private final int TARGET_FPS = 60;
    private int fps;
    private final long TARGET_NSPF = NSPS / TARGET_FPS;
    private double currentTime;

    public final int TILEWIDTH = 30;

    public final Color colGainsboro = new Color(220, 220, 220);
    public final Color colBackground = new Color(46, 46, 48);
    public final Color colStartTile = new Color(56, 56, 58);
    public final Color colEndTile = new Color(56, 56, 58);
    public final Color colWallTile = new Color(66, 66, 68);
    public final Color colTowerTile = new Color(66, 66, 150);

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
        addKeyListener(KEYBOARD);
        addMouseListener(MOUSE);
        addMouseMotionListener(MOUSE);
        requestFocus();
        createBufferStrategy(2);
        BUFFER_STRATEGY = getBufferStrategy();
    }

    public boolean isGameRunning() {
        return isRunning;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int bal) {
        balance = bal;
    }

    public ListOf<Enemy> getEnemies() {
        return listEnemies;
    }

    public ListOf<Tower> getTowers() {
        return listTowers;
    }

    public ListOf<Bullet> getBullets() {
        return listBullets;
    }

    public GameMap getMap() {
        return currentMap;
    }

    private void resize() {
        PANEL.setPreferredSize(new Dimension(width, height + PAINTMARGIN));
        PANEL.setLayout(null);
        setBounds(0, PAINTMARGIN, width, height);
        FRAME.pack();
    }

    @Override
    public void paint(Graphics gObj) {
        Graphics2D g = (Graphics2D) gObj;
        if (currentMap == null) {
            return;
        }

        //Translate to th eupper left corner of the map.
        g.translate(MAPOFFSETX, MAPOFFSETY);

        //Paint the GameObjects in a adequate order.
        currentMap.paint(g);
        listEnemies.forEach(e -> e.paint(g));
        listTowers.forEach(t -> t.paint(g));
        listBullets.forEach(b -> b.paint(g));

        //Translate back to the upper left corner of the canvas...
        g.translate(-MAPOFFSETX, -MAPOFFSETY);

        //...and draw a crosshair.
        g.setColor(colGainsboro);
        g.translate(MOUSE.xMouse, MOUSE.yMouse);
        g.drawLine(-10, 0, 10, 0);
        g.drawLine(0, -10, 0, 10);
        g.translate(-MOUSE.xMouse, -MOUSE.yMouse);

        //Now print some data.
        g.setFont(new Font("Consolas", 0, 15));
        g.drawString("TIME: " + String.format("%.2f", currentTime), 5, 20);
        g.drawString("BALANCE: " + balance, 5, 50);
        Point p = MOUSE.getPoint();
        g.drawString("POS: " + p.x + ", " + p.y, 5, 70);
        g.drawString("MAP POS: " + (p.x - MAPOFFSETX) + ", "
                + (p.y - MAPOFFSETY), 5, 100);
        p = currentMap.getMapCoordinate(p);
        g.drawString("POS: " + p.x + ", " + p.y, 5, 130);
    }

    public void update(double deltatime, double abstime) {
        if (MOUSE.getLeftButton()) {
            Point posClicked = MOUSE.getPoint();
            Rectangle mapArea = currentMap.getRectangle();
            if (mapArea.contains(posClicked)) {
                Tower newTower = new NormalTower(this);
                buildManager.tryPlaceTower(newTower, posClicked);
            }
        }
        if (KEYBOARD.isSpacePressed()) {
            Enemy en = new NormalEnemy(this);
            currentMap.sendEnemy(en);
            listEnemies.add(en);
        }
        currentMap.update(deltatime, abstime);
        buildManager.update(deltatime, abstime);
        listEnemies.forEach(e -> e.update(deltatime, abstime));
        listTowers.forEach(t -> t.update(deltatime, abstime));
        listBullets.forEach(b -> b.update(deltatime, abstime));
    }

    public void startGame() {
        if (loadMap("Map1.csv")) {
            isRunning = true;
            doGameLoop();
        }
    }

    public boolean loadMap(String csv) {
        String path = GameForm.class.getProtectionDomain().getCodeSource().
                getLocation().getPath();
        currentMap = GameMap.load(new File(path + csv), this);
        if (currentMap == null) {
            return false;
        }
        width = currentMap.getWidth() + MAPOFFSETX + 10;
        height = currentMap.getHeight() + MAPOFFSETY + 10;
        resize();
        return true;
    }

    private void doGameLoop() {
        long nanoSecPerSec = 0;
        long absLastFrameBegin = System.nanoTime();
        boolean isFirstRun = true;
        currentTime = 0d;
        while (isRunning) {
            long absFrameBegin = System.nanoTime();
            long lastFrameDuration = absFrameBegin - absLastFrameBegin;
            double deltaSec = (double) lastFrameDuration / NSPS;
            nanoSecPerSec += lastFrameDuration;
            fps++;
            if (nanoSecPerSec >= NSPS) {
                FRAME.setTitle(GAMETITLE + " (FPS: " + fps + ")");
                nanoSecPerSec = 0;
                fps = 0;
            }

            if (!isFirstRun) {
                update(deltaSec, currentTime);
                Graphics2D g = (Graphics2D) BUFFER_STRATEGY.getDrawGraphics();
                HashMap antiAlias = new HashMap(3);
                antiAlias.put(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                antiAlias.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                antiAlias.put(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                g.addRenderingHints(antiAlias);
                g.setColor(colBackground);
                g.fillRect(0, 0, width, height);
                paint(g);
                BUFFER_STRATEGY.show();
                currentTime += deltaSec;
            }

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

        private boolean isSpace;

        public boolean isSpacePressed() {
            if (isSpace) {
                isSpace = false;
                return true;
            }
            return false;
        }

        /**
         * Notification from AWT that a key has been typed. Note that typing a
         * key means to both press and then release it.
         *
         * @param e The details of the key that was typed.
         */
        @Override
        public void keyTyped(KeyEvent e) {
            switch (e.getKeyChar()) {
                case KeyEvent.VK_SPACE:
                    isSpace = true;
                    return;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
            }
        }
    }

    private class MouseInputHandler extends MouseAdapter {

        private int xMouse;
        private int yMouse;
        private boolean isLeftPressed;
        private boolean isRightPressed;

        public int getX() {
            return xMouse;
        }

        public int getY() {
            return yMouse;
        }

        public boolean getLeftButton() {
            if (isLeftPressed) {
                isLeftPressed = false;
                return true;
            }
            return false;
        }

        public boolean getRightButton() {
            if (isRightPressed) {
                isRightPressed = false;
                return true;
            }
            return false;
        }

        public Point getPoint() {
            return new Point(xMouse, yMouse);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    isLeftPressed = true;
                    return;
                case MouseEvent.BUTTON3:
                    isRightPressed = true;
                    return;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            xMouse = e.getX();
            yMouse = e.getY();
        }
    }
}
