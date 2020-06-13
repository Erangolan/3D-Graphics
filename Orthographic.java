import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Orthographic extends Canvas {

    public void Cavalier(Functions func, int angle_box, _Point localTotal[], List<Face> Array_face, Application app){
        func.center_paint();
        total_to_local(func.total, localTotal);
        local_to_Vertices(func.Vertices, localTotal);

        for (_Point t: func.Vertices){
            t.x += t.z * (float)Math.cos(angle_box);
            t.y += t.z * (float)Math.sin(angle_box);
        }

        set_visability(func, Array_face);
        app.draw_polygon();
        local_to_total(func, localTotal);
    }

    public void Cabinet(Functions func, int angle_box,  _Point localTotal[], List<Face> Array_face, Application app){
        func.center_paint();
        total_to_local(func.total, localTotal);
        local_to_Vertices(func.Vertices, localTotal);

        for (_Point t: func.Vertices){
            t.x = t.x + 0.5f * (t.z * (float)Math.cos(angle_box));
            t.y = t.y + 0.5f * (t.z * (float)Math.sin(angle_box));
        }

        set_visability(func, Array_face);
        app.draw_polygon();
        local_to_total(func, localTotal);
    }

    public void Perspective(Functions func, int distance_box,  _Point localTotal[], List<Face> Array_face, Application app){
        func.center_paint();
        total_to_local(func.total, localTotal);
        local_to_Vertices(func.Vertices, localTotal);

        for (_Point t: func.Vertices){
            t.x += (t.z / distance_box);
            t.y += (t.z / distance_box);
        }

        set_visability(func, Array_face);
        app.draw_polygon();
        local_to_total(func, localTotal);
    }

    public void total_to_local(List<_Point> total, _Point localTotal[]){
        for (int i = 0; i < localTotal.length; i++){
            localTotal[i].setX(total.get(i).getX());
            localTotal[i].setY(total.get(i).getY());
            localTotal[i].setZ(total.get(i).getZ());
        }
    }

    public void local_to_Vertices(List<_Point> Vertices, _Point localTotal[]){
        for (int i = 0; i < localTotal.length; i++){
            Vertices.get(i).setX(localTotal[i].getX());
            Vertices.get(i).setY(localTotal[i].getY());
            Vertices.get(i).setZ(localTotal[i].getZ());
        }
    }


    public void local_to_total(Functions func, _Point localTotal[]){
        for (int i = 0; i < func.total.size(); i++){
            func.total.get(i).setX(localTotal[i].getX());
            func.total.get(i).setY(localTotal[i].getY());
            func.total.get(i).setZ(localTotal[i].getZ());
        }
    }

    public void total_to_vertices(Functions func){
        for (int i = 0; i < func.total.size(); i++){
            func.Vertices.get(i).setX(func.total.get(i).getX());
            func.Vertices.get(i).setY(func.total.get(i).getY());
            func.Vertices.get(i).setZ(func.total.get(i).getZ());
        }
    }


    // calculate if face is visible
    public void set_visability(Functions func,List<Face> Array_face){
        for (Face f: Array_face){
            f.d = func.visibility(f.point_array[0].getX(), f.point_array[0].getY(), f.point_array[0].getZ(),
                    f.point_array[1].getX(), f.point_array[1].getY(), f.point_array[1].getZ(),
                    f.point_array[2].getX(), f.point_array[2].getY(), f.point_array[2].getZ());

            if (f.point_array.length == 3){
                Stream.of(f.point_array[0].getZ(), f.point_array[1].getZ(), f.point_array[2].getZ()).
                        max(Comparator.naturalOrder())
                        .ifPresent(maxInt -> f.z_index = maxInt);
            }

            if (f.point_array.length == 4){
                Stream.of(f.point_array[0].getZ(), f.point_array[1].getZ(), f.point_array[2].getZ(), f.point_array[3].getZ()).
                        max(Comparator.naturalOrder())
                        .ifPresent(maxInt -> f.z_index = maxInt);
            }
        }
        sortByZ_index(Array_face);
    }

    public void sortByZ_index(List<Face> Array_face){
        for (int i = 0; i < Array_face.size() - 1; i++){
            for (int j =0; j < Array_face.size() - i - 1; j++){
                if (Array_face.get(j).z_index > Array_face.get(j + 1).z_index){
                    Face tmp = new Face(Array_face.get(j));
                    Array_face.set(j, Array_face.get(j + 1));
                    Array_face.set(j + 1, tmp);
                }
            }
        }
    }
}
