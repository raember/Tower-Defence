/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Raphael
 * @date 29.09.2016
 */
public class Game extends Canvas {

    private JFrame frame;
    private BufferStrategy strategy;

    public Game() {
        frame = new JFrame("Tower Defense");
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setLayout(null);
        setBounds(0, 0, 800, 600);
        panel.add(this);
        setIgnoreRepaint(true);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

		// add a listener to respond to the user closing the window. If they
        // do we'd like to exit the game
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

		// add a key input system (defined below) to our canvas
        // so we can respond to key pressed
        addKeyListener(new KeyInputHandler());

        // request the focus so key events come to us
        requestFocus();

		// create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

		// initialise the entities in our game so there's something
        // to see at startup
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
