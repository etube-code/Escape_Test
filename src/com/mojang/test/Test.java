package com.mojang.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Test extends JComponent {
	

	
	
	
	public Test(int w, int h) {
		
		Dimension size = new Dimension(w, h);
		setPreferredSize(size);
			
	}
	
    //@Override
    public void paint(Graphics g) {
      g.setColor(Color.RED);
      g.fillRect(0, 0, getWidth(), getHeight());
      g.setColor(Color.GREEN);
      g.fillArc(100, 100, 250, 250, 0, ABORT);
    }	
		
	public static void main(String[] args) {
		
		Test game = new Test(640, 480);
		
		JFrame frame = new JFrame("Escape");
		
		frame.add(game);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
			
	}
	
	
	
	
	
}

/*package com.mojang.test;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Test extends JComponent {
	
public static Test game = new Test();	
	
	
	
	public Test() {
		
		Dimension size = new Dimension(640, 480);
		setPreferredSize(size);
		
		
		
	}
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Escape");
		
		
		
		frame.add(game);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		
		
		
		
		
	}
	
	
	
	
	
}*/