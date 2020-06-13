import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Application extends Canvas {
    private GridLayout grid;
    private int axis, rotation, angle_box, distance_box;
    private String type;
    private JFrame frame;
    private _Point center_screen, localTotal[];
    public List<Face> Array_face;
    private int scale_factor_up, scale_factor_down;
    public Functions func;
    private Canvas canvas;
    private File file;
    private Setting setting;
    private Orthographic orthographic;


    public Application(){
        grid = new GridLayout();
        axis = 2;
        type = "Parallel_Orthographic";
        Array_face = new ArrayList<Face>();
        scale_factor_up = scale_factor_down = rotation = 1;
        angle_box = distance_box = 1;
        func = new Functions();
        center_screen = new _Point(func.screen_WIDTH / 2, func.screen_HEIGHT / 2, 1);
        this.setting = new Setting();
        this.orthographic = new Orthographic();

        create_widget();
    }


    public void paintFile(File file) {
        func.read_from_file(file.getAbsolutePath());
        localTotal = new _Point[func.total.size()];
        int i = 0;
        for (_Point t: func.total){
            _Point tmp = new _Point(t);
            localTotal[i++] = tmp;
        }

        setting.set_face_array(func.total, func.Vertices, Array_face);

        // normalizePoints - find edge and center points
        func.normalize_points();

        // Fix size for painting
        func.fix_size();

        // center the paint
        func.center_paint();

        // paint again
        paintTotal();
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

    public void clearCanvas(){
        canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


    // ------------- Projection -------------
    // Parallel_Orthographic - projection
    public void Parallel_Orthographic(){
        func.center_paint();
        orthographic.total_to_vertices(func);
        orthographic.set_visability(func, Array_face);
        draw_polygon();
    }

    public void RotateClick(){
        switch (axis) {
            case 1:
                func.rotate_paintX(rotation);
            paintTotal();
            break;
            case 2:
                func.rotate_paintY(rotation);
                paintTotal();
                break;
            case 3:
                func.rotate_paintZ(rotation);
                paintTotal();
                break;
        }
    }

    public void paintTotal(){
        switch (type) {
            case "Parallel_Orthographic":
                Parallel_Orthographic();
                break;
            case "Cavalier":
                orthographic.Cavalier(func, angle_box, localTotal, Array_face, this);
                break;
            case "Cabinet":
                orthographic.Cabinet(func, angle_box, localTotal, Array_face, this);
                break;
            case "Perspective":
                orthographic.Perspective(func, distance_box, localTotal, Array_face, this);
                break;
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
        b5.setBounds(30,210,140, 40);
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = "Perspective";
                paintTotal();
            }
        });
        frame.getContentPane().add(b5);

        SpinnerModel valueOfPerspective = new SpinnerNumberModel(1, 1, 50, 1);
        JSpinner distance = new JSpinner(valueOfPerspective);
        distance.setBounds(240, 210, 50, 40);
        distance.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                distance_box = (Integer)distance.getValue();
            }
        });
        frame.add(distance);


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


        JLabel label = new JLabel();
        label.setText("select angle");

        JPanel p = new JPanel();
        p.add(label);
        frame.add(p);

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

    public static void main (String[] args) {
        Application app = new Application();
    }
}
