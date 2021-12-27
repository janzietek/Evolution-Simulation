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

    private final ImageElement[] images;

    public ImagesManager(double gridSize)
    {
        images = new ImageElement[16];

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

            images[12] = new ImageElement("src/main/resources/bar_0.png");
            images[13] = new ImageElement("src/main/resources/bar_1.png");
            images[14] = new ImageElement("src/main/resources/bar_2.png");
            images[15] = new ImageElement("src/main/resources/bar_3.png");
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e);
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

    public ImageView GetBarImageView(IMapElement el, double size, int moveEnergy)
    {
        int nIndex = 12; // bush

        if(el.getClass() == Animal.class)
        {
            Animal an = (Animal)el;

            if(an.energy > 20 * moveEnergy)
            {
                nIndex = 15;
            }
            else if(an.energy > 10 * moveEnergy)
            {
                nIndex = 14;
            }
            else if(an.energy > 5 * moveEnergy)
            {
                nIndex = 13;
            }
        }

        ImageView imageView = new ImageView(images[nIndex].image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        return imageView;
    }
}