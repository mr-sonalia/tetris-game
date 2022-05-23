package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Tetris extends Application {
    // The variables
    public static final int MOVE = 25;
    public static final int SIZE = 25;
    public static int XMAX = SIZE * 20;
    public static int YMAX = SIZE * 24;
    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];
    private static Pane group = new Pane();
    private static Block object;
    private static Scene scene = new Scene(group, XMAX + 150, YMAX);
    public static int score = 0;
    private static int top = 0;
    private static boolean game = true;
    private static Block nextObj = Controller.makeRect();
    private static int linesNo = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene.setFill(Color.WHITESMOKE);

        for (int[] a: MESH) {
            Arrays.fill(a, 0);
        }

        Line line = new Line(XMAX, 0, XMAX, YMAX);
        line.setFill(Color.BLACK);

        Text scoreText = new Text("Score: ");
        scoreText.setFont(Font.font("Space Grotesk, Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        scoreText.setFill(Color.FORESTGREEN);
        scoreText.setY(100);
        scoreText.setX(XMAX + 10);

        Text levelsClearedText = new Text("Levels cleared: ");
        levelsClearedText.setFont(Font.font("Space Grotesk, Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 16));
        levelsClearedText.setY(140);
        levelsClearedText.setX(XMAX + 10);
        levelsClearedText.setFill(Color.DARKCYAN);

        Text instructions = new Text("Instructions");
        instructions.setFont(Font.font("Space Grotesk, Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 16));
        instructions.setFill(Color.BLACK);
        instructions.setY(300);
        instructions.setX(XMAX + 10);

        Text movements = new Text("Left ←\nRight →\nRotate ⎵\nFall ↓\n");
        movements.setFont(Font.font("Space Grotesk, Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        movements.setFill(Color.BLACK);
        movements.setY(320);
        movements.setX(XMAX + 10);

        group.getChildren().addAll(scoreText, line, levelsClearedText, instructions, movements);

        Block block = nextObj;
        group.getChildren().addAll(block.a, block.b, block.c, block.d);
        moveOnKeyPress(block);
        object = block;
        nextObj = Controller.makeRect();
        stage.setScene(scene);
        stage.setTitle("TETRIS");
        stage.show();

        Timer fall = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0 ||
                                object.d.getY() == 0)
                            top++;
                        else
                            top = 0;

                        if (top == 2) {
                            // GAME OVER
                            Text over = new Text("GAME OVER");
                            over.setFill(Color.RED);
                            over.setStyle("-fx-font: 70 arial;");
                            over.setY(250);
                            over.setX(10);
                            group.getChildren().add(over);
                            game = false;
                        }
                        // Exit
                        if (top == 15) {
                            System.exit(0);
                        }

                        if (game) {
                            MoveDown(object);
                            scoreText.setText("Score: " + Integer.toString(score));
                            levelsClearedText.setText("Levels cleared: " + Integer.toString(linesNo));
                        }
                    }
                });
            }
        };
        fall.schedule(task, 0, 300);
    }

    private void moveOnKeyPress(Block block) {
        scene.setOnKeyPressed(new EventHandler <KeyEvent> () {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case RIGHT -> Controller.MoveRight(block);
                    case DOWN -> {
                        MoveDown(block);
                        score++;
                    }
                    case LEFT -> Controller.MoveLeft(block);
                    case SPACE -> MoveTurn(block);
                }
            }
        });
    }

    private void MoveTurn(Block block) {
        int f = block.orientation;
        Rectangle a = block.a;
        Rectangle b = block.b;
        Rectangle c = block.c;
        Rectangle d = block.d;
        switch (block.getName()) {
            case "j":
                if (f == 1 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
                    MoveRight(block.a);
                    MoveDown(block.a);
                    MoveDown(block.c);
                    MoveLeft(block.c);
                    MoveDown(block.d);
                    MoveDown(block.d);
                    MoveLeft(block.d);
                    MoveLeft(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, -2, 2)) {
                    MoveDown(block.a);
                    MoveLeft(block.a);
                    MoveLeft(block.c);
                    MoveUp(block.c);
                    MoveLeft(block.d);
                    MoveLeft(block.d);
                    MoveUp(block.d);
                    MoveUp(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, 1) && cB(c, 1, 1) && cB(d, 2, 2)) {
                    MoveLeft(block.a);
                    MoveUp(block.a);
                    MoveUp(block.c);
                    MoveRight(block.c);
                    MoveUp(block.d);
                    MoveUp(block.d);
                    MoveRight(block.d);
                    MoveRight(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 2, -2)) {
                    MoveUp(block.a);
                    MoveRight(block.a);
                    MoveRight(block.c);
                    MoveDown(block.c);
                    MoveRight(block.d);
                    MoveRight(block.d);
                    MoveDown(block.d);
                    MoveDown(block.d);
                    block.changeForm();
                    break;
                }
                break;
            case "l":
                if (f == 1 && cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 2)) {
                    MoveRight(block.a);
                    MoveDown(block.a);
                    MoveUp(block.c);
                    MoveRight(block.c);
                    MoveUp(block.b);
                    MoveUp(block.b);
                    MoveRight(block.b);
                    MoveRight(block.b);
                    block.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -1, -1) && cB(b, 2, -2) && cB(c, 1, -1)) {
                    MoveDown(block.a);
                    MoveLeft(block.a);
                    MoveRight(block.b);
                    MoveRight(block.b);
                    MoveDown(block.b);
                    MoveDown(block.b);
                    MoveRight(block.c);
                    MoveDown(block.c);
                    block.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, 1) && cB(c, -1, -1) && cB(b, -2, -2)) {
                    MoveLeft(block.a);
                    MoveUp(block.a);
                    MoveDown(block.c);
                    MoveLeft(block.c);
                    MoveDown(block.b);
                    MoveDown(block.b);
                    MoveLeft(block.b);
                    MoveLeft(block.b);
                    block.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(b, -2, 2) && cB(c, -1, 1)) {
                    MoveUp(block.a);
                    MoveRight(block.a);
                    MoveLeft(block.b);
                    MoveLeft(block.b);
                    MoveUp(block.b);
                    MoveUp(block.b);
                    MoveLeft(block.c);
                    MoveUp(block.c);
                    block.changeForm();
                    break;
                }
                break;
            case "o":
                break;
            case "s":
                if (f == 1 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveDown(block.a);
                    MoveLeft(block.a);
                    MoveLeft(block.c);
                    MoveUp(block.c);
                    MoveUp(block.d);
                    MoveUp(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 2 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                    MoveUp(block.a);
                    MoveRight(block.a);
                    MoveRight(block.c);
                    MoveDown(block.c);
                    MoveDown(block.d);
                    MoveDown(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveDown(block.a);
                    MoveLeft(block.a);
                    MoveLeft(block.c);
                    MoveUp(block.c);
                    MoveUp(block.d);
                    MoveUp(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                    MoveUp(block.a);
                    MoveRight(block.a);
                    MoveRight(block.c);
                    MoveDown(block.c);
                    MoveDown(block.d);
                    MoveDown(block.d);
                    block.changeForm();
                    break;
                }
                break;
            case "t":
                if (f == 1 && cB(a, 1, 1) && cB(d, -1, -1) && cB(c, -1, 1)) {
                    MoveUp(block.a);
                    MoveRight(block.a);
                    MoveDown(block.d);
                    MoveLeft(block.d);
                    MoveLeft(block.c);
                    MoveUp(block.c);
                    block.changeForm();
                    break;
                }
                if (f == 2 && cB(a, 1, -1) && cB(d, -1, 1) && cB(c, 1, 1)) {
                    MoveRight(block.a);
                    MoveDown(block.a);
                    MoveLeft(block.d);
                    MoveUp(block.d);
                    MoveUp(block.c);
                    MoveRight(block.c);
                    block.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, -1) && cB(d, 1, 1) && cB(c, 1, -1)) {
                    MoveDown(block.a);
                    MoveLeft(block.a);
                    MoveUp(block.d);
                    MoveRight(block.d);
                    MoveRight(block.c);
                    MoveDown(block.c);
                    block.changeForm();
                    break;
                }
                if (f == 4 && cB(a, -1, 1) && cB(d, 1, -1) && cB(c, -1, -1)) {
                    MoveLeft(block.a);
                    MoveUp(block.a);
                    MoveRight(block.d);
                    MoveDown(block.d);
                    MoveDown(block.c);
                    MoveLeft(block.c);
                    block.changeForm();
                    break;
                }
                break;
            case "z":
                if (f == 1 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveUp(block.b);
                    MoveRight(block.b);
                    MoveLeft(block.c);
                    MoveUp(block.c);
                    MoveLeft(block.d);
                    MoveLeft(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 2 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                    MoveDown(block.b);
                    MoveLeft(block.b);
                    MoveRight(block.c);
                    MoveDown(block.c);
                    MoveRight(block.d);
                    MoveRight(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 3 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveUp(block.b);
                    MoveRight(block.b);
                    MoveLeft(block.c);
                    MoveUp(block.c);
                    MoveLeft(block.d);
                    MoveLeft(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 4 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                    MoveDown(block.b);
                    MoveLeft(block.b);
                    MoveRight(block.c);
                    MoveDown(block.c);
                    MoveRight(block.d);
                    MoveRight(block.d);
                    block.changeForm();
                    break;
                }
                break;
            case "i":
                if (f == 1 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                    MoveUp(block.a);
                    MoveUp(block.a);
                    MoveRight(block.a);
                    MoveRight(block.a);
                    MoveUp(block.b);
                    MoveRight(block.b);
                    MoveDown(block.d);
                    MoveLeft(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
                    MoveDown(block.a);
                    MoveDown(block.a);
                    MoveLeft(block.a);
                    MoveLeft(block.a);
                    MoveDown(block.b);
                    MoveLeft(block.b);
                    MoveUp(block.d);
                    MoveRight(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 3 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                    MoveUp(block.a);
                    MoveUp(block.a);
                    MoveRight(block.a);
                    MoveRight(block.a);
                    MoveUp(block.b);
                    MoveRight(block.b);
                    MoveDown(block.d);
                    MoveLeft(block.d);
                    block.changeForm();
                    break;
                }
                if (f == 4 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
                    MoveDown(block.a);
                    MoveDown(block.a);
                    MoveLeft(block.a);
                    MoveLeft(block.a);
                    MoveDown(block.b);
                    MoveLeft(block.b);
                    MoveUp(block.d);
                    MoveRight(block.d);
                    block.changeForm();
                    break;
                }
                break;
        }
    }

    private void RemoveRows(Pane pane) {
        ArrayList < Node > rectangles = new ArrayList < Node > ();
        ArrayList < Integer > lines = new ArrayList < Integer > ();
        ArrayList < Node > newRectangle = new ArrayList < Node > ();
        int full = 0;
        for (int i = 0; i < MESH[0].length; i++) {
            for (int[] mesh : MESH) {
                if (mesh[i] == 1)
                    full++;
            }
            if (full == MESH.length)
                lines.add(i);
            //lines.add(i + lines.size());
            full = 0;
        }
        if (lines.size() > 0)
            do {
                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rectangles.add(node);
                }
                score += 50;
                linesNo++;

                for (Node node: rectangles) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newRectangle.add(node);
                }

                for (Node node: newRectangle) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }
                }
                lines.remove(0);
                rectangles.clear();
                newRectangle.clear();
                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rectangles.add(node);
                }
                for (Node node: rectangles) {
                    Rectangle a = (Rectangle) node;
                    try {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                }
                rectangles.clear();
            } while (lines.size() > 0);
    }

    private void MoveDown(Rectangle rect) {
        if (rect.getY() + MOVE < YMAX)
            rect.setY(rect.getY() + MOVE);

    }

    private void MoveRight(Rectangle rect) {
        if (rect.getX() + MOVE <= XMAX - SIZE)
            rect.setX(rect.getX() + MOVE);
    }

    private void MoveLeft(Rectangle rect) {
        if (rect.getX() - MOVE >= 0)
            rect.setX(rect.getX() - MOVE);
    }

    private void MoveUp(Rectangle rect) {
        if (rect.getY() - MOVE > 0)
            rect.setY(rect.getY() - MOVE);
    }

    private void MoveDown(Block block) {
        if (block.a.getY() == YMAX - SIZE || block.b.getY() == YMAX - SIZE || block.c.getY() == YMAX - SIZE ||
                block.d.getY() == YMAX - SIZE || moveA(block) || moveB(block) || moveC(block) || moveD(block)) {
            MESH[(int) block.a.getX() / SIZE][(int) block.a.getY() / SIZE] = 1;
            MESH[(int) block.b.getX() / SIZE][(int) block.b.getY() / SIZE] = 1;
            MESH[(int) block.c.getX() / SIZE][(int) block.c.getY() / SIZE] = 1;
            MESH[(int) block.d.getX() / SIZE][(int) block.d.getY() / SIZE] = 1;
            RemoveRows(group);

            Block a = nextObj;
            nextObj = Controller.makeRect();
            object = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d);
            moveOnKeyPress(a);
        }

        if (block.a.getY() + MOVE < YMAX && block.b.getY() + MOVE < YMAX && block.c.getY() + MOVE < YMAX &&
                block.d.getY() + MOVE < YMAX) {
            int movea = MESH[(int) block.a.getX() / SIZE][((int) block.a.getY() / SIZE) + 1];
            int moveb = MESH[(int) block.b.getX() / SIZE][((int) block.b.getY() / SIZE) + 1];
            int movec = MESH[(int) block.c.getX() / SIZE][((int) block.c.getY() / SIZE) + 1];
            int moved = MESH[(int) block.d.getX() / SIZE][((int) block.d.getY() / SIZE) + 1];
            if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
                block.a.setY(block.a.getY() + MOVE);
                block.b.setY(block.b.getY() + MOVE);
                block.c.setY(block.c.getY() + MOVE);
                block.d.setY(block.d.getY() + MOVE);
            }
        }
    }

    private boolean moveA(Block block) {
        return (MESH[(int) block.a.getX() / SIZE][((int) block.a.getY() / SIZE) + 1] == 1);
    }

    private boolean moveB(Block block) {
        return (MESH[(int) block.b.getX() / SIZE][((int) block.b.getY() / SIZE) + 1] == 1);
    }

    private boolean moveC(Block block) {
        return (MESH[(int) block.c.getX() / SIZE][((int) block.c.getY() / SIZE) + 1] == 1);
    }

    private boolean moveD(Block block) {
        return (MESH[(int) block.d.getX() / SIZE][((int) block.d.getY() / SIZE) + 1] == 1);
    }

    private boolean cB(Rectangle rect, int x, int y) {
        boolean xb = false;
        boolean yb = false;
        if (x >= 0)
            xb = rect.getX() + x * MOVE <= XMAX - SIZE;
        if (x < 0)
            xb = rect.getX() + x * MOVE >= 0;
        if (y >= 0)
            yb = rect.getY() - y * MOVE > 0;
        if (y < 0)
            yb = rect.getY() + y * MOVE < YMAX;
        return xb && yb && MESH[((int) rect.getX() / SIZE) + x][((int) rect.getY() / SIZE) - y] == 0;
    }

}