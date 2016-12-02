/** Problem Sheet 3, Question 2.
  *
  * Using your answer from Q1, create another class, Circle.
  * It should store circles, where each circle has a radius and an origin Point.
  *
  * Write methods to calculate the circumference and the area of the circle, called: getCircumference and getArea.
  *
  * Write a method to determine whether the any part of two circles intersect, called intersect.
  * Your function should return a true or false value, and take only 1 input parameter (the other circle)
  * You may assume that a circle enclosed entirely within another circle counts as an intersection.
  *
  * Finally write an equals method to determine if two circles are identical (i.e. share both an origin and a radius).
  */
class Circle{
    private Point origin;
    private double radius;
    public Circle(){
        origin = new Point();
        radius = 0.0;
    }
    public Circle(Point p, double radius){
        origin=p;
        this.radius=radius;
    }
    public double getCircumference(){
        return Math.PI * 2 * radius;
    }
    public double getArea(){
        return Math.PI * (radius * radius);
    }
    public boolean intersect(Circle c){
        double radDif = Math.abs(radius - c.radius);
        double sqrt = Math.sqrt(Math.pow((origin.getX()-c.origin.getX()),2)+Math.pow((origin.getY()-c.origin.getY()),2));
        double radSum = radius+c.radius;
        if(radDif <= sqrt && sqrt <= radSum){
            return true;
        }
        return false;
    }
    public boolean equals(Circle c){
        if(origin.equals(c.origin)){
            if(radius == c.radius){
                return true;
            }
        }
            return false;
    }
}