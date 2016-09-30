/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * @author Raphael
 * @date 29.09.2016
 */
public class Game extends Canvas {

    private final JFrame FRAME;
    private final BufferStrategy BUFFER_STRATEGY;

    public Game() {
        FRAME = new JFrame("Tower Defense");
        JPanel panel = (JPanel) FRAME.getContentPane();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setLayout(null);
        setBounds(0, 0, 800, 600);
        panel.add(this);
        setIgnoreRepaint(true);
        FRAME.pack();
        FRAME.setResizable(false);
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();
    }

    private boolean waitingForKeyPress = true;

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
