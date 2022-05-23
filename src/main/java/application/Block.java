package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
    Rectangle a;
    Rectangle b;
    Rectangle c;
    Rectangle d;
    Color color;
    private String name;
    public int orientation = 1;

    public Block(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Block(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String name) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;

        switch (name) {
            case "j" -> color = Color.SLATEGRAY;
            case "l" -> color = Color.DARKGOLDENROD;
            case "o" -> color = Color.INDIANRED;
            case "s" -> color = Color.FORESTGREEN;
            case "t" -> color = Color.CADETBLUE;
            case "z" -> color = Color.HOTPINK;
            case "i" -> color = Color.SANDYBROWN;
        }
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }


    public String getName() { return name; }


    public void changeForm() {
        if (orientation != 4) orientation++;
        else orientation = 1;
    }
}