package edu.caltech.cs2.lab02;

import edu.caltech.cs2.libraries.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Image {
    private Pixel[][] pixels;

    public Image(File imageFile) throws IOException {
        BufferedImage img = ImageIO.read(imageFile);
        this.pixels = new Pixel[img.getWidth()][img.getHeight()];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                this.pixels[i][j] = Pixel.fromInt(img.getRGB(i, j));
            }
        }
    }

    private Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Image transpose() {
        // To declare a two-dimensional int array with $$a$$ rows and $$b$$ columns:
        Pixel[][] array = new Pixel[this.pixels[0].length][this.pixels.length];

// To loop through our array, we use two nested for loops:
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                array[j][i] = pixels[i][j];

            }
        }

        return new Image(array);
    }

    public String decodeText() {
        int counter = 0;
        int num = 0;
        String message = "";
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                num += (Math.pow(2, counter)) * (pixels[i][j].getLowestBitOfR());
                counter ++;
                if(counter == 8) {
                    if(num != 0) {
                        System.out.println(num);
                        System.out.println(Integer.toBinaryString(num));
                        char c = (char)num;
                        message += c;
                    }
                    counter = 0;
                    num = 0;
                }
            }
        }
        return message;
    }

    public Image hideText(String text) {
        Pixel[][] array = new Pixel[this.pixels.length][this.pixels[0].length];
        ArrayList<Integer> binList = new ArrayList<>();


        for (int i = 0; i < text.length(); i++) {
            char charInt = text.charAt(i);
            String bin = Integer.toBinaryString(charInt);
            while (bin.length() < 8) {
                bin = "0" + bin;
            }
            for (int j = 0; j < bin.length(); j++) {
                int single = 0;
                if (bin.charAt(7 - j) == '1') {
                    single = 1;
                } else {
                    single = 0;
                }

                binList.add(single);
            }
        }

        int count = 0;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (count < binList.size()) {
                    array[i][j] = pixels[i][j].fixLowestBitOfR(binList.get(count));
                    count ++;
                } else {
                    array[i][j] = pixels[i][j].fixLowestBitOfR(0);
                }
            }
        }


        return new Image(array);
    }

    public BufferedImage toBufferedImage() {
        BufferedImage b = new BufferedImage(this.pixels.length, this.pixels[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < this.pixels.length; i++) {
            for (int j = 0; j < this.pixels[0].length; j++) {
                b.setRGB(i, j, this.pixels[i][j].toInt());
            }
        }
        return b;
    }

    public void save(String filename) {
        File out = new File(filename);
        try {
            ImageIO.write(this.toBufferedImage(), filename.substring(filename.lastIndexOf(".") + 1, filename.length()), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
