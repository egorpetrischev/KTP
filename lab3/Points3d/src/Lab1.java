import java.util.Scanner;

public class Lab1 {
    public static void main(String[] args) {
        Point3d[] points = new Point3d[3];

        input(points);

        if (isTriangle(points))
            System.out.println("Площадь заданного треугольника = " + computeArea(points));
    }

    // Input triangle points
    public static void input(Point3d[] points){
        Scanner sc = new Scanner(System.in);

        System.out.println("Введите координаты трех точек треугольника\n");
        for (int i = 1; i <= 3; i++){
            double x, y, z;

            System.out.println(i + " точка:");

            System.out.print("x = ");
            x = sc.nextDouble();
            System.out.print("y = ");
            y = sc.nextDouble();
            System.out.print("z = ");
            z = sc.nextDouble();
            System.out.println();

            points[i-1] = new Point3d(x, y, z);
        }
    }

    // Check if triangle exists
    public static boolean isTriangle(Point3d[] points){

        int point1, point2;

        if (points[0].equals(points[1])){
            point1 = 1;
            point2 = 2;
        } else if (points[0].equals(points[2])) {
            point1 = 1;
            point2 = 3;
        } else if (points[1].equals(points[2])) {
            point1 = 2;
            point2 = 3;
        } else {
            return true;
        }

        System.out.println("Треугольник не существует, так как точки " + point1 + " и " + point2 + " совпадают");
        return false;
    }

    // Counts square using Herons formula
    public static double computeArea(Point3d[] points){
        double side1, side2, side3, halfPerimeter;

        side1 = points[0].distanceTo(points[1]);
        side2 = points[0].distanceTo(points[2]);
        side3 = points[1].distanceTo(points[2]);
        halfPerimeter = (side1 + side2 + side3) / 2;

        return Math.sqrt(halfPerimeter * (halfPerimeter - side1) * (halfPerimeter - side2) * (halfPerimeter - side3));
    }
}