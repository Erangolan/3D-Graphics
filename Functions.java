import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

public class Functions {
    String path;
    public _Point center_Paint;
    public float maxX, minX, maxY, minY, maxZ, minZ;

    public float screen_WIDTH = 700, screen_HEIGHT = 600;

    public List<_Point> total;
    public List<_Point> Vertices = new ArrayList<>();

    public Functions(){
        center_Paint = new _Point();
        total = new ArrayList<_Point>();
        Vertices = new ArrayList<_Point>();
    }

    public void read_from_file(String path){
        if (path == "")
            System.out.println("path is empty");
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            List<Integer> list = new ArrayList<Integer>();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.replaceAll("[^a-z0-9]", ",");
                StringTokenizer st = new StringTokenizer(data, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    list.add(parseInt(token));
                }
            }
            for (int i = 0; i < list.size(); i += 3)
                total.add(new _Point(list.get(i), list.get(i + 1), list.get(i + 2)));

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void scale_paint_down(int scale_factor_down){
        float ranger = scale_factor_down;
        ranger = 1 - (ranger / 100);

        for (_Point ver: total){
            ver.setX(ver.getX() * ranger);
            ver.setY(ver.getY() * ranger);
            ver.setZ(ver.getZ() * ranger);
        }
    }

    public void scale_paint_up(int scale_factor_up){
        float ranger = scale_factor_up;
        ranger = ranger / 100 + 1;

        for (_Point ver: total){
            ver.setX(ver.getX() * ranger);
            ver.setY(ver.getY() * ranger);
            ver.setZ(ver.getZ() * ranger);
        }
    }

    public void rotate_paintX(int rotation){
        double angle = (float)rotation * (Math.PI / 180);

        for (_Point t: total){
            float Y = t.getY();
            t.setY((int)(t.getY() * Math.cos(angle) - t.getZ() * Math.sin(angle)));
            t.setZ((int)(Y * Math.sin(angle) + (t.getZ() * Math.cos(angle))));
        }
    }

    public void rotate_paintY(int rotation){
        double angle = (float)rotation * (Math.PI / 180);

        for (_Point t: total){
            float X = t.getX();
            t.setX((int)(t.getX() * Math.cos(angle) + (t.getZ() * Math.sin(angle))));
            t.setZ((int)(-X * Math.sin(angle) + (t.getZ() * Math.cos(angle))));
        }
    }

    public void rotate_paintZ(int rotation){
        double angle = (float)rotation * (Math.PI / 180);

        for (_Point t: total){
            float X = t.getX();
            t.setX(X * (float)Math.cos(angle) - t.y * (float)Math.sin(angle));
            t.setY(X * (float)Math.sin(angle) + t.y * (float)Math.cos(angle));
        }
    }

    public void normalize_points(){
        if (total.size() != 0){
            maxX = minX = total.get(0).getX();
            maxY = minY = total.get(0).getY();
            maxZ = minZ = total.get(0).getZ();

            for (_Point t: total){
                if (t.getX() > maxX)
                    maxX = t.getX();
                if (t.getY() > maxY)
                    maxY = t.getY();
                if (t.getZ() > maxZ)
                    maxZ = t.getZ();

                if (t.getX() < minX)
                    minX = t.getX();
                if (t.getY() < minY)
                    minY = t.getY();
                if (t.getZ() < minZ)
                    minZ = t.getZ();
            }
            center_Paint.setX((maxX + minX) / 2);
            center_Paint.setY((maxY + minY) / 2);
            center_Paint.setZ((maxZ + minZ) / 2);
        }
    }

    public void fix_size(){
        float dx = minX, dy = minY, dz = minZ;

        for (_Point t: total){
            t.setX(t.getX() - dx);
            t.setY(t.getY() - dy);
            t.setZ(t.getZ() - dz);
        }

        double ratioScaleX = screen_WIDTH / maxX * 0.4;
        double ratioScaleY = screen_HEIGHT / maxY * 0.4;

        if (ratioScaleY != 0){
            if (ratioScaleX > ratioScaleY){
                for (_Point t: total){
                    t.setX(t.getX() * (float)ratioScaleY);
                    t.setY(t.getY() * (float)ratioScaleY);
                    t.setZ(t.getZ() * (float)ratioScaleY);
                }
            }
            if (ratioScaleX < ratioScaleY){
                for (_Point t: total){
                    t.setX(t.getX() * (float)ratioScaleX);
                    t.setY(t.getY() * (float)ratioScaleX);
                    t.setZ(t.getZ() * (float)ratioScaleX);
                }
            }
        }
    }

    public void center_paint(){
        normalize_points();
        float dx = (screen_WIDTH / 2) - center_Paint.getX();
        float dy = (screen_HEIGHT / 2) - center_Paint.getY();
        float dz = 150 - center_Paint.getZ();

        for (_Point t: total){
            t.setX(t.getX() + dx);
            t.setY(t.getY() + dy);
            t.setZ(t.getZ() + dz);
        }
    }

    public float visibility(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3){
        float a1 = x2 - x1;
        float b1 = y2 - y1;
        float c1 = z2 - z1;

        float a2 = x3 - x2;
        float b2 = y3 - y2;
        float c2 = z3 - z2;

        float Normal_a = b1 * c2 - b2 * c1;
        float Normal_b = a2 * c1 - a1 * c2;
        float Normal_c = a1 * b2 - b1 * a2;

        return Normal_a * 0 + Normal_b * 0 + Normal_c * (-200);
    }
}
