import java.util.ArrayList;
import java.util.List;

public class Quad {

	public Quadtree makeQuadTreee(int x, int y, int sideSize, int[][] image) {
		if (sideSize <= 1) {
			return new Quadtree(x, y, sideSize, image[x][y]);
		}

		Quadtree nw = makeQuadTreee(x, y, sideSize / 2, image);
		Quadtree sw = makeQuadTreee(x + sideSize / 2, y, sideSize / 2, image);
		Quadtree ne = makeQuadTreee(x, y + sideSize / 2, sideSize / 2, image);
		Quadtree se = makeQuadTreee(x + sideSize / 2, y + sideSize / 2, sideSize / 2, image);
		if (nw.color != -1 && nw.color == ne.color && ne.color == sw.color && sw.color == se.color) {
			Quadtree atree = new Quadtree(x, y, sideSize, nw.color);
			return atree;
		} else {
			Quadtree newtree = new Quadtree(x, y, sideSize, -1);
			newtree.nw = nw;
			newtree.sw = sw;
			newtree.ne = ne;
			newtree.se = se;

			return newtree;
		}
		
		

	}
	
	
	public static void main(String[] args) {
		List<List<String>> list = new ArrayList<>();
		List<String> sub = new ArrayList<>();
		sub.add("hh");
		sub.add("hhh2");
		list.add(sub);
		for (List subb : list) {
			System.out.println(subb.get(0));
		}
		
	}

	

}
