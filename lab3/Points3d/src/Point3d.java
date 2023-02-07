public class Point3d {
    private double x;
    private double y;
    private double z;

    public Point3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Default constructor
    public Point3d() {
        new Point3d(0.0, 0.0, 0.0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    // Check if two points are equal
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Point3d point = (Point3d) o;
        return this.getX() == point.getX()
                && this.getY() == point.getY()
                && this.getZ() == point.getZ();
    }

    // Counts the distance between two points
    public double distanceTo(Point3d point){
        double x1, x2, y1, y2, z1, z2;

        x1 = this.getX();
        y1 = this.getY();
        z1 = this.getZ();

        x2 = point.getX();
        y2 = point.getY();
        z2 = point.getZ();

        return Math.sqrt(Math.pow((x2 - x1), 2)
                + Math.pow((y2 - y1), 2)
                + Math.pow((z2 - z1), 2));
    }
}
