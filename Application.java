import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Application extends Canvas {

    private GridLayout grid;
    private int axis;
    private String type;
    private JFrame frame;
    private _Point center_screen;

    private List<Face> Array_face;
    private int scale_factor_up, scale_factor_down;
    private Functions func;
    private int rotation;
    private Canvas canvas;
    private int angle_box;
    private File file;
    private  _Point localTotal[];
    private List<_Point> vec = new ArrayList<>();


    public Application(){
        grid = new GridLayout();
        axis = 2;
        type = "Parallel_Orthographic";
        Array_face = new ArrayList<Face>();
        scale_factor_up = scale_factor_down = rotation = 1;
        func = new Functions();
        center_screen = new _Point(func.screen_WIDTH / 2, func.screen_HEIGHT / 2, 1);
        angle_box = 1;

        create_widget();
    }

    public void paintTotal(){
        if (type == "Parallel_Orthographic")
            Parallel_Orthographic();
        if (type == "Cavalier")
            Cavalier();
        if (type == "Cabinet")
            Cabinet();
        if (type == "Perspective")
            Perspective();
    }

    public void paintFile(File file) {
        func.read_from_file(file.getAbsolutePath());
        localTotal = new _Point[func.total.size()];
        int i = 0;
        for (_Point t: func.total){
            _Point tmp = new _Point(t);
            localTotal[i++] = tmp;
        }

        set_face_array();

        // normalizePoints - find edge and center points
        func.normalize_points();

        // Fix size for painting
        func.fix_size();

        // center the paint
        func.center_paint();

        // paint again
        paintTotal();
    }

     // ------------- Projection -------------
     // Parallel_Orthographic - projection
    public void Parallel_Orthographic(){
        func.center_paint();
        total_to_vertices();
        set_visability();
        draw_polygon();
    }

    public void Cavalier(){
        int angle = angle_box;
        func.center_paint();
        total_to_local();
        local_to_Vertices();

        for (_Point t: func.Vertices){
            t.x = t.x + (t.z * (float)Math.cos(angle));
            t.y = t.y + (t.z * (float)Math.sin(angle));
        }

        set_visability();
        draw_polygon();
        local_to_total();
    }

    public void Cabinet(){
        int angle = angle_box;
        func.center_paint();
        total_to_local();
        local_to_Vertices();

        for (_Point t: func.Vertices){
            t.x = t.x + 0.5f * (t.z * (float)Math.cos(angle));
            t.y = t.y + 0.5f * (t.z * (float)Math.sin(angle));
        }

        set_visability();
        draw_polygon();
        local_to_total();
    }

    public void Perspective(){
        int d = angle_box;
        func.center_paint();

        total_to_local();
        local_to_Vertices();

        for (_Point t: func.Vertices){
            t.x = t.x + (t.z / d);
            t.y = t.y + (t.z / d);
        }

        set_visability();
        draw_polygon();
        local_to_total();
    }

    public void total_to_local(){
        for (int i = 0; i < localTotal.length; i++){
            localTotal[i].setX(func.total.get(i).getX());
            localTotal[i].setY(func.total.get(i).getY());
            localTotal[i].setZ(func.total.get(i).getZ());
        }
    }

    public void local_to_Vertices(){
        for (int i = 0; i < localTotal.length; i++){
            func.Vertices.get(i).setX(localTotal[i].getX());
            func.Vertices.get(i).setY(localTotal[i].getY());
            func.Vertices.get(i).setZ(localTotal[i].getZ());
        }
    }


    public void local_to_total(){
        for (int i = 0; i < func.total.size(); i++){
            func.total.get(i).setX(localTotal[i].getX());
            func.total.get(i).setY(localTotal[i].getY());
            func.total.get(i).setZ(localTotal[i].getZ());
        }
    }

    public void total_to_vertices(){
        for (int i = 0; i < func.total.size(); i++){
            func.Vertices.get(i).setX(func.total.get(i).getX());
            func.Vertices.get(i).setY(func.total.get(i).getY());
            func.Vertices.get(i).setZ(func.total.get(i).getZ());
        }
    }

    public void RotateClick(){
        if (axis == 1){
            func.rotate_paintX(rotation);
            paintTotal();
        }
        if (axis == 2){
            func.rotate_paintY(rotation);
            paintTotal();
        }
        if (axis == 3){
            func.rotate_paintZ(rotation);
            paintTotal();
        }
    }

    public void start_Paint(){
        frame = new JFrame("graphic's window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas();
        canvas.setSize(700, 600);
        frame.setLocation(480,200);
        center_screen.setX(200);
        center_screen.setY(200);
        center_screen.setZ(1);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    // calculate if face is visible
    public void set_visability(){

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
        sortByZ_index();
    }


    public void sortByZ_index(){
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

    public void clearCanvas(){
        canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    // Draw polygons
    public void draw_polygon(){
        clearCanvas();
        for (int i = 0; i < Array_face.size(); i++){
            if (Array_face.get(i).d < 0 || Array_face.get(i).d == 0){
                ArrayList<Integer> arr1 = new ArrayList<Integer>(Array_face.get(i).point_array.length);
                ArrayList<Integer> arr2 = new ArrayList<Integer>(Array_face.get(i).point_array.length);

                for (int j = 0; j < Array_face.get(i).point_array.length; j++){
                    arr1.add((int)Array_face.get(i).point_array[j].getX());
                    arr2.add((int)Array_face.get(i).point_array[j].getY());
                }
                int arr11[] = new int[arr1.size()];
                int arr22[] = new int[arr1.size()];
                for (int h = 0; h < arr1.size(); h++){
                    arr11[h] = arr1.get(h);
                    arr22[h] = arr2.get(h);
                }
                canvas.getGraphics().drawPolygon(arr11, arr22, arr11.length);
            }
        }
    }


    public void create_widget(){
        JFrame frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450,640);
        frame.setLocation(40,200);

        JButton b1 = new JButton("Open File");
        b1.setBounds(30,20,140, 40);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                FileDialog fd = new FileDialog(new Frame(), "Open", FileDialog.LOAD);
                fd.setVisible(true);
                if (fd.getDirectory() == null || fd.getFile() == null)
                    return;
                file = new File(fd.getDirectory(), fd.getFile());
                paintFile(file);
            }
        });
        frame.getContentPane().add(b1);


        JButton b2 = new JButton("Orthographic");
        b2.setBounds(30,70,140, 40);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = "Parallel_Orthographic";
                paintTotal();
            }
        });
        frame.getContentPane().add(b2);

        JButton b3 = new JButton("Cabinet");
        b3.setBounds(30,110,140, 40);
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = "Cabinet";
                paintTotal();
            }
        });
        frame.getContentPane().add(b3);

        JButton b4 = new JButton("Cavalier");
        b4.setBounds(30,150,140, 40);
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = "Cavalier";
                paintTotal();
            }
        });
        frame.getContentPane().add(b4);

        JButton b5 = new JButton("Perspective");
        b5.setBounds(30,190,140, 40);
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = "Perspective";
                paintTotal();
            }
        });
        frame.getContentPane().add(b5);


        SpinnerModel value = new SpinnerNumberModel(1, 1, 50, 1);
        JSpinner ObliqueAngle = new JSpinner(value);
        ObliqueAngle.setBounds(240, 120, 50, 40);
        ObliqueAngle.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                angle_box = (Integer)ObliqueAngle.getValue();
            }
        });
        frame.add(ObliqueAngle);



        JButton b6 = new JButton("Scale UP");
        b6.setBounds(60,320,140, 40);
        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                func.scale_paint_up(scale_factor_up);
                paintTotal();
            }
        });
        frame.getContentPane().add(b6);


        SpinnerModel value1 = new SpinnerNumberModel(1, 1, 50, 1);
        JSpinner spinner1 = new JSpinner(value1);
        spinner1.setBounds(10, 320, 50, 40);
        spinner1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                scale_factor_up = (Integer)spinner1.getValue();
            }
        });
        frame.add(spinner1);

        SpinnerModel value2 = new SpinnerNumberModel(1, 1, 50, 1);
        JSpinner sppiner2 = new JSpinner(value2);
        sppiner2.setBounds(370, 320, 50, 40);
        sppiner2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                scale_factor_down = (Integer)sppiner2.getValue();
            }
        });
        frame.add(sppiner2);

        JButton b7 = new JButton("Scale Down");
        b7.setBounds(250,320,140, 40);
        b7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                func.scale_paint_down(scale_factor_down);
                paintTotal();
            }
        });
        frame.getContentPane().add(b7);

        JButton b8 = new JButton("Rotate X");
        b8.setBounds(30,420,140, 40);
        b8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                axis = 1;
                RotateClick();
            }
        });
        frame.getContentPane().add(b8);

        JButton b9 = new JButton("Rotate Y");
        b9.setBounds(30,470,140, 40);
        b9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                axis = 2;
                RotateClick();
            }
        });
        frame.getContentPane().add(b9);

        JButton b10 = new JButton("Rotate Z");
        b10.setBounds(30,520,140, 40);
        b10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                axis = 3;
                RotateClick();
            }
        });
        frame.getContentPane().add(b10);


        SpinnerModel value3 = new SpinnerNumberModel(1, 1, 50, 1);
        JSpinner sppiner3 = new JSpinner(value3);
        sppiner3.setBounds(250, 470, 50, 40);
        sppiner3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                rotation = (Integer)sppiner3.getValue();
            }
        });
        frame.add(sppiner3);

        JButton b11 = new JButton("Clear Canvas");
        b11.setBounds(250,20,140, 40);
        b11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearCanvas();
            }
        });
        frame.getContentPane().add(b11);

        frame.setLayout(null);
        frame.setVisible(true);
        start_Paint();
    }


    public void set_face_array(){

        for (_Point t: func.total){
            func.Vertices.add(t);
        }

        List<_Point> t = new ArrayList<>();
        t = func.Vertices;


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


    public static void main (String[] args) {
        Application app = new Application();
    }
}


