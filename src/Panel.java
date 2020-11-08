import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Panel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    int UNIT_SIZE;
    int coinCount;
    ArrayList<Integer> coinCountXY = new ArrayList<Integer>();
    ArrayList<Integer> coinX = new ArrayList<Integer>();
    ArrayList<Integer> coinY = new ArrayList<Integer>();
    boolean running = false;
    Random random;
    int size;
    int A_PLAYER_LOCATION_X;
    int A_PLAYER_LOCATION_Y;
    int B_PLAYER_LOCATION_X;
    int B_PLAYER_LOCATION_Y;
    int C_PLAYER_LOCATION_X;
    int C_PLAYER_LOCATION_Y;
    int D_PLAYER_LOCATION_X;
    int D_PLAYER_LOCATION_Y;


    Panel() {
        String str1 = JOptionPane.showInputDialog("Boyutu Girin: ");
        int size = Integer.parseInt(str1);
        UNIT_SIZE = 800 / size;
        coinCount = (SCREEN_HEIGHT / UNIT_SIZE * SCREEN_HEIGHT / UNIT_SIZE) * 20 / 100;
        A_PLAYER_LOCATION_X = 0;
        A_PLAYER_LOCATION_Y = 0;
        B_PLAYER_LOCATION_Y = 0;
        B_PLAYER_LOCATION_X = size - 1;
        C_PLAYER_LOCATION_X = 0;
        C_PLAYER_LOCATION_Y = size - 1;
        D_PLAYER_LOCATION_X = size - 1;
        D_PLAYER_LOCATION_Y = size - 1;


        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
        randomCoins();

    }

    public void randomCoins() {

        for (int i = 0; i < coinCount; i++) {
            coinX.add(i, random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE);
            coinY.add(i, random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE);
            coinCountXY.add(i, (random.nextInt((int) (4)) + 1) * 5);
        }
    }

    // for start the game
    public void startGame() {
        running = true;

    }

    // for paint
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("A", A_PLAYER_LOCATION_X * UNIT_SIZE + UNIT_SIZE / 2, A_PLAYER_LOCATION_Y * UNIT_SIZE + UNIT_SIZE / 2);
        g.drawString("B", B_PLAYER_LOCATION_X * UNIT_SIZE + UNIT_SIZE / 2, B_PLAYER_LOCATION_Y * UNIT_SIZE + UNIT_SIZE / 2);
        g.drawString("C", C_PLAYER_LOCATION_X * UNIT_SIZE + UNIT_SIZE / 2, C_PLAYER_LOCATION_Y * UNIT_SIZE + UNIT_SIZE / 2);
        g.drawString("D", D_PLAYER_LOCATION_X * UNIT_SIZE + UNIT_SIZE / 2, D_PLAYER_LOCATION_Y * UNIT_SIZE + UNIT_SIZE / 2);

        for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
            g.setColor(Color.BLACK);
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

        }
        int counter = 0;


        for (int i = 0; i < coinCount; i++) {

            if (i < Math.round(((coinCount) * 1 / 10))) {
                g.setColor(Color.RED);
                System.out.println("Gizli" + (coinCount) * 1 / 10);
                g.drawString(String.valueOf(coinCountXY.get(i)), coinX.get(i), coinY.get(i));
                System.out.println(coinX.get(i) + " " + coinY.get(i));
            } else {
                g.setColor(Color.BLUE);
                g.drawString(String.valueOf(coinCountXY.get(i)), coinX.get(i), coinY.get(i));
                System.out.println(coinX.get(i) + " " + coinY.get(i));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("Action Performed");
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    randomCoins();
                    repaint();
                    break;


            }
        }
    }

    public void move() {

    }

}
