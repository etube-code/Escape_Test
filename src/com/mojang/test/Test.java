package com.mojang.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Test extends JComponent implements Runnable {
	
	private int width, height;
	
	private boolean running;
	private Thread thread;
	
	public Test(int width, int height) {
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, 640, 480);
		
		
	}
	
	public void start(){
		
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
		
	}
	
	public void run(){
		
		System.out.println("It's running...");
		
	}
	
	
	
	
	public static void main(String[] args) {
		
		Test game = new Test(640, 480);
		
		JFrame frame = new JFrame("Escape");
		
		frame.add(game);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
	
		game.start();
		
		
	}
	
	
	
}

















/*package com.mojang.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Test extends JComponent implements Runnable{
	
	private boolean running;
	private Thread thread;
	

	
	public Test(int w, int h) {
		
		Dimension size = new Dimension(w, h);
		setPreferredSize(size);
			
	}
	
    //@Override
    public void paint(Graphics g) {
      g.setColor(Color.RED);
      g.fillRect(0, 0, getWidth(), getHeight());

    }	
    
	public synchronized void start() {
		
		if(running) 
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
		
	}

	
	public void run() {
        while (running) {
            tick();      // Mantık güncelleme
            repaint();   // Ekranı yenile
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		
	}
	private void tick() {
		

	}
		
	public static void main(String[] args) {
		
		Test game = new Test(640, 480);
		
		JFrame frame = new JFrame("Escape");
		
		frame.add(game);
		frame.setVisible(true);
		frame.pack();
		game.start();
		frame.setLocationRelativeTo(null);
			
	}
	
	
	
	
	
}*/