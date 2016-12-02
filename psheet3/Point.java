/** Problem Sheet 3, Question 1.
  *
  * Write a short implementation for a Point class with the following operations:
  *
  * public double getX()                        // get the x value
  * public double getY()                        // get the y value
  * public void movePoint(double x, double y)   // move the point by x and y, relative to current position
  * public void setPoint(double x, double y)    // set the point to x and y
  * public String toString()                    // write out the value of the point in the form "(x, y)"
  * public boolean equals(Point p)              // compare if two points are equal
  *
  * Your class should have 2 constructors, one taking an x and y value, the other setting default values to 0.0.
  *
  * Additionally, a method called "distance" that returns the euclidian distance between itself and another point,
  * passed as a parameter.
  *
  * Finally, a method called "findMidpoint" that returns a point that is exactly halfway between itself and another point.
  * i.e.,
  * Point p1 = new Point(0.0,0.0);
  * Point p2 = new Point(4.0,4.0);
  * Point p3 = p1.findMidpoint(p2);
  *
  * Your Point class should fit this specification exactly.
  */
class Point {
    private double x;
    private double y;
    public Point(){
        this.x = 0.0;
        this.y = 0.0;
    }
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void setPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void movePoint(double x, double y){
        this.x = this.x + x;
        this.y = this.y + y;
    }
    public double distance(Point p){
        double resultX = Math.abs(x-p.getX());
        double resultY = Math.abs (y - p.getY());
        double distance = Math.sqrt((resultY)*(resultY) +(resultX)*(resultX));
        return distance;
    }
    public Point findMidpoint(Point p){
        double rX = (x + p.getX())/2;
        double rY = (y + p.getY())/2;
        Point result = new Point(rX,rY);
        return result;

    }
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public String toString(){
        return "("+x+", "+y+")";
    }
    public boolean equals(Point p){
        if(x == p.getX()){
            if(y == p.getY()){
                return true;
            }
        }
        return false;
    }
}
