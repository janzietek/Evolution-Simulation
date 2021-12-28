package agh.ics.sym.gui;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageElement {
    public Image image;

    public ImageElement (String filename) throws FileNotFoundException {

        try {
            image = new Image(new FileInputStream(filename));
        }
        catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }
}
