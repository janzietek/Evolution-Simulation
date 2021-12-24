package agh.ics.sym.gui;

import agh.ics.sym.engine.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
//    public VBox gMapElement;
//
//    public GuiElementBox (IMapElement mapElement) throws FileNotFoundException {
//        try {
//            Image image = new Image(new FileInputStream(mapElement.getPath()));
//            ImageView imageView = new ImageView(image);
//            imageView.setFitWidth(20);
//            imageView.setFitHeight(20);
//
//            VBox container = new VBox();
//            container.setAlignment(Pos.CENTER);
//            container.getChildren().add(imageView);
//            container.getChildren().add(new javafx.scene.control.Label(mapElement.toString()));
//            this.gMapElement = container;
//        }
//        catch (FileNotFoundException ex) {
//            System.out.println(ex);
//        }
//    }
}