package simple_soccer_lib.perception;

import simple_soccer_lib.utils.Vector2D;

public class ObjectPerception {
	private Vector2D position;
	//private Vector2D direction; //direcao do movimento -- para versões futuras

	public ObjectPerception(Vector2D position){
		this.position = position;
	}
	
	public ObjectPerception(){		
	}
	
	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}
	
}