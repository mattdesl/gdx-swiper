package mdesl.swipe.simplify;

import mdesl.swipe.SwipeResolver;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


//Events that trigger a "slash"
//- Reached max number of points; slash stops and fades out
//- Delay from touchDown is too long

public class ResolverRadialChaikin implements SwipeResolver {
	
	private Array<Vector2> tmp = new Array<Vector2>(Vector2.class);
	
	public static int iterations = 2;
	public static float simplifyTolerance = 35f;
	
	public void resolve(Array<Vector2> input, Array<Vector2> output) {
		output.clear();
		if (input.size<=2) { //simple copy
			output.addAll(input);
			return;
		}

		//simplify with squared tolerance
		if (simplifyTolerance>0 && input.size>3) {
			simplify(input, simplifyTolerance * simplifyTolerance, tmp);
			input = tmp;
		}
		
		//perform smooth operations
		if (iterations<=0) { //no smooth, just copy input to output
			output.addAll(input);
		} else if (iterations==1) { //1 iteration, smooth to output
			smooth(input, output);
		} else { //multiple iterations.. ping-pong between arrays
			int iters = iterations;
			//subsequent iterations
			do {
				smooth(input, output);
				tmp.clear();
				tmp.addAll(output);
				Array<Vector2> old = output;
				input = tmp;
				output = old;
			} while (--iters > 0);
		}
	}
	
	public static void smooth(Array<Vector2> input, Array<Vector2> output) {
		//expected size
		output.clear();
		output.ensureCapacity(input.size*2);
		
		//first element
		output.add(input.get(0));
		//average elements
		for (int i=0; i<input.size-1; i++) {
			Vector2 p0 = input.get(i);
			Vector2 p1 = input.get(i+1);
	
			Vector2 Q = new Vector2(0.75f * p0.x + 0.25f * p1.x, 0.75f * p0.y + 0.25f * p1.y);
			Vector2 R = new Vector2(0.25f * p0.x + 0.75f * p1.x, 0.25f * p0.y + 0.75f * p1.y);
	        output.add(Q);
	        output.add(R);
		}
		
		//last element
		output.add(input.get(input.size-1));
	}
	
	//simple distance-based simplification
	//adapted from simplify.js
	public static void simplify(Array<Vector2> points, float sqTolerance, Array<Vector2> out) {
		int len = points.size;

		Vector2 point = new Vector2();
		Vector2 prevPoint = points.get(0);
		
		out.clear();
		out.add(prevPoint);
		
		for (int i = 1; i < len; i++) {
			point = points.get(i);
			if (distSq(point, prevPoint) > sqTolerance) {
				out.add(point);
				prevPoint = point;
			}
		}
		if (!prevPoint.equals(point)) {
			out.add(point);
		}
	}
	
	public static float distSq(Vector2 p1, Vector2 p2) {
		float dx = p1.x - p2.x, dy = p1.y - p2.y;
		return dx * dx + dy * dy;
	}

}

