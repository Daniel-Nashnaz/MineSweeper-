package GUI;

import java.awt.Image;
import javax.swing.ImageIcon;

public class GameImage {

    private Integer width;
    private Integer height;
    private Integer level;
    private Image[] imageArr;
    private final Integer SIZE = 11;

    public GameImage(Integer level) {
        this.imageArr = new Image[SIZE];
        this.level = level;
        sizeUpdate();
        insertImage();
    }

    private void insertImage() {
        for (int i = 0; i < SIZE; i++) {
            this.imageArr[i] = (new ImageIcon(getClass().getResource("/GUI/newpackage/images/" + i + ".png"))).getImage();
        }
    }

    private void sizeUpdate() {
        if (level.equals(5)) {
            this.width = 150;
            this.height = 140;
            return;
        }
        if (level.equals(10)) {
            this.width = 72;
            this.height = 68;
            return;
        }
        this.width = 33;
        this.height = 30;
    }

    public ImageIcon setImage(int num) {
        if (num == -2 || num == -3) {
            num = 9;
        }
        Image image = imageArr[num].getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);

    }

}
