package com.mojang.test;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Simple3DWorld extends JPanel implements MouseListener, MouseMotionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    // Kamera açıları
    private double rotX = 0;
    private double rotY = 0;
    
    // Mouse pozisyonu
    private int lastMouseX, lastMouseY;
    private boolean dragging = false;
    
    // 3D Noktalar (küpler için)
    private List<Cube> cubes;
    
    public Simple3DWorld() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addMouseListener(this);
        addMouseMotionListener(this);
        
        // Basit bir minecraft dünya oluştur
        createWorld();
    }
    
    private void createWorld() {
        cubes = new ArrayList<>();
        
        // Zemin katmanı (yeşil çim blokları)
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                cubes.add(new Cube(x, 0, z, Color.GREEN));
            }
        }
        
        // Bazı yapılar ekle
        // Küçük ev benzeri yapı
        for (int y = 1; y <= 3; y++) {
            cubes.add(new Cube(-2, y, -2, Color.ORANGE)); // Duvar
            cubes.add(new Cube(-2, y, -1, Color.ORANGE));
            cubes.add(new Cube(-2, y, 0, Color.ORANGE));
            cubes.add(new Cube(-1, y, -2, Color.ORANGE));
            cubes.add(new Cube(0, y, -2, Color.ORANGE));
            cubes.add(new Cube(0, y, -1, Color.ORANGE));
            cubes.add(new Cube(0, y, 0, Color.ORANGE));
            cubes.add(new Cube(-1, y, 0, Color.ORANGE));
        }
        
        // Çatı
        cubes.add(new Cube(-2, 4, -2, Color.RED));
        cubes.add(new Cube(-2, 4, -1, Color.RED));
        cubes.add(new Cube(-2, 4, 0, Color.RED));
        cubes.add(new Cube(-1, 4, -2, Color.RED));
        cubes.add(new Cube(-1, 4, -1, Color.RED));
        cubes.add(new Cube(-1, 4, 0, Color.RED));
        cubes.add(new Cube(0, 4, -2, Color.RED));
        cubes.add(new Cube(0, 4, -1, Color.RED));
        cubes.add(new Cube(0, 4, 0, Color.RED));
        
        // Bazı ağaçlar (kahverengi gövde, yeşil yapraklar)
        for (int y = 1; y <= 4; y++) {
            cubes.add(new Cube(3, y, 3, Color.decode("#8B4513"))); // Gövde
        }
        for (int x = 2; x <= 4; x++) {
            for (int z = 2; z <= 4; z++) {
                for (int y = 4; y <= 6; y++) {
                    if (!(x == 3 && z == 3 && y == 4)) { // Gövdeyi atla
                        cubes.add(new Cube(x, y, z, Color.decode("#228B22")));
                    }
                }
            }
        }
        
        // Rastgele taş blokları
        cubes.add(new Cube(1, 1, 1, Color.GRAY));
        cubes.add(new Cube(-3, 1, 2, Color.GRAY));
        cubes.add(new Cube(4, 1, -1, Color.GRAY));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Tüm küpleri çiz
        for (Cube cube : cubes) {
            drawCube(g2d, cube);
        }
        
        // Kontrol talimatları
        g2d.setColor(Color.WHITE);
        g2d.drawString("Mouse ile sürükleyerek kamerayı çevirin", 10, 20);
    }
    
    private void drawCube(Graphics2D g2d, Cube cube) {
        // Küpün 8 köşe noktası
        Point3D[] vertices = new Point3D[8];
        double size = 0.5;
        
        vertices[0] = new Point3D(cube.x - size, cube.y - size, cube.z - size);
        vertices[1] = new Point3D(cube.x + size, cube.y - size, cube.z - size);
        vertices[2] = new Point3D(cube.x + size, cube.y + size, cube.z - size);
        vertices[3] = new Point3D(cube.x - size, cube.y + size, cube.z - size);
        vertices[4] = new Point3D(cube.x - size, cube.y - size, cube.z + size);
        vertices[5] = new Point3D(cube.x + size, cube.y - size, cube.z + size);
        vertices[6] = new Point3D(cube.x + size, cube.y + size, cube.z + size);
        vertices[7] = new Point3D(cube.x - size, cube.y + size, cube.z + size);
        
        // Rotasyon uygula
        for (Point3D vertex : vertices) {
            rotatePoint(vertex);
        }
        
        // 3D'den 2D'ye projeksiyon
        Point[] screenPoints = new Point[8];
        for (int i = 0; i < 8; i++) {
            screenPoints[i] = project3DTo2D(vertices[i]);
        }
        
        // Küpün yüzlerini çiz (sadece görünen yüzler)
        drawCubeFaces(g2d, screenPoints, cube.color);
    }
    
    private void rotatePoint(Point3D point) {
        // Y ekseni etrafında rotasyon
        double cosY = Math.cos(rotY);
        double sinY = Math.sin(rotY);
        double newX = point.x * cosY - point.z * sinY;
        double newZ = point.x * sinY + point.z * cosY;
        point.x = newX;
        point.z = newZ;
        
        // X ekseni etrafında rotasyon
        double cosX = Math.cos(rotX);
        double sinX = Math.sin(rotX);
        double newY = point.y * cosX - point.z * sinX;
        newZ = point.y * sinX + point.z * cosX;
        point.y = newY;
        point.z = newZ;
    }
    
    private Point project3DTo2D(Point3D point3D) {
        // Basit perspektif projeksiyon
        double distance = 500;
        double scale = distance / (distance + point3D.z);
        int x = (int) (point3D.x * scale * 50 + WIDTH / 2);
        int y = (int) (-point3D.y * scale * 50 + HEIGHT / 2);
        return new Point(x, y);
    }
    
    private void drawCubeFaces(Graphics2D g2d, Point[] points, Color color) {
        // Küpün yüzlerini çiz
        g2d.setColor(color);
        
        // Ön yüz (0,1,2,3)
        int[] xPoints1 = {points[0].x, points[1].x, points[2].x, points[3].x};
        int[] yPoints1 = {points[0].y, points[1].y, points[2].y, points[3].y};
        g2d.fillPolygon(xPoints1, yPoints1, 4);
        
        // Arka yüz (4,5,6,7)
        int[] xPoints2 = {points[4].x, points[5].x, points[6].x, points[7].x};
        int[] yPoints2 = {points[4].y, points[5].y, points[6].y, points[7].y};
        g2d.fillPolygon(xPoints2, yPoints2, 4);
        
        // Üst yüz (3,2,6,7)
        Color topColor = color.brighter();
        g2d.setColor(topColor);
        int[] xPoints3 = {points[3].x, points[2].x, points[6].x, points[7].x};
        int[] yPoints3 = {points[3].y, points[2].y, points[6].y, points[7].y};
        g2d.fillPolygon(xPoints3, yPoints3, 4);
        
        // Alt yüz (0,1,5,4)
        Color bottomColor = color.darker();
        g2d.setColor(bottomColor);
        int[] xPoints4 = {points[0].x, points[1].x, points[5].x, points[4].x};
        int[] yPoints4 = {points[0].y, points[1].y, points[5].y, points[4].y};
        g2d.fillPolygon(xPoints4, yPoints4, 4);
        
        // Sağ yüz (1,2,6,5)
        Color rightColor = color.darker().darker();
        g2d.setColor(rightColor);
        int[] xPoints5 = {points[1].x, points[2].x, points[6].x, points[5].x};
        int[] yPoints5 = {points[1].y, points[2].y, points[6].y, points[5].y};
        g2d.fillPolygon(xPoints5, yPoints5, 4);
        
        // Sol yüz (0,3,7,4)
        Color leftColor = color.darker().darker();
        g2d.setColor(leftColor);
        int[] xPoints6 = {points[0].x, points[3].x, points[7].x, points[4].x};
        int[] yPoints6 = {points[0].y, points[3].y, points[7].y, points[4].y};
        g2d.fillPolygon(xPoints6, yPoints6, 4);
        
        // Kenarları çiz
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        
        // Tüm kenarları çiz
        drawLine(g2d, points[0], points[1]);
        drawLine(g2d, points[1], points[2]);
        drawLine(g2d, points[2], points[3]);
        drawLine(g2d, points[3], points[0]);
        
        drawLine(g2d, points[4], points[5]);
        drawLine(g2d, points[5], points[6]);
        drawLine(g2d, points[6], points[7]);
        drawLine(g2d, points[7], points[4]);
        
        drawLine(g2d, points[0], points[4]);
        drawLine(g2d, points[1], points[5]);
        drawLine(g2d, points[2], points[6]);
        drawLine(g2d, points[3], points[7]);
    }
    
    private void drawLine(Graphics2D g2d, Point p1, Point p2) {
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
        dragging = true;
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            int deltaX = e.getX() - lastMouseX;
            int deltaY = e.getY() - lastMouseY;
            
            rotY += deltaX * 0.01; // Yatay mouse hareketi Y ekseninde rotasyon
            rotX += deltaY * 0.01; // Dikey mouse hareketi X ekseninde rotasyon
            
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            
            repaint();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    // 3D nokta sınıfı
    private static class Point3D {
        double x, y, z;
        
        Point3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
    // Küp sınıfı
    private static class Cube {
        int x, y, z;
        Color color;
        
        Cube(int x, int y, int z, Color color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.color = color;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Basit 3D Minecraft Dünyası");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Simple3DWorld());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}