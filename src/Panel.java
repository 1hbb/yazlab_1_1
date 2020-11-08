import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Panel extends JPanel {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    int UNIT_SIZE;
    int coinCount;
    ArrayList<Integer> coinCountXY = new ArrayList<Integer>();
    ArrayList<Integer> coinX = new ArrayList<Integer>();
    ArrayList<Integer> coinY = new ArrayList<Integer>();
    boolean running = false;
    Random random;
    int size = 0;

    Panel() {
        Scanner sc = new Scanner(System.in);
        String str1 = JOptionPane.showInputDialog("Boyutu Girin: ");
        int size = Integer.parseInt(str1);
        UNIT_SIZE = 800 / size;
        coinCount = (SCREEN_HEIGHT / UNIT_SIZE * SCREEN_HEIGHT / UNIT_SIZE) * 20 / 100;
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        startGame();
        randomCoins();

    }

    public void randomCoins() {

        for (int i = 0; i < coinCount; i++) {
            coinX.add(random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE);
            coinY.add(random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE);
            coinCountXY.add((random.nextInt((int) (4)) + 1) * 5);
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
        for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            g.setColor(Color.BLACK);
        }
        int counter = 0;

        for (int i = 0; i < coinCount; i++) {

            if (i < Math.round(((coinCount) * 1 / 10))) {
                g.setColor(Color.RED);
                //g.fillRect(coinX[i], coinY[i], UNIT_SIZE, UNIT_SIZE);
                //System.out.print(coinX[i] + " " + coinY[i] + "\n");
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

    public void move() {


    }


}
