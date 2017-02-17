package simple_soccer_lib.utils;


public class Vector2D {
	private double x;
	private double y;
	
	public Vector2D(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(){
		
	}
	
	public Vector2D(double degreeAngle) {
		double radAngle = (degreeAngle * Math.PI) / 180.0d;
		this.x = Math.cos(radAngle);
		this.y = Math.sin(radAngle);
	}

	public void normalizeIn() {
		double length = magnitude();
		if (length != 0) {
			this.x = this.x/length;
			this.y = this.y/length;
		}
	}
	
	public Vector2D normalize(double magnitude) {
		Vector2D v2 = new Vector2D();
		double length = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
		if (length != 0) {
			v2.x = this.x/length;
			v2.y = this.y/length;
		}
		return v2;
	}
	
	public double normIn(){
		return Math.sqrt(Math.pow(this.getX(),2)+Math.pow(this.getY(),2));
	}
	
	public void multiplyIn(double d) {
		throw new Error(); // FAZER!
	}
	
	public void sumIn(Vector2D other) {
		this.x = this.x + other.getX();
		this.y = this.y + other.getY();	
	}
	
	public Vector2D sum(Vector2D other) {
		Vector2D v2 = new Vector2D(this.x + other.getX(), this.y + other.getY());
	    return v2;
	}
	
	public void subIn(Vector2D other) {
		this.x = this.x - other.getX();
		this.y = this.y - other.getY();
	}

	public Vector2D sub(Vector2D other) {
	 	return new Vector2D(this.getX() - other.getX(), this.getY() - other.getY());
	}

	public double magnitude() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	public Vector2D rotate(double angle) {
		double x = (this.x * Math.cos(angle)) - (this.y * Math.sin(angle));
		double y = (this.x * Math.sin(angle)) + (this.y * Math.cos(angle));
		Vector2D v2 = new Vector2D(x,y);
		return v2;
	}
	
	public void rotateIn(double angle) {
		this.x = (this.x * Math.cos(angle)) - (this.y * Math.sin(angle));
		this.y = (this.x * Math.sin(angle)) + (this.y * Math.cos(angle));
	}
	
	public static double distance(Vector2D vector1, Vector2D vector2){
		return Math.sqrt(Math.pow(vector1.getX()-vector2.getX(),2)+Math.pow(vector1.getY()-vector2.getY(),2));
	}
	
	double distanceIn(Vector2D other) {
		return Math.sqrt(Math.pow(this.x-other.getX(),2)+Math.pow(this.y-other.getY(),2));
	}
	
	double distanceSquared(Vector2D other) { //para comparar (e.g. achar o mais perto), não precisa tirar a raiz quadrada
		return -1;
	}
	
	public void scaleIn(double scale){
		this.x*=scale;
		this.y*=scale;
	}
	
	public Vector2D scale(double scale){
		return new Vector2D(this.x*=scale,this.y*=scale);
	}	
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public static Vector2D[] intersections(Vector2D one, Vector2D two, double done, double dtwo){
		Vector2D[] vectors = null;
		double d = distance(one,two);
		
		if(d>(done+dtwo)) return vectors;
		else if(d<(done-dtwo) ||d<(done-dtwo)*-1) return vectors;
		
		double a = (Math.pow(done,2)-Math.pow(dtwo,2)+Math.pow(d,2))/(2*d);
		double h = Math.sqrt(Math.pow(done,2)-Math.pow(a,2));
		Vector2D three = one.sum(two.sub(one).scale(a/d));
		
		double x1 = three.getX() + h*(two.getY() - one.getY())/d;
		double y1 = three.getY() - h*(two.getX() - one.getX())/d;
		
		double x2 = three.getX() - h*(two.getY() - one.getY())/d;
		double y2 = three.getY() + h*(two.getX() - one.getX())/d;
		
		vectors = new Vector2D[2];
		vectors[0] = new Vector2D(x1,y1); 
		vectors[1] = new Vector2D(x2,y2); 
		
		return vectors;
	}
	
	public static Vector2D[] searchInY(Vector2D one,double distance, double y){
		Vector2D[] vectors = new Vector2D[2];
		double c = Math.pow(one.getY()-y, 2)-Math.pow(distance, 2)-Math.pow(one.getX(), 2);
		double x1 = one.getX()+Math.sqrt(Math.pow(one.getX(),2)-c);
		double x2 = one.getX()-Math.sqrt(Math.pow(one.getX(),2)-c);
		vectors[0] = new Vector2D(x1,y);
		vectors[1] = new Vector2D(x2,y);
		return vectors;
	}
	
	public static Vector2D[] searchInX(Vector2D one,double distance, double x){
		Vector2D[] vectors = new Vector2D[2];
		double c = Math.pow(one.getX()-x, 2)-Math.pow(distance, 2)-Math.pow(one.getY(), 2);
		double y1 = one.getY()+Math.sqrt(Math.pow(one.getY(),2)-c);
		double y2 = one.getY()-Math.sqrt(Math.pow(one.getY(),2)-c);
		vectors[0] = new Vector2D(x,y1);
		vectors[1] = new Vector2D(x,y2);
		return vectors;
	}
	
	public static Vector2D newVector(Vector2D one, double distance, double angle){
		double dx = Math.sin(angle)*distance;
		double dy = Math.cos(angle)*distance;
		Vector2D vector = new Vector2D(dx,dy);
		return vector;
	}
	
//	public static double angle(Vector2D one, Vector2D two){
//		double a = (one.getX()*two.getX())+(one.getY()*two.getY());
//		double cos = a/(one.normIn()*two.normIn());
//		//System.out.println("Cos = " + cos);
//		double angle = Math.acos(cos) * 180.0d / Math.PI;
//		return angle;
//	}
		
	//angle of v2 relative to v1 = atan2(v2.y,v2.x) - atan2(v1.y,v1.x)
	//http://www.euclideanspace.com/maths/algebra/vectors/angleBetween/
	public double angleFrom(Vector2D reference) {
		double radAngle = Math.atan2(this.y, this.x) - Math.atan2(reference.y, reference.x);
		return (radAngle * 180.0d) / Math.PI;
	}

	public String toString() {
		return "Vector2D [x=" + x + ", y=" + y + "]";
	}
}
