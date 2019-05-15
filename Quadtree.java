
public class Quadtree {

	public Quadtree nw = null;
	public Quadtree ne = null;
	public Quadtree sw = null;
	public Quadtree se = null;

	public int xp, yp, sidesize;
	int color;
	int[][] image;

	/**
	 * Quadtree creates a region quadtree for the specified data.
	 * 
	 * @param imageData
	 *            - a two-dimensional array of integers representing a grayscale
	 *            image. Quadtree assumes that the width and height of imageData are
	 *            the same. (That is that the image is square.)
	 */
	public Quadtree(int[][] imageData) {
		image = imageData;

	}

	public Quadtree(int xp, int yp, int sidesize, int color) {

		this.xp = xp;
		this.yp = yp;
		this.sidesize = sidesize;
		this.color = color;
	}

	public void setchildren(Quadtree nw, Quadtree ne, Quadtree sw, Quadtree se) {
		this.nw = nw;
		this.ne = ne;
		this.sw = sw;
		this.se = se;

	}

	/**
	 * @return the height of the quadtree
	 */
	public int getHeight() {
		return Math.max(Math.max(height_help(nw), height_help(ne)), Math.max(height_help(sw), height_help(se)));

	}

	public int height_help(Quadtree tree) {
		if (tree == null) {
			return 0;
		}
		return Math.max(Math.max(height_help(tree.nw), height_help(tree.ne)),
				Math.max(height_help(tree.sw), height_help(tree.se))) + 1;
	}

	/**
	 * Gets the value of a pixel in the image represented by the Quadtree. (0,0) is
	 * the upper left-hand corner of the image.
	 * 
	 * @param x
	 * @param y
	 * @return the pixel value for coordinate (x,y) of the image (stored on the quad
	 *         tree)
	 */
	public int getPixel(int x, int y) {

		if (sidesize == 1) {
			return this.color;
		}
		if (nw == null) {
			return this.color;
		}

		if (x < xp + sidesize / 2) {
			if (y < yp + sidesize / 2) {
				return nw.getPixel(x, y);
			} else {
				return ne.getPixel(x, y);
			}
		} else {
			if (y < yp + sidesize / 2) {
				return sw.getPixel(x, y);
			} else {
				return se.getPixel(x, y);
			}
		}

	}

	public void split(int x, int y, int val) {
		if (sidesize == 1) {
			this.color = val;
			return;
		}
		nw = new Quadtree(xp, y, sidesize / 2, this.color);
		ne = new Quadtree(xp, yp + sidesize / 2, sidesize / 2, this.color);
		sw = new Quadtree(xp + sidesize / 2, y, sidesize / 2, this.color);
		se = new Quadtree(xp + sidesize / 2, yp + sidesize / 2, sidesize / 2, this.color);

		if (x < xp + sidesize / 2) {
			if (y < yp + sidesize / 2) {

				nw.split(x, y, val);
			} else {
				ne.split(x, y, val);
			}
		} else {
			if (y < yp + sidesize / 2) {
				sw.split(x, y, val);
			} else {
				se.split(x, y, val);
			}
		}
	}

	/**
	 * Sets the value of a pixel in the image represented by the Quadtree. (0,0) is
	 * the upper left-hand corner of the image. Modifies the Quadtree to represent
	 * an image with the pixel as (x,y) set to the value val. Whenever possible,
	 * Quadtree should avoid reconstructing the entire tree from scratch.
	 * 
	 * @param x
	 * @param y
	 * @param val
	 */
	public void setPixel(int x, int y, int val) {
		if (sidesize == 1) {
			this.color = val;
			return;
		}
		if (nw == null && this.color != val) {
			split(x, y, val);
			return;
		}

		if (x < xp + sidesize / 2) {
			if (y < yp + sidesize / 2) {
				nw.setPixel(x, y, val);
			} else {
				ne.setPixel(x, y, val);
			}
		} else {
			if (y < yp + sidesize / 2) {
				sw.setPixel(x, y, val);
			} else {
				se.setPixel(x, y, val);
			}
		}
	}

	/**
	 * image() should return an integer array of the values represented by the
	 * Quadtree. If set pixel hasn't been called the array should contain the same
	 * values as those passed into the tree's constructor.
	 * 
	 * @return the image value represented by the Quadtree.
	 */
	public int[][] image() {
		
		

		int[][] result = new int[sidesize][sidesize];
		if(sidesize == 1) {
			result[0][0] = this.color;
			return result;
		}
		if(nw == null) {
			help_array_fill(result, this.color);
			return result;
		}
		
		if(sidesize == 2) {
			result[0][0] = nw.color;
			result[0][1] = ne.color;
			result[1][0] = sw.color;
			result[1][1] = se.color;
			return result;
		}
		
		
		
		for(int i=0; i<sidesize; i++) {
			for(int j=0; j<sidesize; j++) {
//				result[i][j] 
				if(i < sidesize/2) {
					if(j<sidesize/2) {
						
						help_array(result, nw.image(), 0, 0);
					}else {
						help_array(result, ne.image(), 0, sidesize/2);
					}
				}else {
					if(j<sidesize/2) {
						help_array(result, sw.image(), sidesize/2, 0);
					}else {
						help_array(result, se.image(), sidesize/2, sidesize/2);
					}
				}
			}
		}
		return result;
	}
	
	public static void help_array_fill(int[][]result, int sub) {
		for(int i=0; i<result.length; i++) {
			for(int j=0; j<result.length; j++) {
				result[i][j] = sub;
			}
		}
	}
	
	public static void help_array(int[][]result, int[][] sub, int xstart, int ystart) {
		for(int i=xstart; i<xstart+sub.length; i++) {
			for(int j=ystart; j<ystart+sub[0].length; j++) {
				result[i][j] = sub[i-xstart][j-ystart];
			}
		}
	}

	/**
	 * Gets the average value for the pixels for a region in the image represented
	 * by the Quadtree. The region is defined by the upper left-hand corner of the
	 * region (x,y) and a width and height.
	 *
	 * @param x
	 *            - x value of the upper left-hand corner of the region.
	 * @param y
	 *            - y value of the upper left-hand corner of the region.
	 * @param width
	 *            - the width of the region
	 * @param height
	 *            - the height of the region
	 * @return the average pixel value for the region.
	 */
	public float getAverageValue(int x, int y, int width, int height) {
		int total = 0;
		int num = 0;
		for(int i=x; i<x+width; i++) {
			for(int j=y; j<y+height; j++) {
				total = total + getPixel(i, j);
				num = num +1;
			}
		}
		
		return total / num;
	}
}
