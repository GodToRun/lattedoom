package lattedoom.engine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
	d_engine engine;
	Mutech0Panel panel;
	public int width = 640;
	public int height = 480;
	public Window(d_engine engine, int xsiz, int ysiz) {
		this.engine = engine;
		this.width = xsiz;
		this.height = ysiz;
		setTitle("mutech0");
		setSize(xsiz, ysiz);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new Mutech0Panel();
        this.add(panel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
		setVisible(true);
		addKeyListener(new KeyAdapter() { //키 이벤트
			@Override
			public void keyPressed(KeyEvent e) { //키 눌렀을때
				engine.onKey(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				engine.onKeyUp(e);
			}
		});
	}
	public void setCursorVisible(boolean v) {
		if (v == false) {
			// Transparent 16 x 16 pixel cursor image.
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			// Create a new blank cursor.
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(0, 0), "blank cursor");

			// Set the blank cursor to the JFrame.
			getContentPane().setCursor(blankCursor);
		}
		else {
			getContentPane().setCursor(Cursor.getDefaultCursor());
		}
	}
	public float getMouseX() {
		Point b = MouseInfo.getPointerInfo().getLocation();
		return b.x - getLocationOnScreen().x;
	}
	public float getMouseY() {
		Point b = MouseInfo.getPointerInfo().getLocation();
		return b.y - getLocationOnScreen().y;
	}
	public void update() {
		//panel.repaint();
		if (engine.renderer != null) {
			engine.renderer.render(panel.getGraphics());
		}
	}
	class Mutech0Panel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if (engine.renderer != null) {
    			engine.renderer.render(g);
    		}
        }
    }
}
