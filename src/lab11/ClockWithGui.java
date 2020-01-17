package lab11;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.time.LocalTime;

import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_MITER;

public class ClockWithGui extends JPanel {

    LocalTime time = LocalTime.now();

    class ClockThread extends Thread {
        @Override
        public void run() {
            for (; ; ) {
                time = LocalTime.now();
                System.out.printf("%02d:%02d:%02d\n", time.getHour(), time.getMinute(), time.getSecond());

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                repaint();


            }
        }

        ClockThread() {

        }
    }

    ClockWithGui() {

        new ClockThread().start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Clock");
        frame.setContentPane(new ClockWithGui());
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);


        new ClockWithGui();

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, getHeight() / 2);

        for (int i = 1; i < 13; i++) {
            AffineTransform at = new AffineTransform();
            at.rotate(2 * Math.PI / 12 * i);
            Point2D src = new Point2D.Float(0, -120);
            Point2D trg = new Point2D.Float();
            at.transform(src, trg);
            g2d.drawString(Integer.toString(i), (int) trg.getX(), (int) trg.getY());

        }


        for (int i = 0; i < 60; i++) {

            g2d.drawLine(0, -90, 0, -97);
            g2d.rotate(2 * Math.PI / 60);
//            g2d.setTransform(saveAT);

//            AffineTransform at2 = new AffineTransform();
//            at2.rotate(2 * Math.PI / 12 * i);
//            Point2D src = new Point2D.Float(0, -120);
//            Point2D trg = new Point2D.Float();
//            at2.transform(src, trg);
//            g2d.drawLine((int) trg.getX(), (int) trg.getY(), (int) trg.getX()-10, (int) trg.getY());
        }

        AffineTransform saveAT = g2d.getTransform();

        g2d.rotate(time.getHour() % 12 * 2 * Math.PI / 12);
        g2d.setStroke(new BasicStroke(3, CAP_ROUND, JOIN_MITER));
        g2d.drawLine(0, 0, 0, -40);
        g2d.setTransform(saveAT);

        g2d.rotate(time.getMinute() % 60 * 2 * Math.PI / 60);
        g2d.setStroke(new BasicStroke(2, CAP_ROUND, JOIN_MITER));
        g2d.setColor(Color.gray);
        g2d.drawLine(0, 0, 0, -70);
        g2d.setTransform(saveAT);

        g2d.rotate(time.getSecond() % 60 * 2 * Math.PI / 60);
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(1, CAP_ROUND, JOIN_MITER));
        g2d.drawLine(0, 0, 0, -100);
        g2d.setTransform(saveAT);

        setOpaque(false);

    }


}