
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.function.IntPredicate;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageReader {

	/**
	 * Reads in a image file, converts the image to grayscale, and returns a two
	 * dimensional array of integers. Most common image file types are supported
	 * (the exact list of supported types is system specific).
	 * 
	 * @param filename
	 *            - filename of image.
	 * @return - two dimensional integer array.
	 * @throws IOException
	 */
	public static int[][] imageToGrayscale(String filename) throws IOException {
		return imageToGrayscale(ImageIO.read(new File(filename)));
	}

	/**
	 * Takes a buffered image, converts it to grayscale, and returns and a two
	 * dimensional integer array. Grayscale conversion uses "luma" calculations.
	 * (See http://www.tannerhelland.com/3643/grayscale-image-algorithm-vb6/)
	 * 
	 * @param img
	 *            - source image
	 * @return - two dimensional integer array.
	 */
	protected static int[][] imageToGrayscale(BufferedImage img) {
		int dim = (int) Math.max(img.getHeight(), img.getWidth());
		int[][] result = new int[dim][dim];
		for (int x = 0; x < img.getWidth(); ++x)
			for (int y = 0; y < img.getHeight(); ++y) {
				int rgb = img.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);

				// Normalize and gamma correct:
				double rr = Math.pow(r / 255.0, 2.2);
				double gg = Math.pow(g / 255.0, 2.2);
				double bb = Math.pow(b / 255.0, 2.2);

				// Calculate luminance:
				double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;

				// Gamma
				int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));

				result[x][y] = grayLevel;
				// System.out.println("x: "+x+" y : "+y);

			}
		return result;
	}

	/**
	 * resizeToPowerOfTwo takes a square 2-d array of ints and returns of a copy of
	 * the array padded with black pixels on theon the right and bottom so that the
	 * sides of the square are a power of two. (This it to ease the construction of
	 * quadtrees)
	 * 
	 * @param origImageData
	 * @return
	 */
	public static int[][] resizeToPowerOfTwo(int[][] origImageData) {
		// pads image to make it sqaure and a power of two
		int size = 1;
		while ((size < origImageData.length) || (size < origImageData[0].length)) {
			size *= 2;
		}
		int[][] newImage = new int[size][size];
		for (int i = 0; i < origImageData.length; i++) {
			for (int j = 0; j < origImageData[0].length; j++) {
				newImage[i][j] = origImageData[i][j];
			}
		}
		return newImage;

	}

	/**
	 * Takes a two dimensional integer array and returns a grayscale BufferedImage
	 * 
	 * @param grayVals
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage grayscaleToImage(int[][] grayVals) {
		BufferedImage img = new BufferedImage(grayVals.length, grayVals[0].length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < img.getWidth(); ++x) {
			for (int y = 0; y < img.getHeight(); ++y) {
				int grayLevel = grayVals[x][y];
				int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
				img.setRGB(x, y, gray);
			}
		}
		return img;
	}

	/**
	 * Takes a two dimensional integer array and displays it a grayscale Image in a
	 * JFrame.
	 * 
	 * @param img
	 * @throws IOException
	 */
	public static void displayImage(int[][] img) {
		displayImage(grayscaleToImage(img));

	}

	/**
	 * Displays an Image in a JFrame
	 * 
	 * @param img
	 */
	public static void displayImage(Image img) {
		ImageIcon icon = new ImageIcon(img);
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
		JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void help(int[][]result, int[][] sub, int xstart, int ystart) {
		for(int i=xstart; i<xstart+sub.length; i++) {
			for(int j=ystart; j<ystart+sub[0].length; j++) {
				result[i][j] = sub[i-xstart][j-ystart];
			}
		}
	}
	public static int[][] create0(int s) {
		int[][] arr = new int[s][s];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				arr[i][j] = 0;
			}
		}
		return arr;
	}
	
	public static void main(String[] args) throws IOException {
//		test();
		test1();
		
		

	}
	
	public static void test1() throws IOException {
		Quad quad1 = new Quad();
		int[][] intArrayImg = imageToGrayscale("testImage.png");

		BufferedImage bufferedImage = grayscaleToImage(intArrayImg);
		
		Quadtree tree1 = new Quadtree(intArrayImg);
		tree1 = quad1.makeQuadTreee(0, 0, intArrayImg.length, intArrayImg);
		System.out.println("height: " + tree1.getHeight());
		print(intArrayImg);
		check(intArrayImg, tree1);
		System.out.println("original value: "+tree1.getPixel(0,0));
//		tree1.setPixel(0,0, 88);
		print(tree1.image());
		System.out.println("original value: "+tree1.getPixel(0,0));
		System.out.println(tree1.getAverageValue(8, 0, 8, 8));
	}
	
	public static void test() {
		Quad quad = new Quad();

		int[][] arr = create(8);
		System.out.println("curr array:");
		print(arr);
		System.out.println("-------------------------------");
		Quadtree tree = new Quadtree(arr);
		tree = quad.makeQuadTreee(0, 0, arr.length, arr);
		System.out.println("overall height: " + tree.getHeight());
		
		check(arr, tree);
		System.out.println(tree.getAverageValue(0, 0, 8, 8));
//		
		System.out.println("original value: "+tree.getPixel(4, 5));
		tree.setPixel(4, 5, 88);
		System.out.println("original value: "+tree.getPixel(4, 5));
		print(tree.image());
		
		
	}
	
	public static void check(int[][]arr, Quadtree tree) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				int num1 = arr[i][j] ;
				int num2 = tree.getPixel(i, j);
				if(num1 != num2) {
					System.out.println("num1: "+num1+" num2 : "+num2+" index: "+i+", "+j);
				}
			}
		}
		System.out.println("get pixel all pass");
	}

	public static int[][] create(int s) {
		int[][] arr = new int[s][s];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				arr[i][j] = new Random().nextInt(9);
			}
		}
		return arr;
	}

	public static void print(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				int num = arr[i][j];

				System.out.print(num + " ");
			}
			System.out.println();
		}
	}

}