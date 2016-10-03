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

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
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
    public BuildingManager buildManager = new BuildingManager(this);

    private final String GAMETITLE = "Tower Defence";
    private final JFrame FRAME;
    private final JPanel PANEL;
    private final BufferStrategy BUFFER_STRATEGY;
    public final MouseInputHandler MOUSE = new MouseInputHandler();
    public final KeyInputHandler KEYBOARD = new KeyInputHandler();

    public boolean isGameRunning;
    public int balance = 10000;

    public int width = 800;
    public int height = 600;
    public final int PAINTMARGIN = 10;
    private final int WIDTHCORRECTION = -10;
    private final int HEIGHTCORRECTION = -10;

    private final long NSPS = 1000000000;
    private final int NSPMS = 1000000;
    private final int TARGET_FPS = 60;
    private int fps;
    private final long TARGET_NSPF = NSPS / TARGET_FPS;

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

    private void resize() {
        PANEL.setPreferredSize(new Dimension(width + WIDTHCORRECTION, height
                + HEIGHTCORRECTION));
        PANEL.setLayout(null);
        setBounds(0, 0, width, height);
        FRAME.pack();
    }

    @Override
    public void paint(Graphics g) {
        g.translate(PAINTMARGIN, PAINTMARGIN);
        Graphics2D g2 = (Graphics2D) g;
        if (currentMap == null) {
            return;
        }
        currentMap.paint(g2);
        listEnemies.forEach(e -> e.paint(g2));
        listTowers.forEach(t -> t.paint(g2));
        listBullets.forEach(b -> b.paint(g2));
        g.translate(-PAINTMARGIN, -PAINTMARGIN);
        g.setColor(colGainsboro);
        g.translate(MOUSE.XMouse, MOUSE.YMouse);
        g.drawLine(-PAINTMARGIN, 0, PAINTMARGIN, 0);
        g.drawLine(0, -PAINTMARGIN, 0, PAINTMARGIN);
        g.translate(-MOUSE.XMouse, -MOUSE.YMouse);
    }

    public void update(double deltatime, double abstime) {
        currentMap.update(deltatime, abstime);
        buildManager.update(deltatime, abstime);
        listEnemies.forEach(e -> e.update(deltatime, abstime));
        listTowers.forEach(t -> t.update(deltatime, abstime));
        listBullets.forEach(b -> b.update(deltatime, abstime));
    }

    public void startGame() {
        if (loadMap("Map1.csv")) {
            isGameRunning = true;
            doGameLoop();
        }
    }

    public boolean loadMap(String csv) {
        String path = GameForm.class.getProtectionDomain().getCodeSource().
                getLocation().getPath();
        currentMap = Map.load(new File(path + csv), this);
        if (currentMap == null) {
            return false;
        }
        width = currentMap.getWidth() + 30;
        height = currentMap.getHeight() + 30;
        resize();
        return true;
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
                //TODO: Fix MOUSE inputs.
                if (MOUSE.Button != 0) {
                    System.out.println(MOUSE.Button);
                }
                if (MOUSE.Button == MouseEvent.BUTTON1) {
                    Point posClicked = MOUSE.getPoint();
                    Rectangle mapArea = currentMap.getRectangle();
                    if (mapArea.contains(posClicked)) {
                        buildManager.placeTower(new NormalTower(this), posClicked);
                    }
                }
                update(deltaSec, absTime);
                Graphics2D g = (Graphics2D) BUFFER_STRATEGY.getDrawGraphics();
                g.setColor(colBackground);
                g.fillRect(0, 0, width, height);
                paint(g);
                BUFFER_STRATEGY.show();
                absTime += deltaSec;
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

        public KeyEvent typedKey;

        /**
         * Notification from AWT that a key has been typed. Note that typing a
         * key means to both press and then release it.
         *
         * @param e The details of the key that was typed.
         */
        @Override
        public void keyTyped(KeyEvent e) {
            typedKey = e;
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
    }

    private class MouseInputHandler extends MouseAdapter {

        public int XMouse;
        public int YMouse;
        public int Button;

        public Point getPoint() {
            return new Point(XMouse, YMouse);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Button = e.getButton();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Button = e.getButton();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Button = e.getButton();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            XMouse = e.getX();
            YMouse = e.getY();
        }
    }
}
