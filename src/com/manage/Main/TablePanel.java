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
public class TablePanel extends JPanel{
    private String[] table_s = {"","未使用","在点餐","已下单"};
    private JLabel title;
    private JPanel titlePanel;
    private  String titles[] = {"桌号","座位数","使用状态"};
    private String keys[] = {"table_id","max_seating","table_status"};
    private String objects[][]= null;
    private JTable table = null;
    private JScrollPane scrollPane = null;
    private  JPanel centerPanel = new JPanel();
    //中间的textField
    private JLabel tableidLabel = new JLabel("桌号");
    private JLabel maxseatingLabel = new JLabel("座位数");
    private JLabel tablestatusLabel = new JLabel("使用状态");
    private  JTextField tableidTextField = new JTextField();
    private  JTextField maxseatingTextField = new JTextField();
    private JComboBox statusbox = new JComboBox();
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
    public TablePanel(){
        // 创建标题面板
        titlePanel = new JPanel();

        // 设置标题面板的大小
        titlePanel.setPreferredSize(new Dimension(600, 60));

        // 设置标题面板上下左右的边距
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // 设置标题的字体及大小
        title = new JLabel("餐桌信息", SwingConstants.CENTER);
        title.setFont(new Font("宋体", Font.BOLD, 28));

        // 把标题加入标题面板
        titlePanel.add(title);
        //进行数据库的链接初始化数组
        jdbcMySQL.getConnection();
        ResultSet res = jdbcMySQL.query("select * from res_table");
        try{
            res.last();
            totalRows = res.getRow();
            objects = new String[totalRows][3];
            res.beforeFirst();

            while (res.next()){
                for( j = 0; j < 3;j++)
                    objects[i][j] = res.getString(keys[j]);
                i++;
            }
            DefaultTableModel model = new DefaultTableModel(objects,titles);
            table = new JTable(model);
            table.setPreferredScrollableViewportSize(new Dimension(800, 150));
            table.setRowHeight(30);
            scrollPane = new JScrollPane(table);

            editPanel.add(tableidLabel);
            tableidTextField.setColumns(8);
            editPanel.add(tableidTextField);

            editPanel.add(maxseatingLabel);
            maxseatingTextField.setColumns(8);
            editPanel.add(maxseatingTextField);

            editPanel.add(tablestatusLabel);
            statusbox.addItem(table_s[0]);
            statusbox.addItem(table_s[1]);
            statusbox.addItem(table_s[2]);
            statusbox.addItem(table_s[3]);
            editPanel.add(statusbox);


//
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(scrollPane);
            centerPanel.add(editPanel,BorderLayout.SOUTH);
            editPanel.add(insertBtn);
            editPanel.add(updateBtn);
            editPanel.add(deleteBtn);


            // 把标题面板加入first panel面板
            this.add(titlePanel, BorderLayout.NORTH);
            this.add(centerPanel,BorderLayout.CENTER);

//table点击某一行
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if(table.getSelectedRowCount()==1) {
                        int row = table.getSelectedRow();
                        selectRows = row;
                        //获取该行的数据 并且显示在textField上
                        tableidTextField.setText(objects[row][0]);
                        maxseatingTextField.setText(objects[row][1]);
                        statusbox.setSelectedIndex(Integer.valueOf(objects[row][2]));
                    }
                }
            });
            insertBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //插入语句 判断数据的有效性

                    if(check()) {
                        int tableid = Integer.valueOf(tableidTextField.getText().toString());
                        int max = Integer.valueOf(maxseatingTextField.getText().toString());
                        int tablestatus = statusbox.getSelectedIndex();
                        String sql = "insert into res_table values (" + tableid  + ","+max+","+ tablestatus +")";

                        if(jdbcMySQL.deleteOrInsert(sql) > 0) {
                            JOptionPane.showMessageDialog(null, "添加成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                            //插入行
                            String  newobjects[][] = objects;
                            int tempRow = totalRows;
                            totalRows += 1;

                            objects = new String[totalRows][3];
                            for(i = 0 ; i < tempRow;i++)
                                for (j = 0; j < 3;j++)
                                    objects[i][j] = newobjects[i][j];

                            objects[totalRows - 1][0] = String.valueOf(tableid);
                            objects[totalRows - 1][1] = String.valueOf(max);
                            objects[totalRows - 1][2] = String.valueOf(tablestatus);
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
                        int tableid = Integer.valueOf(tableidTextField.getText().toString());
                        int max = Integer.valueOf(maxseatingTextField.getText().toString());
                        int tablestatus = statusbox.getSelectedIndex();
                        String sql = "UPDATE restable SET table_id = " + tableid + ",max_seating="+max+",table_status="+tablestatus+" where table_id = " + tableid;
                        if(jdbcMySQL.update(sql) > 0) {
                            JOptionPane.showMessageDialog(null, "修改成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                            objects[selectRows][0] = String.valueOf(tableid);
                            objects[selectRows][1] = String.valueOf(max);
                            objects[selectRows][2] = String.valueOf(tablestatus);
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
                    String tableid = tableidTextField.getText().toString();
                    String sql = "delete from res_table where table_id = " + tableid;
                    if(jdbcMySQL.deleteOrInsert(sql)> 0){
                        JOptionPane.showMessageDialog(null, "删除成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                        //前几行和他一样
                        String newobject[][] = new String[totalRows - 1][3];
                        for(i = 0; i < selectRows;i++)
                            for(j = 0; j < 3;j++)
                                newobject[i][j] = objects[i][j];
                        //向前移动一行
                        for( i = 0; i < 3;i++) {
                            for(j = selectRows; j < totalRows - 1;j++)
                                newobject[j][i] = objects[j+1][i];
                        }
                        totalRows--;
                        objects = new String[totalRows][3];
                        for(int i = 0; i < totalRows;i++)
                            for(int j = 0 ; j < 3;j++) {
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
