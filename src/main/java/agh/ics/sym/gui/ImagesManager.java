package agh.ics.sym.gui;


import agh.ics.sym.engine.Animal;
import agh.ics.sym.engine.IMapElement;
import agh.ics.sym.engine.MapDirection;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImagesManager {

    private ImageElement[] images;
    private double gridSize = 20;

    public ImagesManager(double gridSize)
    {
        this.gridSize = gridSize;
        images = new ImageElement[12];

        try {
            images[0] = new ImageElement("src/main/resources/jungle.png");
            images[1] = new ImageElement("src/main/resources/savanna.png");

            images[2] = new ImageElement("src/main/resources/bush.png");
            images[3] = new ImageElement("src/main/resources/cactus.png");

            images[4] = new ImageElement("src/main/resources/dog_dir_0.png");
            images[5] = new ImageElement("src/main/resources/dog_dir_1.png");
            images[6] = new ImageElement("src/main/resources/dog_dir_2.png");
            images[7] = new ImageElement("src/main/resources/dog_dir_3.png");
            images[8] = new ImageElement("src/main/resources/dog_dir_4.png");
            images[9] = new ImageElement("src/main/resources/dog_dir_5.png");
            images[10] = new ImageElement("src/main/resources/dog_dir_6.png");
            images[11] = new ImageElement("src/main/resources/dog_dir_7.png");
        }
        catch(FileNotFoundException e)
        {

        }
    }

    public ImageView GetImageViewByIndex(int i, double size)
    {
        ImageView imageView = new ImageView(images[i].image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        return imageView;
    }

    public ImageView GetImageView(IMapElement el, double size)
    {
        int nIndex = 3; // bush

        if(el.getClass() == Animal.class)
        {
            Animal an = (Animal)el;
            nIndex = switch (an.getOrientation()) {
                case NORTH -> 4;
                case NORTHEAST -> 5;
                case EAST -> 6;
                case SOUTHEAST -> 7;
                case SOUTH -> 8;
                case SOUTHWEST -> 9;
                case WEST -> 10;
                case NORTHWEST -> 11;
            };
        }

        ImageView imageView = new ImageView(images[nIndex].image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        return imageView;
    }
}
