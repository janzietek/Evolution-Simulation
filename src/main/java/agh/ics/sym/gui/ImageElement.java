package agh.ics.sym.gui;


import agh.ics.sym.engine.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
