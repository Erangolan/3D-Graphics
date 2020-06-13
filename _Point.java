
public class _Point {
    private float x, y, z;

    public _Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public _Point() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public _Point(_Point point){
        this.x = point.getX();
        this.y = point.getY();
        this.z = point.getZ();
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void setZ(float z){
        this.z = z;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getZ(){
        return this.z;
    }
}


