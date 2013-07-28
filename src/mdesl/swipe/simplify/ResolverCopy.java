package mdesl.swipe.simplify;

import mdesl.swipe.SwipeResolver;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ResolverCopy implements SwipeResolver {
	
	@Override
	public void resolve(Array<Vector2> input, Array<Vector2> output) {
		output.clear(); 
		output.addAll(input);
	}

}
