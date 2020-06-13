
public class Face {
    _Point point_array[];
    float z_index, d;

    public Face(){
        this.z_index = 0;
        this.d = 0;
    }


    public Face(_Point[] arr){
        this.point_array = new _Point[arr.length];
        for (int i = 0; i < arr.length; i++)
            point_array[i] = arr[i];

        this.z_index = 0;
        this.d = 0;
    }

    public Face(Face f){
        this.point_array = f.point_array;
        this.z_index = f.z_index;
        d = f.d;
    }



}
