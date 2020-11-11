import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;

    int UNIT_SIZE;
    int coinCount;
    int secretCoinCount;

    ArrayList<Integer> coinCountXY = new ArrayList<>();
    ArrayList<Integer> coinX = new ArrayList<>();
    ArrayList<Integer> coinY = new ArrayList<>();

    boolean running = false;
    Random random;

    int SIZE;
    int[][] COIN_MATRIX = new int[SIZE][SIZE];
    int[][] SECRET_COIN_MATRIX = new int[SIZE][SIZE];

    int MOVE_STEP = 3; // HER HAMLE ICIN ADIM SAYISI

    int A_PLAYER_COIN_COUNT = 200;
    int B_PLAYER_COIN_COUNT = 200;
    int C_PLAYER_COIN_COUNT = 200;
    int D_PLAYER_COIN_COUNT = 200;

    int A_PLAYER_TARGET_COST = 5;
    int B_PLAYER_TARGET_COST = 10;
    int C_PLAYER_TARGET_COST = 15;
    int D_PLAYER_TARGET_COST = 20;

    int A_PLAYER_MOVE_COST = 5;
    int B_PLAYER_MOVE_COST = 5;
    int C_PLAYER_MOVE_COST = 5;
    int D_PLAYER_MOVE_COST = 5;

    int[] A_PLAYER_CURRENT_TARGET = new int[2];
    int[] B_PLAYER_CURRENT_TARGET = new int[2];
    int[] C_PLAYER_CURRENT_TARGET = new int[2];
    int[] D_PLAYER_CURRENT_TARGET = new int[2];

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
        SIZE = size;
        UNIT_SIZE = 800 / size;
        coinCount = (SCREEN_HEIGHT / UNIT_SIZE * SCREEN_HEIGHT / UNIT_SIZE) * 20 / 100;
        secretCoinCount = coinCount * 1 / 10;
        System.out.println("secret coin count " + secretCoinCount);
        A_PLAYER_LOCATION_X = 0;
        A_PLAYER_LOCATION_Y = 0;
        B_PLAYER_LOCATION_X = size - 1;
        B_PLAYER_LOCATION_Y = 0;
        C_PLAYER_LOCATION_X = 0;
        C_PLAYER_LOCATION_Y = size - 1;
        D_PLAYER_LOCATION_X = size - 1;
        D_PLAYER_LOCATION_Y = size - 1;


        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT + 100));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
        randomCoins();

    }

    public void randomCoins() {
        int randX;
        int randY;
        int counter;
        for (int i = 0; i < coinCount; i++) {
            counter = 0;
            randX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            randY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            for (int j = 0; j < i; j++) {
                if (randX == coinX.get(j) && randY == coinY.get(j)) {
                    counter--;
                } else {
                    if (randX / UNIT_SIZE == A_PLAYER_LOCATION_X && randY / UNIT_SIZE == A_PLAYER_LOCATION_Y) {
                        counter--;
                    }
                    if (randX / UNIT_SIZE == B_PLAYER_LOCATION_X && randY / UNIT_SIZE == B_PLAYER_LOCATION_Y) {
                        counter--;
                    }
                    if (randX / UNIT_SIZE == C_PLAYER_LOCATION_X && randY / UNIT_SIZE == C_PLAYER_LOCATION_Y) {
                        counter--;
                    }
                    if (randX / UNIT_SIZE == D_PLAYER_LOCATION_X && randY / UNIT_SIZE == D_PLAYER_LOCATION_Y) {
                        counter--;
                    }

                }


            }
            if (counter <= -1) {
                i--;
            } else {
                coinX.add(i, randX);
                coinY.add(i, randY);
                coinCountXY.add(i, (random.nextInt((int) (4)) + 1) * 5);
            }
        }

        int[][] secretCoinMatrix = new int[SIZE][SIZE];

        for (int l = 0; l < secretCoinCount; l++) {
            secretCoinMatrix[(coinX.get(l) / UNIT_SIZE)][(coinY.get(l) / UNIT_SIZE)] = coinCountXY.get(l);
            coinCountXY.remove(l);
            coinX.remove(l);
            coinY.remove(l);
        }

        SECRET_COIN_MATRIX = secretCoinMatrix;

        int[][] coinMatrix = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < coinCount - secretCoinCount; k++) {
                    if ((coinX.get(k) / UNIT_SIZE) == i && (coinY.get(k) / UNIT_SIZE) == j) {
                        coinMatrix[i][j] = coinCountXY.get(k);
                    }
                }
            }
        }

        COIN_MATRIX = coinMatrix;
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

        g.setColor(Color.RED);
        g.drawString("A PLAYER: " + A_PLAYER_COIN_COUNT + " COIN", SCREEN_WIDTH / 4 * 0, SCREEN_HEIGHT + 50);
        g.drawString("B PLAYER: " + B_PLAYER_COIN_COUNT + " COIN", SCREEN_WIDTH / 4 * 1, SCREEN_HEIGHT + 50);
        g.drawString("C PLAYER: " + C_PLAYER_COIN_COUNT + " COIN", SCREEN_WIDTH / 4 * 2, SCREEN_HEIGHT + 50);
        g.drawString("D PLAYER: " + D_PLAYER_COIN_COUNT + " COIN", SCREEN_WIDTH / 4 * 3, SCREEN_HEIGHT + 50);

        for (int i = 0; i <= SIZE; i++) {
            g.setColor(Color.BLACK);
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (COIN_MATRIX[i][j] != 0) {
                    g.setColor(Color.BLUE);
                    g.drawString(String.valueOf(COIN_MATRIX[i][j]), (i * UNIT_SIZE) + (UNIT_SIZE / 2), (j * UNIT_SIZE) + (UNIT_SIZE / 2));
                }
            }
        }

//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZE; j++) {
//                System.out.print(SECRET_COIN_MATRIX[i][j] + " ");
//            }
//            System.out.println();
//        }

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
                    //FIND_TARGET_FOR_C_PLAYER();
                    MOVE_A();
                    repaint();
                    //FIND_TARGET_FOR_A_PLAYER();
                    //FIND_TARGET_FOR_B_PLAYER();
                    //randomCoins();
                    //repaint();
                    break;


            }
        }
    }

    public int[] FIND_TARGET_FOR_A_PLAYER() {
        int[] target = new int[2]; // target[0] = hedefin x koordinatı target[1] hedefin y koordinatı olacak
        int distance = 1000000; // mesafeyi max olarak atadım başta çünkü en yakını bulacaz
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (COIN_MATRIX[i][j] != 0) { // sadece altın olan noktalarda işlem yapmak için
                    int tmp = Math.abs((A_PLAYER_LOCATION_X - i)) + Math.abs((A_PLAYER_LOCATION_Y - j)); // A oyuncusunun konumu ile altın arasındaki mesafenin mutlak değeri
                    if (tmp < distance) { //en yakını bulduğumuz kısım
                        distance = tmp; // mesafeyi her tmp değişkenine atıyoruz
                        target[0] = i; //en yakındaki altının x koordinatı
                        target[1] = j; //en yakındaki altının y koordinatı
                    }
                }
            }
        }
        System.out.println("A player target i: " + target[0] + " j: " + target[1]);
        System.out.println("Coint count " + COIN_MATRIX[target[0]][target[1]]);


        return target; // içinde hedefin x ve y koordinatı olan diziyi döndürüyoruz
    }

    public int[] FIND_TARGET_FOR_B_PLAYER() {
        int[] target = new int[2];
        int distance = 1000000;
        int earning = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (COIN_MATRIX[i][j] != 0) {
                    int tmp = Math.abs((B_PLAYER_LOCATION_X - i)) + Math.abs((B_PLAYER_LOCATION_Y - j));
                    tmp = Math.abs(tmp);
                    int step;
                    if (tmp % MOVE_STEP == 0) {
                        step = tmp / MOVE_STEP;
                    } else {
                        step = (tmp / MOVE_STEP) + 1;
                    }
                    int tmp_earning = COIN_MATRIX[i][j] - (step * B_PLAYER_MOVE_COST);
                    if (tmp <= distance && tmp_earning >= earning) {
                        distance = tmp;
                        earning = tmp_earning;
                        target[0] = i;
                        target[1] = j;

                    }
                }
            }
        }
        System.out.println("B player target i: " + target[0] + " j: " + target[1]);
        System.out.println("B Coint count " + COIN_MATRIX[target[0]][target[1]]);
        System.out.println("B Player Earning For Move: " + earning);


        return target;

    }

    public int[] FIND_TARGET_FOR_C_PLAYER() {

        SHOW_SECRET_COIN();
        SHOW_SECRET_COIN();

        int[] target = new int[2];
        int distance = 1000000;
        int earning = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (COIN_MATRIX[i][j] != 0) {
                    int tmp = Math.abs((C_PLAYER_LOCATION_X - i)) + Math.abs((C_PLAYER_LOCATION_Y - j));
                    tmp = Math.abs(tmp);
                    int step;
                    if (tmp % MOVE_STEP == 0) {
                        step = tmp / MOVE_STEP;
                    } else {
                        step = (tmp / MOVE_STEP) + 1;
                    }
                    int tmp_earning = COIN_MATRIX[i][j] - (step * C_PLAYER_MOVE_COST);
                    if (tmp <= distance && tmp_earning >= earning) {
                        distance = tmp;
                        earning = tmp_earning;
                        target[0] = i;
                        target[1] = j;


                    }
                }
            }
        }
        System.out.println("C player target i: " + target[0] + " j: " + target[1]);
        System.out.println("C Coint count " + COIN_MATRIX[target[0]][target[1]]);
        System.out.println("C Player Earning For Move: " + earning);


        return target;
    }


    public void SHOW_SECRET_COIN() {
        int[] target = new int[2];
        int distance = 1000000;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (SECRET_COIN_MATRIX[i][j] != 0) {
                    int tmp = Math.abs((C_PLAYER_LOCATION_X - i)) + Math.abs((C_PLAYER_LOCATION_Y - j));
                    tmp = Math.abs(tmp);
                    if (tmp <= distance) {
                        distance = tmp;
                        target[0] = i;
                        target[1] = j;

                    }
                }
            }
        }
        int x = target[0];
        int y = target[1];

        COIN_MATRIX[x][y] = SECRET_COIN_MATRIX[x][y];
        SECRET_COIN_MATRIX[x][y] = 0;

    }


    public void FIND_TARGET_FOR_D_PLAYER() {

    }

    public void MOVE_A() {
        boolean moved = false;
        int halfMove = 1;
        if (A_PLAYER_CURRENT_TARGET[1] == 0 && A_PLAYER_CURRENT_TARGET[1] == 0) { //başlangıçta hedef belirleme
            A_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_A_PLAYER();
            A_PLAYER_COIN_COUNT = A_PLAYER_COIN_COUNT - A_PLAYER_TARGET_COST;
        }
        if (COIN_MATRIX[A_PLAYER_CURRENT_TARGET[0]][A_PLAYER_CURRENT_TARGET[1]] == 0) { //hedef başkası tarafından yendi ise yeniden hedef belirle
            A_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_A_PLAYER();
            A_PLAYER_COIN_COUNT = A_PLAYER_COIN_COUNT - A_PLAYER_TARGET_COST;
        } // hareket ettirme kısmı
        if (A_PLAYER_LOCATION_X > A_PLAYER_CURRENT_TARGET[0] && moved == false) {
            if (A_PLAYER_LOCATION_X - A_PLAYER_CURRENT_TARGET[0] >= MOVE_STEP) {
                A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X - MOVE_STEP;
                moved = true;
            } else {
                A_PLAYER_LOCATION_X = A_PLAYER_CURRENT_TARGET[0];
                halfMove = MOVE_STEP - (A_PLAYER_LOCATION_X - A_PLAYER_CURRENT_TARGET[0]);
            }
        } else if (A_PLAYER_CURRENT_TARGET[0] > A_PLAYER_LOCATION_X && moved == false) {
            if (A_PLAYER_CURRENT_TARGET[0] - A_PLAYER_LOCATION_X >= MOVE_STEP) {
                A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X + MOVE_STEP;
                moved = true;
            } else {
                A_PLAYER_LOCATION_X = A_PLAYER_CURRENT_TARGET[0];
            }

        }
        if (A_PLAYER_LOCATION_Y > A_PLAYER_CURRENT_TARGET[1] && moved == false) {
            if (A_PLAYER_LOCATION_Y - A_PLAYER_CURRENT_TARGET[1] >= MOVE_STEP) {
                A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y - MOVE_STEP;
                moved = true;
            } else {
                A_PLAYER_LOCATION_Y = A_PLAYER_CURRENT_TARGET[1];
            }

        } else if (A_PLAYER_CURRENT_TARGET[1] > A_PLAYER_LOCATION_Y && moved == false) {
            if (A_PLAYER_CURRENT_TARGET[1] - A_PLAYER_LOCATION_Y >= MOVE_STEP) {
                A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y + MOVE_STEP;
                moved = true;
            } else {
                A_PLAYER_LOCATION_Y = A_PLAYER_CURRENT_TARGET[1];
            }
        }
        if (A_PLAYER_LOCATION_X == A_PLAYER_CURRENT_TARGET[0] && A_PLAYER_LOCATION_Y == A_PLAYER_CURRENT_TARGET[1]) { //hedefine ulaştı ise
            A_PLAYER_COIN_COUNT += COIN_MATRIX[A_PLAYER_LOCATION_X][A_PLAYER_LOCATION_Y];
            COIN_MATRIX[A_PLAYER_LOCATION_X][A_PLAYER_LOCATION_Y] = 0;
        }
        A_PLAYER_COIN_COUNT = A_PLAYER_COIN_COUNT - A_PLAYER_MOVE_COST;

    }

    public void MOVE_B() {

    }

    public void MOVE_C() {

    }

    public void MOVE_D() {

    }

}
