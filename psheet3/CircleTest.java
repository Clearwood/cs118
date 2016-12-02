/** Problem Sheet 3, Question 3.
  *
  * Create a CircleTest program.
  * The main method should create an array of 100 Circles, each with a random origin (between [0,0] and [100,100])
  * and a random radius (between 0.0 and 100.0).
  *
  * Calculate the mean of the areas and output:
  *    "Area Avg: X"   (where X is the calculated mean area)
  * Calculate the standard deviation of the areas and output:
  *    "Area Stdev: X" (where X is the calculated stddev)
  * Find the index of the circle with the most intersections with other circles and output:
  *    "Circle X has Y intersections" (where X is the calculated circle and Y is the number of intersections)
  *
  * As before ensure your output is formatted exactly as above.
  * Example Output:
  *
  * Area Avg: 50.23135
  * Area Stdev: 4.45345
  * Circle 53 has 4 intersections
  *
  */
class CircleTest{

    public static void main(String[] args){
        Circle[] array = new Circle[100];
        double area = 0.0;
        for(int i=0; i<100;i++){
            double ranX = Math.random()*100;
            double ranY = Math.random()*100;
            double ranRadius = Math.random()*100;
            Point tmp = new Point(ranX,ranY);
            array[i]= new Circle(tmp, ranRadius);
            area += array[i].getArea();
        }
        double areaAvg = area/100;
        System.out.println("Area Avg:" + areaAvg);
        double dev = 0.0;
        int absolute=0;
        int num = 0;
        for(int i=0; i<100; i++){
            dev += Math.pow(Math.abs(areaAvg-array[i].getArea()),2);
            int tmp = 0;
            for(int j=0;j<100;j++){
                if(array[i].intersect(array[j])){
                    tmp++;
                }
            }
            if(tmp>absolute){
                absolute=tmp;
                num = i;
            }
        }
        double Stddev = Math.sqrt(dev/100);
        System.out.println("Area Stdev: "+ Stddev);
        System.out.println("Circle "+num+" has "+absolute+" intersections");
    }


}
