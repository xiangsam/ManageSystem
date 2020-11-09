package com.manage.Main;
import com.manage.MySQL.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

//点餐处理面板
@SuppressWarnings("serial")
public class DishPanel extends JPanel{
    private String[] hot_s = {"无效","不辣","微辣","中辣","超辣"};
    private String[] sign_s = {"不是招牌","招牌"};
    private String[] new_s = {"不是新品","新品"};
    private String[] dishtype_s ={"热销","饭后甜点","锅底","蔬菜","肉类","冷菜","酒水"};
    private JLabel title;
    private JPanel titlePanel;
    private  String titles[] = {"菜的主键","菜名","图片路径","辣度","是否招牌","是否新品","单价","菜品","菜的描述"};
    private String keys[] = {"dish_id","dish_name","dish_url","hot_status","is_sign","is_new_dish","dish_price","dish_type","dish_description"};
    private String objects[][]= null;
    private JTable table = null;
    private JScrollPane scrollPane = null;
    private  JPanel centerPanel = new JPanel();
    //中间的textField
    private JLabel dishidLabel = new JLabel("菜的主键");
    private JLabel dishnameLabel = new JLabel("菜名");
    private JLabel dishurlLabel = new JLabel("图片路径");
    private JLabel hotstatusLabel = new JLabel("是否招牌");
    private JLabel issignLabel = new JLabel("是否招牌");
    private JLabel isnewdishLabel = new JLabel("是否新品");
    private JLabel dishpriceLabel = new JLabel("单价");
    private JLabel dishtypeLabel = new JLabel("菜品");
    private JLabel dishdescriptionLabel = new JLabel("菜的描述");
    private  JTextField dishidTextField = new JTextField();
    private  JTextField dishnameTextField = new JTextField();
    private  JTextField dishurlTextField = new JTextField();
    private JComboBox hotstatusbox = new JComboBox();
    private JComboBox signbox =new JComboBox();
    private JComboBox newbox =new JComboBox();
    private  JTextField dishpriceTextField = new JTextField();
    private JComboBox dishtypebox =new JComboBox();
    private  JTextField dishdescriptionTextField = new JTextField();
    private  JPanel editPanel = new JPanel();
    //南部的按钮
    private JButton insertBtn = new JButton("添加");
    private  JButton updateBtn = new JButton("修改");
    private  JButton deleteBtn = new JButton("删除");
    private  JPanel southPanel = new JPanel();


    private  JDBCMySQL jdbcMySQL = new JDBCMySQL();
    private  int i = 0,j = 0;
    private  int totalRows = 0;
    //被选中的行数
    private  int selectRows = 0;
    public DishPanel(){
        // 创建标题面板
        titlePanel = new JPanel();

        // 设置标题面板的大小
        titlePanel.setPreferredSize(new Dimension(600, 60));

        // 设置标题面板上下左右的边距
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // 设置标题的字体及大小
        title = new JLabel("菜单管理", SwingConstants.CENTER);
        title.setFont(new Font("宋体", Font.BOLD, 28));

        // 把标题加入标题面板
        titlePanel.add(title);
        //进行数据库的链接初始化数组
        jdbcMySQL.getConnection();
        ResultSet res = jdbcMySQL.query("select * from dish");
        try{
            res.last();
            totalRows = res.getRow();
            objects = new String[totalRows][9];
            res.beforeFirst();

            while (res.next()){
                for( j = 0; j < 9;j++)
                    objects[i][j] = res.getString(keys[j]);
                i++;
            }
            DefaultTableModel model = new DefaultTableModel(objects,titles);
            table = new JTable(model);
            table.setPreferredScrollableViewportSize(new Dimension(1100, 150));
            table.setRowHeight(30);
            scrollPane = new JScrollPane(table);

            editPanel.add(dishidLabel);
            dishidTextField.setColumns(8);
            editPanel.add(dishidTextField);

            editPanel.add(dishnameLabel);
            dishnameTextField.setColumns(8);
            editPanel.add(dishnameTextField);

            editPanel.add(dishurlLabel);
            dishurlTextField.setColumns(8);
            editPanel.add(dishurlTextField);

            editPanel.add(hotstatusLabel);
            hotstatusbox.addItem(hot_s[0]);
            hotstatusbox.addItem(hot_s[1]);
            hotstatusbox.addItem(hot_s[2]);
            hotstatusbox.addItem(hot_s[3]);
            hotstatusbox.addItem(hot_s[4]);
            editPanel.add(hotstatusbox);

            editPanel.add(issignLabel);
            signbox.addItem(sign_s[0]);
            signbox.addItem(sign_s[1]);
            editPanel.add(signbox);

            editPanel.add(isnewdishLabel);
            newbox.addItem(new_s[0]);
            newbox.addItem(new_s[1]);
            editPanel.add(newbox);

            editPanel.add(dishpriceLabel);
            dishpriceTextField.setColumns(8);
            editPanel.add(dishpriceTextField);

            editPanel.add(dishtypeLabel);
            dishtypebox.addItem(dishtype_s[0]);
            dishtypebox.addItem(dishtype_s[1]);
            dishtypebox.addItem(dishtype_s[2]);
            dishtypebox.addItem(dishtype_s[3]);
            dishtypebox.addItem(dishtype_s[4]);
            dishtypebox.addItem(dishtype_s[5]);
            dishtypebox.addItem(dishtype_s[6]);
            editPanel.add(dishtypebox);

            editPanel.add(dishdescriptionLabel);
            dishdescriptionTextField.setColumns(8);
            editPanel.add(dishdescriptionTextField);

//
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(scrollPane);
            centerPanel.add(editPanel,BorderLayout.SOUTH);
            //南部面板
            // southPanel.add(editPanel);
            southPanel.add(insertBtn);
            southPanel.add(updateBtn);
            southPanel.add(deleteBtn);


            // 把标题面板加入first panel面板
            this.add(titlePanel, BorderLayout.NORTH);
            this.add(centerPanel,BorderLayout.CENTER);
            this.add(southPanel,BorderLayout.SOUTH);

//table点击某一行
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if(table.getSelectedRowCount()==1) {
                        int row = table.getSelectedRow();
                        selectRows = row;
                        //获取该行的数据 并且显示在textField上
                        dishidTextField.setText(objects[row][0]);
                        dishnameTextField.setText(objects[row][1]);
                        dishurlTextField.setText(objects[row][2]);
                        hotstatusbox.setSelectedIndex(Integer.valueOf(objects[row][3]));
                        signbox.setSelectedIndex(Integer.valueOf(objects[row][4]));
                        newbox.setSelectedIndex(Integer.valueOf(objects[row][5]));
                        dishpriceTextField.setText(objects[row][6]);
                        dishtypebox.setSelectedIndex(Integer.valueOf(objects[row][7]));
                        dishdescriptionTextField.setText(objects[row][8]);
                    }
                }
            });
            insertBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //插入语句 判断数据的有效性

                    if(check()) {
                        int dishid = Integer.valueOf(dishidTextField.getText().toString());
                        String dishname =dishnameTextField.getText().toString();
                        String dishurl =dishurlTextField.getText().toString();
                        int hotstatus = hotstatusbox.getSelectedIndex();
                        int issign = signbox.getSelectedIndex();
                        int isnewdish = newbox.getSelectedIndex();
                        Double dishprice = Double.valueOf(dishpriceTextField.getText().toString());
                        int dishtype = dishtypebox.getSelectedIndex();
                        String dishdescription = dishdescriptionTextField.getText().toString();
                        String sql = "insert into dish values (" + dishid  + ",\'"+dishname+"\',\'"+ dishurl + "\'," + hotstatus +","+0+","+issign+","+isnewdish+","+0+","+dishprice+","+dishtype+",\'" + dishdescription + "\')";

                        if(jdbcMySQL.deleteOrInsert(sql) > 0) {
                            JOptionPane.showMessageDialog(null, "添加成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                            //插入行
                            String  newobjects[][] = objects;
                            int tempRow = totalRows;
                            totalRows += 1;

                            objects = new String[totalRows][4];
                            for(i = 0 ; i < tempRow;i++)
                                for (j = 0; j < 4;j++)
                                    objects[i][j] = newobjects[i][j];

                            objects[totalRows - 1][0] = String.valueOf(dishid);
                            objects[totalRows - 1][1] = dishname;
                            objects[totalRows - 1][2] = dishurl;
                            objects[totalRows - 1][3] = String.valueOf(hotstatus);
                            objects[totalRows - 1][4] = String.valueOf(issign);
                            objects[totalRows - 1][5] = String.valueOf(isnewdish);
                            objects[totalRows - 1][6] = String.valueOf(dishprice);
                            objects[totalRows - 1][7] = String.valueOf(dishtype);
                            objects[totalRows - 1][8] = dishdescription;
                            model.setDataVector(objects,titles);
                        }
                        else
                            JOptionPane.showMessageDialog(null, "添加失败", "警告", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            //更新某行
            updateBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(check()){
                        int dishid = Integer.valueOf(dishidTextField.getText().toString());
                        String dishname =dishnameTextField.getText().toString();
                        String dishurl =dishurlTextField.getText().toString();
                        int hotstatus = hotstatusbox.getSelectedIndex();
                        int issign = signbox.getSelectedIndex();
                        int isnewdish = newbox.getSelectedIndex();
                        Double dishprice = Double.valueOf(dishpriceTextField.getText().toString());
                        int dishtype = dishtypebox.getSelectedIndex();
                        String dishdescription = dishdescriptionTextField.getText().toString();
                        String sql = "UPDATE dish SET dish_id = " + dishid + ",dish_name=\'"+dishname+"\',dish_url=\'"+dishurl+"\',hot_status=" + hotstatus+",is_sign="+issign+",is_new_dish="+isnewdish+",dish_price="+dishprice+",dish_type="+dishtype+",dish_description=\'"+dishdescription+"\' where dish_id = " + dishid;
                        if(jdbcMySQL.update(sql) > 0) {
                            JOptionPane.showMessageDialog(null, "修改成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                            objects[selectRows][0] = String.valueOf(dishid);
                            objects[selectRows][1] = dishname;
                            objects[selectRows][2] = dishurl;
                            objects[selectRows][3] = String.valueOf(hotstatus);
                            objects[selectRows][4] = String.valueOf(issign);
                            objects[selectRows][5] = String.valueOf(isnewdish);
                            objects[selectRows][6] = String.valueOf(dishprice);
                            objects[selectRows][7] = String.valueOf(dishtype);
                            objects[selectRows][8] = dishdescription;
                            model.setDataVector(objects,titles);

                        }else
                            JOptionPane.showMessageDialog(null, "修改失败", "警告", JOptionPane.WARNING_MESSAGE);


                    }
                }
            });
            //删除某行
            deleteBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //向前移动一行
                    String dishid = dishidTextField.getText().toString();
                    String sql = "delete from dish where dish_id = " + dishid;
                    if(jdbcMySQL.deleteOrInsert(sql)> 0){
                        JOptionPane.showMessageDialog(null, "删除成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                        //前几行和他一样
                        String newobject[][] = new String[totalRows - 1][9];
                        for(i = 0; i < selectRows;i++)
                            for(j = 0; j < 9;j++)
                                newobject[i][j] = objects[i][j];
                        //向前移动一行
                        for( i = 0; i < 9;i++) {
                            for(j = selectRows; j < totalRows - 1;j++)
                                newobject[j][i] = objects[j+1][i];
                        }
                        totalRows--;
                        objects = new String[totalRows][9];
                        for(int i = 0; i < totalRows;i++)
                            for(int j = 0 ; j < 9;j++) {
                                objects[i][j] = newobject[i][j];

                            }

                        model.setDataVector(objects,titles);
                    }else{
                        JOptionPane.showMessageDialog(null, "删除失败", "警告", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }catch (SQLException e){
            e.printStackTrace();
        }











    }
    //检查有效性
    public  boolean check() {
        return true;
    }
}
