import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;

    String NEXT_PLAYER = "A";

    boolean A_PLAYER_ELIMINATED = false;
    boolean B_PLAYER_ELIMINATED = false;
    boolean C_PLAYER_ELIMINATED = false;
    boolean D_PLAYER_ELIMINATED = false;

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

    int A_PLAYER_NUM_OF_STEPS = 0;
    int B_PLAYER_NUM_OF_STEPS = 0;
    int C_PLAYER_NUM_OF_STEPS = 0;
    int D_PLAYER_NUM_OF_STEPS = 0;

    int A_PLAYER_NUM_OF_COLLECTED_GOLD = 0;
    int B_PLAYER_NUM_OF_COLLECTED_GOLD = 0;
    int C_PLAYER_NUM_OF_COLLECTED_GOLD = 0;
    int D_PLAYER_NUM_OF_COLLECTED_GOLD = 0;


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
    FileWriter A_PLAYER_WRITE = new FileWriter("A_PLAYER.txt");
    FileWriter B_PLAYER_WRITE = new FileWriter("B_PLAYER.txt");
    FileWriter C_PLAYER_WRITE = new FileWriter("C_PLAYER.txt");
    FileWriter D_PLAYER_WRITE = new FileWriter("D_PLAYER.txt");


    Panel() throws IOException {
        String str1 = JOptionPane.showInputDialog("Boyutu Girin: ");
        int size = Integer.parseInt(str1);
        SIZE = size;
        System.out.println(SIZE);
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

        g.setColor(Color.GREEN);
        g.drawString("A NUM OF STEP: " + A_PLAYER_NUM_OF_STEPS, SCREEN_WIDTH / 4 * 0, SCREEN_HEIGHT + 65);
        g.drawString("B NUM OF STEP: " + B_PLAYER_NUM_OF_STEPS, SCREEN_WIDTH / 4 * 1, SCREEN_HEIGHT + 65);
        g.drawString("C NUM OF STEP: " + C_PLAYER_NUM_OF_STEPS, SCREEN_WIDTH / 4 * 2, SCREEN_HEIGHT + 65);
        g.drawString("D NUM OF STEP: " + D_PLAYER_NUM_OF_STEPS, SCREEN_WIDTH / 4 * 3, SCREEN_HEIGHT + 65);

        g.setColor(Color.BLUE);
        g.drawString("A COLLECTED GOLD: " + A_PLAYER_NUM_OF_COLLECTED_GOLD, SCREEN_WIDTH / 4 * 0, SCREEN_HEIGHT + 80);
        g.drawString("B COLLECTED GOLD: " + B_PLAYER_NUM_OF_COLLECTED_GOLD, SCREEN_WIDTH / 4 * 1, SCREEN_HEIGHT + 80);
        g.drawString("C COLLECTED GOLD: " + C_PLAYER_NUM_OF_COLLECTED_GOLD, SCREEN_WIDTH / 4 * 2, SCREEN_HEIGHT + 80);
        g.drawString("D COLLECTED GOLD: " + D_PLAYER_NUM_OF_COLLECTED_GOLD, SCREEN_WIDTH / 4 * 3, SCREEN_HEIGHT + 80);

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
        int earning = -1000000;
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
        int earning = -1000000;
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
                    int distanceToAPlayersTarget =
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


    public int[] FIND_TARGET_FOR_D_PLAYER() {
        int[] target = new int[2];
        int distance = 1000000;
        int earning = -1000000;
        boolean passThisTarget = false;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (COIN_MATRIX[i][j] != 0) {
                    int tmp = Math.abs((D_PLAYER_LOCATION_X - i)) + Math.abs((D_PLAYER_LOCATION_Y - j));
                    tmp = Math.abs(tmp);
                    int step;
                    if (tmp % MOVE_STEP == 0) {
                        step = tmp / MOVE_STEP;
                    } else {
                        step = (tmp / MOVE_STEP) + 1;
                    }

                    if (i == A_PLAYER_CURRENT_TARGET[0] && j == A_PLAYER_CURRENT_TARGET[1]) {
                        int tmpA = Math.abs((A_PLAYER_LOCATION_X - i)) + Math.abs((A_PLAYER_LOCATION_Y - j));
                        int stepA;
                        if (tmpA % MOVE_STEP == 0) {
                            stepA = tmpA / MOVE_STEP;
                        } else {
                            stepA = (tmpA / MOVE_STEP) + 1;
                        }
                        if (stepA > step) {
                            passThisTarget = true;
                            System.out.println("passed");
                        }


                    }
                    if (i == B_PLAYER_CURRENT_TARGET[0] && j == B_PLAYER_CURRENT_TARGET[1]) {
                        int tmpB = Math.abs((B_PLAYER_LOCATION_X - i)) + Math.abs((B_PLAYER_LOCATION_Y - j));
                        int stepB;
                        if (tmpB % MOVE_STEP == 0) {
                            stepB = tmpB / MOVE_STEP;
                        } else {
                            stepB = (tmpB / MOVE_STEP) + 1;
                        }
                        if (stepB > step) {
                            passThisTarget = true;
                            System.out.println("passed");
                        }


                    }
                    if (i == C_PLAYER_CURRENT_TARGET[0] && j == C_PLAYER_CURRENT_TARGET[1]) {
                        int tmpC = Math.abs((C_PLAYER_LOCATION_X - i)) + Math.abs((C_PLAYER_LOCATION_Y - j));
                        int stepC;
                        if (tmpC % MOVE_STEP == 0) {
                            stepC = tmpC / MOVE_STEP;
                        } else {
                            stepC = (tmpC / MOVE_STEP) + 1;
                        }
                        if (stepC > step) {
                            passThisTarget = true;
                            System.out.println("passed");
                        }


                    }


                    int tmp_earning = COIN_MATRIX[i][j] - (step * D_PLAYER_MOVE_COST);
                    if (tmp <= distance && tmp_earning >= earning && passThisTarget == false) {
                        distance = tmp;
                        earning = tmp_earning;
                        target[0] = i;
                        target[1] = j;
                    }
                }
            }
        }
        System.out.println("D player target i: " + target[0] + " j: " + target[1]);
        System.out.println("D Coint count " + COIN_MATRIX[target[0]][target[1]]);
        System.out.println("D Player Earning For Move: " + earning);


        return target;
    }

    public void MOVE_A() throws IOException {
        String moved = "";
        int mod = 0;


        if (A_PLAYER_CURRENT_TARGET[0] == 0 && A_PLAYER_CURRENT_TARGET[1] == 0) { //başlangıçta hedef belirleme
            A_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_A_PLAYER();
            A_PLAYER_COIN_COUNT = A_PLAYER_COIN_COUNT - A_PLAYER_TARGET_COST;
        }
        if (COIN_MATRIX[A_PLAYER_CURRENT_TARGET[0]][A_PLAYER_CURRENT_TARGET[1]] == 0) { //hedef başkası tarafından yendi ise yeniden hedef belirle
            A_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_A_PLAYER();
            //A_PLAYER_COIN_COUNT = A_PLAYER_COIN_COUNT - A_PLAYER_TARGET_COST;
            if (A_PLAYER_CURRENT_TARGET[0] == 0 && A_PLAYER_CURRENT_TARGET[1] == 0) {
                A_PLAYER_ELIMINATED = true;
            } else {
                A_PLAYER_COIN_COUNT = A_PLAYER_COIN_COUNT - A_PLAYER_TARGET_COST;
            }
        } // hareket ettirme kısmı
        if (A_PLAYER_LOCATION_X > A_PLAYER_CURRENT_TARGET[0] && A_PLAYER_ELIMINATED == false) {
            if (A_PLAYER_LOCATION_X - A_PLAYER_CURRENT_TARGET[0] >= MOVE_STEP && moved == "") {
                A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X - MOVE_STEP;
                moved = "true";
            } else {
                moved = "false";
                if (mod == 0) {
                    int tmp = A_PLAYER_LOCATION_X - A_PLAYER_CURRENT_TARGET[0];
                    A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X - (A_PLAYER_LOCATION_X - A_PLAYER_CURRENT_TARGET[0]);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= A_PLAYER_LOCATION_X - A_PLAYER_CURRENT_TARGET[0]) {
                    A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X - mod;
                } else if (A_PLAYER_LOCATION_X - A_PLAYER_CURRENT_TARGET[0] == 0) {
                    A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X - mod;
                } else {
                    A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X - (A_PLAYER_LOCATION_X - A_PLAYER_CURRENT_TARGET[0]);
                }

            }
        }
        if (A_PLAYER_CURRENT_TARGET[0] > A_PLAYER_LOCATION_X && A_PLAYER_ELIMINATED == false) {
            if (A_PLAYER_CURRENT_TARGET[0] - A_PLAYER_LOCATION_X >= MOVE_STEP && moved == "") {
                A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X + MOVE_STEP;
                moved = "true";
            } else {
                moved = "false";
                if (mod == 0) {
                    int tmp = A_PLAYER_CURRENT_TARGET[0] - A_PLAYER_LOCATION_X;
                    A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X + (A_PLAYER_CURRENT_TARGET[0] - A_PLAYER_LOCATION_X);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= A_PLAYER_CURRENT_TARGET[0] - A_PLAYER_LOCATION_X) {
                    A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X + mod;
                } else if (A_PLAYER_CURRENT_TARGET[0] - A_PLAYER_LOCATION_X == 0) {
                    A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X + mod;
                } else {
                    A_PLAYER_LOCATION_X = A_PLAYER_LOCATION_X - (A_PLAYER_CURRENT_TARGET[0] - A_PLAYER_LOCATION_X);
                }

            }
        }
        if (A_PLAYER_LOCATION_Y > A_PLAYER_CURRENT_TARGET[1] && moved != "true" && A_PLAYER_ELIMINATED == false) {
            if (A_PLAYER_LOCATION_Y - A_PLAYER_CURRENT_TARGET[1] >= MOVE_STEP && moved == "") {
                A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y - MOVE_STEP;
            } else {
                if (mod == 0) {
                    int tmp = A_PLAYER_LOCATION_Y - A_PLAYER_CURRENT_TARGET[1];
                    A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y - (A_PLAYER_LOCATION_Y - A_PLAYER_CURRENT_TARGET[1]);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= (A_PLAYER_LOCATION_Y - A_PLAYER_CURRENT_TARGET[1])) {
                    A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y - mod;
                } else if (A_PLAYER_LOCATION_Y - A_PLAYER_CURRENT_TARGET[1] == 0) {
                    A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y - mod;
                } else {
                    A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y - (A_PLAYER_LOCATION_Y - A_PLAYER_CURRENT_TARGET[1]);
                }

            }

        }
        if (A_PLAYER_CURRENT_TARGET[1] > A_PLAYER_LOCATION_Y && moved != "true" && A_PLAYER_ELIMINATED == false) {
            if (A_PLAYER_CURRENT_TARGET[1] - A_PLAYER_LOCATION_Y >= MOVE_STEP && moved == "") {
                A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y + MOVE_STEP;
            } else {
                if (mod == 0) {
                    int tmp = A_PLAYER_CURRENT_TARGET[1] - A_PLAYER_LOCATION_Y;
                    A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y + (A_PLAYER_CURRENT_TARGET[1] - A_PLAYER_LOCATION_Y);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= A_PLAYER_CURRENT_TARGET[1] - A_PLAYER_LOCATION_Y) {
                    A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y + mod;

                } else if (A_PLAYER_CURRENT_TARGET[1] - A_PLAYER_LOCATION_Y == 0) {
                    A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y + mod;
                } else {
                    A_PLAYER_LOCATION_Y = A_PLAYER_LOCATION_Y + (A_PLAYER_CURRENT_TARGET[1] - A_PLAYER_LOCATION_Y);
                }

            }
        }
        if (A_PLAYER_LOCATION_X == A_PLAYER_CURRENT_TARGET[0] && A_PLAYER_LOCATION_Y == A_PLAYER_CURRENT_TARGET[1]) { //hedefine ulaştı ise
            A_PLAYER_NUM_OF_COLLECTED_GOLD += COIN_MATRIX[A_PLAYER_LOCATION_X][A_PLAYER_LOCATION_Y];
            A_PLAYER_COIN_COUNT += COIN_MATRIX[A_PLAYER_LOCATION_X][A_PLAYER_LOCATION_Y];
            COIN_MATRIX[A_PLAYER_LOCATION_X][A_PLAYER_LOCATION_Y] = 0;

            A_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_A_PLAYER();
            if (A_PLAYER_CURRENT_TARGET[0] == 0 && A_PLAYER_CURRENT_TARGET[1] == 0) {
                A_PLAYER_ELIMINATED = true;
            } else {
                A_PLAYER_COIN_COUNT = A_PLAYER_COIN_COUNT - A_PLAYER_TARGET_COST;
            }

        }
        if (A_PLAYER_CURRENT_TARGET[0] == 0 && A_PLAYER_CURRENT_TARGET[1] == 0) {
            A_PLAYER_ELIMINATED = true;
        }
        if (A_PLAYER_ELIMINATED == false) {
            A_PLAYER_COIN_COUNT = A_PLAYER_COIN_COUNT - A_PLAYER_MOVE_COST;
            A_PLAYER_NUM_OF_STEPS++;
            A_PLAYER_WRITE.write("[" + A_PLAYER_LOCATION_X + "]" + "[" + A_PLAYER_LOCATION_Y + "]" + " -> ");

        }

        if (A_PLAYER_COIN_COUNT <= 0) {
            A_PLAYER_ELIMINATED = true;
            A_PLAYER_COIN_COUNT = 0;
        }

        //NEXT_PLAYER = "B";

    }

    public void MOVE_B() throws IOException {
        String moved = "";
        int mod = 0;


        if (B_PLAYER_CURRENT_TARGET[0] == 0 && B_PLAYER_CURRENT_TARGET[1] == 0) { //başlangıçta hedef belirleme
            B_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_B_PLAYER();
            B_PLAYER_COIN_COUNT = B_PLAYER_COIN_COUNT - B_PLAYER_TARGET_COST;
        }
        if (COIN_MATRIX[B_PLAYER_CURRENT_TARGET[0]][B_PLAYER_CURRENT_TARGET[1]] == 0) { //hedef başkası tarafından yendi ise yeniden hedef belirle
            B_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_B_PLAYER();

            if (B_PLAYER_CURRENT_TARGET[0] == 0 && B_PLAYER_CURRENT_TARGET[1] == 0) {
                B_PLAYER_ELIMINATED = true;
            } else {
                B_PLAYER_COIN_COUNT = B_PLAYER_COIN_COUNT - B_PLAYER_TARGET_COST;
            }
        } // hareket ettirme kısmı
        if (B_PLAYER_LOCATION_X > B_PLAYER_CURRENT_TARGET[0] && B_PLAYER_ELIMINATED == false) {
            if (B_PLAYER_LOCATION_X - B_PLAYER_CURRENT_TARGET[0] >= MOVE_STEP && moved == "") {
                B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X - MOVE_STEP;
                moved = "true";
            } else {
                moved = "false";
                if (mod == 0) {
                    int tmp = B_PLAYER_LOCATION_X - B_PLAYER_CURRENT_TARGET[0];
                    B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X - (B_PLAYER_LOCATION_X - B_PLAYER_CURRENT_TARGET[0]);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= B_PLAYER_LOCATION_X - B_PLAYER_CURRENT_TARGET[0]) {
                    B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X - mod;
                } else if (B_PLAYER_LOCATION_X - B_PLAYER_CURRENT_TARGET[0] == 0) {
                    B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X - mod;
                } else {
                    B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X - (B_PLAYER_LOCATION_X - B_PLAYER_CURRENT_TARGET[0]);
                }

            }
        }
        if (B_PLAYER_CURRENT_TARGET[0] > B_PLAYER_LOCATION_X && B_PLAYER_ELIMINATED == false) {
            if (B_PLAYER_CURRENT_TARGET[0] - B_PLAYER_LOCATION_X >= MOVE_STEP && moved == "") {
                B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X + MOVE_STEP;
                moved = "true";
            } else {
                moved = "false";
                if (mod == 0) {
                    int tmp = B_PLAYER_CURRENT_TARGET[0] - B_PLAYER_LOCATION_X;
                    B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X + (B_PLAYER_CURRENT_TARGET[0] - B_PLAYER_LOCATION_X);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= B_PLAYER_CURRENT_TARGET[0] - B_PLAYER_LOCATION_X) {
                    B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X + mod;
                } else if (B_PLAYER_CURRENT_TARGET[0] - B_PLAYER_LOCATION_X == 0) {
                    B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X + mod;
                } else {
                    B_PLAYER_LOCATION_X = B_PLAYER_LOCATION_X - (B_PLAYER_CURRENT_TARGET[0] - B_PLAYER_LOCATION_X);
                }

            }
        }
        if (B_PLAYER_LOCATION_Y > B_PLAYER_CURRENT_TARGET[1] && moved != "true" && B_PLAYER_ELIMINATED == false) {
            if (B_PLAYER_LOCATION_Y - B_PLAYER_CURRENT_TARGET[1] >= MOVE_STEP && moved == "") {
                B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y - MOVE_STEP;
            } else {
                if (mod == 0) {
                    int tmp = B_PLAYER_LOCATION_Y - B_PLAYER_CURRENT_TARGET[1];
                    B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y - (B_PLAYER_LOCATION_Y - B_PLAYER_CURRENT_TARGET[1]);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= (B_PLAYER_LOCATION_Y - B_PLAYER_CURRENT_TARGET[1])) {
                    B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y - mod;
                } else if (B_PLAYER_LOCATION_Y - B_PLAYER_CURRENT_TARGET[1] == 0) {
                    B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y - mod;
                } else {
                    B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y - (B_PLAYER_LOCATION_Y - B_PLAYER_CURRENT_TARGET[1]);
                }

            }

        }
        if (B_PLAYER_CURRENT_TARGET[1] > B_PLAYER_LOCATION_Y && moved != "true" && B_PLAYER_ELIMINATED == false) {
            if (B_PLAYER_CURRENT_TARGET[1] - B_PLAYER_LOCATION_Y >= MOVE_STEP && moved == "") {
                B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y + MOVE_STEP;
            } else {
                if (mod == 0) {
                    int tmp = B_PLAYER_CURRENT_TARGET[1] - B_PLAYER_LOCATION_Y;
                    B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y + (B_PLAYER_CURRENT_TARGET[1] - B_PLAYER_LOCATION_Y);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= B_PLAYER_CURRENT_TARGET[1] - B_PLAYER_LOCATION_Y) {
                    B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y + mod;

                } else if (B_PLAYER_CURRENT_TARGET[1] - B_PLAYER_LOCATION_Y == 0) {
                    B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y + mod;
                } else {
                    B_PLAYER_LOCATION_Y = B_PLAYER_LOCATION_Y + (B_PLAYER_CURRENT_TARGET[1] - B_PLAYER_LOCATION_Y);
                }

            }
        }
        if (B_PLAYER_LOCATION_X == B_PLAYER_CURRENT_TARGET[0] && B_PLAYER_LOCATION_Y == B_PLAYER_CURRENT_TARGET[1]) { //hedefine ulaştı ise
            B_PLAYER_NUM_OF_COLLECTED_GOLD += COIN_MATRIX[B_PLAYER_LOCATION_X][B_PLAYER_LOCATION_Y];
            B_PLAYER_COIN_COUNT += COIN_MATRIX[B_PLAYER_LOCATION_X][B_PLAYER_LOCATION_Y];
            COIN_MATRIX[B_PLAYER_LOCATION_X][B_PLAYER_LOCATION_Y] = 0;

            B_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_B_PLAYER();
            if (B_PLAYER_CURRENT_TARGET[0] == 0 && B_PLAYER_CURRENT_TARGET[1] == 0) {
                B_PLAYER_ELIMINATED = true;
            } else {
                B_PLAYER_COIN_COUNT = B_PLAYER_COIN_COUNT - B_PLAYER_TARGET_COST;
            }

        }
        if (B_PLAYER_CURRENT_TARGET[0] == 0 && B_PLAYER_CURRENT_TARGET[1] == 0) {
            B_PLAYER_ELIMINATED = true;
        }
        if (B_PLAYER_ELIMINATED == false) {
            B_PLAYER_COIN_COUNT = B_PLAYER_COIN_COUNT - B_PLAYER_MOVE_COST;
            B_PLAYER_NUM_OF_STEPS++;
            B_PLAYER_WRITE.write("[" + B_PLAYER_LOCATION_X + "]" + "[" + B_PLAYER_LOCATION_Y + "]" + " -> ");
        }

        if (B_PLAYER_COIN_COUNT <= 0) {
            B_PLAYER_ELIMINATED = true;
            B_PLAYER_COIN_COUNT = 0;
        }

        //NEXT_PLAYER = "C";
    }

    public void MOVE_C() throws IOException {
        String moved = "";
        int mod = 0;


        if (C_PLAYER_CURRENT_TARGET[0] == 0 && C_PLAYER_CURRENT_TARGET[1] == 0) { //başlangıçta hedef belirleme
            C_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_C_PLAYER();
            C_PLAYER_COIN_COUNT = C_PLAYER_COIN_COUNT - C_PLAYER_TARGET_COST;
        }
        if (COIN_MATRIX[C_PLAYER_CURRENT_TARGET[0]][C_PLAYER_CURRENT_TARGET[1]] == 0) { //hedef başkası tarafından yendi ise yeniden hedef belirle
            C_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_C_PLAYER();

            if (C_PLAYER_CURRENT_TARGET[0] == 0 && C_PLAYER_CURRENT_TARGET[1] == 0) {
                C_PLAYER_ELIMINATED = true;
            } else {
                C_PLAYER_COIN_COUNT = C_PLAYER_COIN_COUNT - C_PLAYER_TARGET_COST;
            }
        } // hareket ettirme kısmı
        if (C_PLAYER_LOCATION_X > C_PLAYER_CURRENT_TARGET[0] && C_PLAYER_ELIMINATED == false) {
            if (C_PLAYER_LOCATION_X - C_PLAYER_CURRENT_TARGET[0] >= MOVE_STEP && moved == "") {
                C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X - MOVE_STEP;
                moved = "true";
            } else {
                moved = "false";
                if (mod == 0) {
                    int tmp = C_PLAYER_LOCATION_X - C_PLAYER_CURRENT_TARGET[0];
                    C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X - (C_PLAYER_LOCATION_X - C_PLAYER_CURRENT_TARGET[0]);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= C_PLAYER_LOCATION_X - C_PLAYER_CURRENT_TARGET[0]) {
                    C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X - mod;
                } else if (C_PLAYER_LOCATION_X - C_PLAYER_CURRENT_TARGET[0] == 0) {
                    C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X - mod;
                } else {
                    C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X - (C_PLAYER_LOCATION_X - C_PLAYER_CURRENT_TARGET[0]);
                }

            }
        }
        if (C_PLAYER_CURRENT_TARGET[0] > C_PLAYER_LOCATION_X && C_PLAYER_ELIMINATED == false) {
            if (C_PLAYER_CURRENT_TARGET[0] - C_PLAYER_LOCATION_X >= MOVE_STEP && moved == "") {
                C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X + MOVE_STEP;
                moved = "true";
            } else {
                moved = "false";
                if (mod == 0) {
                    int tmp = C_PLAYER_CURRENT_TARGET[0] - C_PLAYER_LOCATION_X;
                    C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X + (C_PLAYER_CURRENT_TARGET[0] - C_PLAYER_LOCATION_X);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= C_PLAYER_CURRENT_TARGET[0] - C_PLAYER_LOCATION_X) {
                    C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X + mod;
                } else if (C_PLAYER_CURRENT_TARGET[0] - C_PLAYER_LOCATION_X == 0) {
                    C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X + mod;
                } else {
                    C_PLAYER_LOCATION_X = C_PLAYER_LOCATION_X - (C_PLAYER_CURRENT_TARGET[0] - C_PLAYER_LOCATION_X);
                }

            }
        }
        if (C_PLAYER_LOCATION_Y > C_PLAYER_CURRENT_TARGET[1] && moved != "true" && C_PLAYER_ELIMINATED == false) {
            if (C_PLAYER_LOCATION_Y - C_PLAYER_CURRENT_TARGET[1] >= MOVE_STEP && moved == "") {
                C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y - MOVE_STEP;
            } else {
                if (mod == 0) {
                    int tmp = C_PLAYER_LOCATION_Y - C_PLAYER_CURRENT_TARGET[1];
                    C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y - (C_PLAYER_LOCATION_Y - C_PLAYER_CURRENT_TARGET[1]);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= (C_PLAYER_LOCATION_Y - C_PLAYER_CURRENT_TARGET[1])) {
                    C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y - mod;
                } else if (C_PLAYER_LOCATION_Y - C_PLAYER_CURRENT_TARGET[1] == 0) {
                    C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y - mod;
                } else {
                    C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y - (C_PLAYER_LOCATION_Y - C_PLAYER_CURRENT_TARGET[1]);
                }

            }

        }
        if (C_PLAYER_CURRENT_TARGET[1] > C_PLAYER_LOCATION_Y && moved != "true" && C_PLAYER_ELIMINATED == false) {
            if (C_PLAYER_CURRENT_TARGET[1] - C_PLAYER_LOCATION_Y >= MOVE_STEP && moved == "") {
                C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y + MOVE_STEP;
            } else {
                if (mod == 0) {
                    int tmp = C_PLAYER_CURRENT_TARGET[1] - C_PLAYER_LOCATION_Y;
                    C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y + (C_PLAYER_CURRENT_TARGET[1] - C_PLAYER_LOCATION_Y);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= C_PLAYER_CURRENT_TARGET[1] - C_PLAYER_LOCATION_Y) {
                    C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y + mod;

                } else if (C_PLAYER_CURRENT_TARGET[1] - C_PLAYER_LOCATION_Y == 0) {
                    C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y + mod;
                } else {
                    C_PLAYER_LOCATION_Y = C_PLAYER_LOCATION_Y + (C_PLAYER_CURRENT_TARGET[1] - C_PLAYER_LOCATION_Y);
                }

            }
        }
        if (C_PLAYER_LOCATION_X == C_PLAYER_CURRENT_TARGET[0] && C_PLAYER_LOCATION_Y == C_PLAYER_CURRENT_TARGET[1]) { //hedefine ulaştı ise
            C_PLAYER_NUM_OF_COLLECTED_GOLD += COIN_MATRIX[C_PLAYER_LOCATION_X][C_PLAYER_LOCATION_Y];
            C_PLAYER_COIN_COUNT += COIN_MATRIX[C_PLAYER_LOCATION_X][C_PLAYER_LOCATION_Y];
            COIN_MATRIX[C_PLAYER_LOCATION_X][C_PLAYER_LOCATION_Y] = 0;

            C_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_C_PLAYER();
            if (C_PLAYER_CURRENT_TARGET[0] == 0 && C_PLAYER_CURRENT_TARGET[1] == 0) {
                C_PLAYER_ELIMINATED = true;
            } else {
                C_PLAYER_COIN_COUNT = C_PLAYER_COIN_COUNT - C_PLAYER_TARGET_COST;
            }

        }
        if (C_PLAYER_CURRENT_TARGET[0] == 0 && C_PLAYER_CURRENT_TARGET[1] == 0) {
            C_PLAYER_ELIMINATED = true;
        }
        if (C_PLAYER_ELIMINATED == false) {
            C_PLAYER_COIN_COUNT = C_PLAYER_COIN_COUNT - C_PLAYER_MOVE_COST;
            C_PLAYER_NUM_OF_STEPS++;
            C_PLAYER_WRITE.write("[" + C_PLAYER_LOCATION_X + "]" + "[" + C_PLAYER_LOCATION_Y + "]" + " -> ");
        }

        if (C_PLAYER_COIN_COUNT <= 0) {
            C_PLAYER_ELIMINATED = true;
            C_PLAYER_COIN_COUNT = 0;
        }

        //NEXT_PLAYER = "A";
    }

    public void MOVE_D() throws IOException {
        String moved = "";
        int mod = 0;


        if (D_PLAYER_CURRENT_TARGET[0] == 0 && D_PLAYER_CURRENT_TARGET[1] == 0) { //başlangıçta hedef belirleme
            D_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_D_PLAYER();
            D_PLAYER_COIN_COUNT = D_PLAYER_COIN_COUNT - D_PLAYER_TARGET_COST;
        }
        if (COIN_MATRIX[D_PLAYER_CURRENT_TARGET[0]][D_PLAYER_CURRENT_TARGET[1]] == 0) { //hedef başkası tarafından yendi ise yeniden hedef belirle
            D_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_D_PLAYER();

            if (D_PLAYER_CURRENT_TARGET[0] == 0 && D_PLAYER_CURRENT_TARGET[1] == 0) {
                D_PLAYER_ELIMINATED = true;
            } else {
                D_PLAYER_COIN_COUNT = D_PLAYER_COIN_COUNT - D_PLAYER_TARGET_COST;
            }
        } // hareket ettirme kısmı
        if (D_PLAYER_LOCATION_X > D_PLAYER_CURRENT_TARGET[0] && D_PLAYER_ELIMINATED == false) {
            if (D_PLAYER_LOCATION_X - D_PLAYER_CURRENT_TARGET[0] >= MOVE_STEP && moved == "") {
                D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X - MOVE_STEP;
                moved = "true";
            } else {
                moved = "false";
                if (mod == 0) {
                    int tmp = D_PLAYER_LOCATION_X - D_PLAYER_CURRENT_TARGET[0];
                    D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X - (D_PLAYER_LOCATION_X - D_PLAYER_CURRENT_TARGET[0]);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= D_PLAYER_LOCATION_X - D_PLAYER_CURRENT_TARGET[0]) {
                    D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X - mod;
                } else if (D_PLAYER_LOCATION_X - D_PLAYER_CURRENT_TARGET[0] == 0) {
                    D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X - mod;
                } else {
                    D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X - (D_PLAYER_LOCATION_X - D_PLAYER_CURRENT_TARGET[0]);
                }

            }
        }
        if (D_PLAYER_CURRENT_TARGET[0] > D_PLAYER_LOCATION_X && D_PLAYER_ELIMINATED == false) {
            if (D_PLAYER_CURRENT_TARGET[0] - D_PLAYER_LOCATION_X >= MOVE_STEP && moved == "") {
                D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X + MOVE_STEP;
                moved = "true";
            } else {
                moved = "false";
                if (mod == 0) {
                    int tmp = D_PLAYER_CURRENT_TARGET[0] - D_PLAYER_LOCATION_X;
                    D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X + (D_PLAYER_CURRENT_TARGET[0] - D_PLAYER_LOCATION_X);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= D_PLAYER_CURRENT_TARGET[0] - D_PLAYER_LOCATION_X) {
                    D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X + mod;
                } else if (D_PLAYER_CURRENT_TARGET[0] - D_PLAYER_LOCATION_X == 0) {
                    D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X + mod;
                } else {
                    D_PLAYER_LOCATION_X = D_PLAYER_LOCATION_X - (D_PLAYER_CURRENT_TARGET[0] - D_PLAYER_LOCATION_X);
                }

            }
        }
        if (D_PLAYER_LOCATION_Y > D_PLAYER_CURRENT_TARGET[1] && moved != "true" && D_PLAYER_ELIMINATED == false) {
            if (D_PLAYER_LOCATION_Y - D_PLAYER_CURRENT_TARGET[1] >= MOVE_STEP && moved == "") {
                D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y - MOVE_STEP;
            } else {
                if (mod == 0) {
                    int tmp = D_PLAYER_LOCATION_Y - D_PLAYER_CURRENT_TARGET[1];
                    D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y - (D_PLAYER_LOCATION_Y - D_PLAYER_CURRENT_TARGET[1]);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= (D_PLAYER_LOCATION_Y - D_PLAYER_CURRENT_TARGET[1])) {
                    D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y - mod;
                } else if (D_PLAYER_LOCATION_Y - D_PLAYER_CURRENT_TARGET[1] == 0) {
                    D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y - mod;
                } else {
                    D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y - (D_PLAYER_LOCATION_Y - D_PLAYER_CURRENT_TARGET[1]);
                }

            }

        }
        if (D_PLAYER_CURRENT_TARGET[1] > D_PLAYER_LOCATION_Y && moved != "true" && D_PLAYER_ELIMINATED == false) {
            if (D_PLAYER_CURRENT_TARGET[1] - D_PLAYER_LOCATION_Y >= MOVE_STEP && moved == "") {
                D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y + MOVE_STEP;
            } else {
                if (mod == 0) {
                    int tmp = D_PLAYER_CURRENT_TARGET[1] - D_PLAYER_LOCATION_Y;
                    D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y + (D_PLAYER_CURRENT_TARGET[1] - D_PLAYER_LOCATION_Y);
                    mod = MOVE_STEP - tmp;
                    System.out.println("mod: " + mod);
                } else if (mod <= D_PLAYER_CURRENT_TARGET[1] - D_PLAYER_LOCATION_Y) {
                    D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y + mod;

                } else if (D_PLAYER_CURRENT_TARGET[1] - D_PLAYER_LOCATION_Y == 0) {
                    D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y + mod;
                } else {
                    D_PLAYER_LOCATION_Y = D_PLAYER_LOCATION_Y + (D_PLAYER_CURRENT_TARGET[1] - D_PLAYER_LOCATION_Y);
                }

            }
        }
        if (D_PLAYER_LOCATION_X == D_PLAYER_CURRENT_TARGET[0] && D_PLAYER_LOCATION_Y == D_PLAYER_CURRENT_TARGET[1]) { //hedefine ulaştı ise
            D_PLAYER_NUM_OF_COLLECTED_GOLD += COIN_MATRIX[D_PLAYER_LOCATION_X][D_PLAYER_LOCATION_Y];
            D_PLAYER_COIN_COUNT += COIN_MATRIX[D_PLAYER_LOCATION_X][D_PLAYER_LOCATION_Y];
            COIN_MATRIX[D_PLAYER_LOCATION_X][D_PLAYER_LOCATION_Y] = 0;

            D_PLAYER_CURRENT_TARGET = FIND_TARGET_FOR_D_PLAYER();
            if (D_PLAYER_CURRENT_TARGET[0] == 0 && D_PLAYER_CURRENT_TARGET[1] == 0) {
                D_PLAYER_ELIMINATED = true;
            } else {
                D_PLAYER_COIN_COUNT = D_PLAYER_COIN_COUNT - D_PLAYER_TARGET_COST;
            }

        }
        if (D_PLAYER_CURRENT_TARGET[0] == 0 && D_PLAYER_CURRENT_TARGET[1] == 0) {
            D_PLAYER_ELIMINATED = true;
        }
        if (D_PLAYER_ELIMINATED == false) {
            D_PLAYER_COIN_COUNT = D_PLAYER_COIN_COUNT - D_PLAYER_MOVE_COST;
            D_PLAYER_NUM_OF_STEPS++;
            D_PLAYER_WRITE.write("[" + D_PLAYER_LOCATION_X + "]" + "[" + D_PLAYER_LOCATION_Y + "]" + " -> ");
        }

        if (D_PLAYER_COIN_COUNT <= 0) {
            D_PLAYER_ELIMINATED = true;
            D_PLAYER_COIN_COUNT = 0;
        }

        //NEXT_PLAYER = "A";
    }

    public void PLAY() throws IOException {
        if (NEXT_PLAYER == "A") {
            if (A_PLAYER_ELIMINATED == false) {
                MOVE_A();
            } else {
                A_PLAYER_WRITE.close();
            }
            NEXT_PLAYER = "B";

        } else if (NEXT_PLAYER == "B") {
            if (B_PLAYER_ELIMINATED == false) {
                MOVE_B();
            }
            else {
                B_PLAYER_WRITE.close();
            }
            NEXT_PLAYER = "C";

        } else if (NEXT_PLAYER == "C") {
            if (C_PLAYER_ELIMINATED == false) {
                MOVE_C();

            }
            else {
                C_PLAYER_WRITE.close();
            }
            NEXT_PLAYER = "D";

        } else if (NEXT_PLAYER == "D") {
            if (D_PLAYER_ELIMINATED == false) {
                MOVE_D();
            }
            else {
                D_PLAYER_WRITE.close();
            }
            NEXT_PLAYER = "A";
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
                    //FIND_TARGET_FOR_C_PLAYER();
                    //MOVE_A();

                    try {
                        PLAY();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    repaint();
                    //FIND_TARGET_FOR_A_PLAYER();
                    //FIND_TARGET_FOR_B_PLAYER();
                    //randomCoins();
                    //repaint();
                    break;


            }
        }
    }

}
