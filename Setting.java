import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Setting {

    public void set_face_array(List<_Point> total, List<_Point> Vertices, List<Face> Array_face){

        for (_Point t: total)
            Vertices.add(t);

        List<_Point> t = new ArrayList<>();
        t = Vertices;

        _Point s1[] = new _Point[4];
        s1[0] = t.get(5);
        s1[1] = t.get(6);
        s1[2] = t.get(7);
        s1[3] = t.get(4);

        Face f1 = new Face(s1);

        Stream.of(t.get(4).getZ(), t.get(5).getZ(), t.get(6).getZ(), t.get(7).getZ()).
                max(Comparator.naturalOrder())
                .ifPresent(maxInt -> f1.z_index = maxInt);

        Array_face.add(f1);

        // Bottom
        _Point s2[] = new _Point[4];
        s2[0] = t.get(3);
        s2[1] = t.get(7);
        s2[2] = t.get(6);
        s2[3] = t.get(2);

        Face f2 = new Face(s2);

        Stream.of(t.get(3).getZ(), t.get(7).getZ(), t.get(6).getZ(), t.get(2).getZ()).
                max(Comparator.naturalOrder())
                .ifPresent(maxInt -> f2.z_index = maxInt);

        Array_face.add(f2);

        // Left
        _Point s3[] = new _Point[4];
        s3[0] = t.get(0);
        s3[1] = t.get(4);
        s3[2] = t.get(7);
        s3[3] = t.get(3);

        Face f3 = new Face(s3);

        Stream.of(t.get(0).getZ(), t.get(4).getZ(), t.get(7).getZ(), t.get(3).getZ()).
                max(Comparator.naturalOrder()).ifPresent(maxInt -> f3.z_index = maxInt);

        Array_face.add(f3);

        // Top
        _Point s4[] = new _Point[4];
        s4[0] = t.get(0);
        s4[1] = t.get(1);
        s4[2] = t.get(5);
        s4[3] = t.get(4);

        Face f4 = new Face(s4);

        Stream.of(t.get(0).getZ(), t.get(4).getZ(), t.get(5).getZ(), t.get(1).getZ()).
                max(Comparator.naturalOrder()).ifPresent(maxInt -> f4.z_index = maxInt);

        Array_face.add(f4);

        // Right
        _Point s5[] = new _Point[4];
        s5[0] = t.get(1);
        s5[1] = t.get(2);
        s5[2] = t.get(6);
        s5[3] = t.get(5);

        Face f5 = new Face(s5);

        Stream.of(t.get(1).getZ(), t.get(5).getZ(), t.get(6).getZ(), t.get(2).getZ()).
                max(Comparator.naturalOrder())
                .ifPresent(maxInt -> f5.z_index = maxInt);

        Array_face.add(f5);

        // Front
        _Point s6[] = new _Point[4];
        s6[0] = t.get(0);
        s6[1] = t.get(3);
        s6[2] = t.get(2);
        s6[3] = t.get(1);

        Face f6 = new Face(s6);

        Stream.of(t.get(0).getZ(), t.get(3).getZ(), t.get(2).getZ(), t.get(1).getZ()).
                max(Comparator.naturalOrder())
                .ifPresent(maxInt -> f6.z_index = maxInt);

        Array_face.add(f6);

        // --------------Pyramid--------------
        // Front
        _Point s7[] = new _Point[3];
        s7[0] = t.get(8);
        s7[1] = t.get(10);
        s7[2] = t.get(9);

        Face f7 = new Face(s7);

        Stream.of(t.get(8).getZ(), t.get(9).getZ(), t.get(10).getZ()).
                max(Comparator.naturalOrder())
                .ifPresent(maxInt -> f7.z_index = maxInt);

        Array_face.add(f7);

        // Left
        _Point s8[] = new _Point[3];
        s8[0] = t.get(8);
        s8[1] = t.get(9);
        s8[2] = t.get(11);

        Face f8 = new Face(s8);

        Stream.of(t.get(8).getZ(), t.get(9).getZ(), t.get(11).getZ()).
                max(Comparator.naturalOrder())
                .ifPresent(maxInt -> f8.z_index = maxInt);

        Array_face.add(f8);

        // Right
        _Point s9[] = new _Point[3];
        s9[0] = t.get(9);
        s9[1] = t.get(10);
        s9[2] = t.get(11);

        Face f9 = new Face(s9);

        Stream.of(t.get(10).getZ(), t.get(9).getZ(), t.get(11).getZ()).
                max(Comparator.naturalOrder())
                .ifPresent(maxInt -> f9.z_index = maxInt);

        Array_face.add(f9);

        // Bottom
        _Point s10[] = new _Point[3];
        s10[0] = t.get(8);
        s10[1] = t.get(11);
        s10[2] = t.get(10);

        Face f10 = new Face(s10);

        Stream.of(t.get(8).getZ(), t.get(10).getZ(), t.get(11).getZ()).
                max(Comparator.naturalOrder())
                .ifPresent(maxInt -> f10.z_index = maxInt);

        Array_face.add(f10);
    }
}
