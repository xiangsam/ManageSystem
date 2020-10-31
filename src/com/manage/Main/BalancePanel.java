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
public class BalancePanel extends JPanel{
    private String[] isdelete_s = {"删除","显示"};
    private String[] balance_s = {"失效","未付款","已付款"};
    private JLabel title;
    private JPanel titlePanel;
    private  String titles[] = {"订单主键","处理状态","付款状态","总价"};
    private String keys[] = {"id","is_delete","balance_status","total_price"};
    private String objects[][]= null;
    private JTable table = null;
    private JScrollPane scrollPane = null;
    private  JPanel centerPanel = new JPanel();
    //中间的textField
    private JLabel idLabel = new JLabel("订单主键");
    private JLabel isdeleteLabel = new JLabel("处理状态");
    private JLabel balancestatusLabel = new JLabel("付款状态");
    private JLabel totalpriceLabel = new JLabel("总价");
    private  JTextField idTextField = new JTextField();
    private JComboBox deletebox = new JComboBox();
    private JComboBox balancebox =new JComboBox();
    private  JTextField totalpriceTextField = new JTextField();
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
    public BalancePanel(){
        // 创建标题面板
        titlePanel = new JPanel();

        // 设置标题面板的大小
        titlePanel.setPreferredSize(new Dimension(600, 140));

        // 设置标题面板上下左右的边距
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        // 设置标题的字体及大小
        title = new JLabel("点餐信息", SwingConstants.CENTER);
        title.setFont(new Font("宋体", Font.BOLD, 28));

        // 把标题加入标题面板
        titlePanel.add(title);
        //进行数据库的链接初始化数组
        jdbcMySQL.getConnection();
        ResultSet res = jdbcMySQL.query("select * from balance");
        try{
            res.last();
            totalRows = res.getRow();
            objects = new String[totalRows][4];
            res.beforeFirst();

            while (res.next()){
                for( j = 0; j < 4;j++)
                    objects[i][j] = res.getString(keys[j]);
                i++;
            }
            DefaultTableModel model = new DefaultTableModel(objects,titles);
            table = new JTable(model);
            table.setPreferredScrollableViewportSize(new Dimension(660, 170));
            table.setRowHeight(30);
            scrollPane = new JScrollPane(table);

            editPanel.add(idLabel);
            idTextField.setColumns(8);
            editPanel.add(idTextField);

            editPanel.add(isdeleteLabel);
            deletebox.addItem(isdelete_s[0]);
            deletebox.addItem(isdelete_s[1]);
            editPanel.add(deletebox);

            editPanel.add(balancestatusLabel);
            balancebox.addItem(balance_s[0]);
            balancebox.addItem(balance_s[1]);
            balancebox.addItem(balance_s[2]);
            editPanel.add(balancebox);

            editPanel.add(totalpriceLabel);
            totalpriceTextField.setColumns(8);
            editPanel.add(totalpriceTextField);

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
                        idTextField.setText(objects[row][0]);
                        deletebox.setSelectedIndex(Integer.valueOf(objects[row][1]));
                        balancebox.setSelectedIndex(Integer.valueOf(objects[row][2]));
                        totalpriceTextField.setText(objects[row][3]);
                    }
                }
            });
            insertBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //插入语句 判断数据的有效性

                    if(check()) {
                        int id = Integer.valueOf(idTextField.getText().toString());
                        int isdelete = deletebox.getSelectedIndex();
                        int balancestatus = balancebox.getSelectedIndex();
                        Double totalprice = Double.valueOf(totalpriceTextField.getText().toString());
                        String sql = "insert into balance values (" + id  + ","+isdelete+","+ balancestatus + "," + totalprice + ",\'" + " " + "\',\'" + " " + ",\'" + " " + "\')";

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

                            objects[totalRows - 1][0] = String.valueOf(id);
                            objects[totalRows - 1][1] = String.valueOf(isdelete);
                            objects[totalRows - 1][2] = String.valueOf(balancestatus);
                            objects[totalRows - 1][3] = String.valueOf(totalprice);
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
                        int id = Integer.valueOf(idTextField.getText().toString());
                        int isdelete = deletebox.getSelectedIndex();
                        int balancestatus = balancebox.getSelectedIndex();
                        Double totalprice = Double.valueOf(totalpriceTextField.getText().toString());
                        String sql = "UPDATE balance SET id = " + id + ",is_delete="+isdelete+",balance_status="+balancestatus+",total_price=" + totalprice+" where id = " + id;
                        if(jdbcMySQL.update(sql) > 0) {
                            JOptionPane.showMessageDialog(null, "修改成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                            objects[selectRows][0] = String.valueOf(id);
                            objects[selectRows][1] = String.valueOf(isdelete);
                            objects[selectRows][2] = String.valueOf(balancestatus);
                            objects[selectRows][3] = String.valueOf(totalprice);
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
                    String id = idTextField.getText().toString();
                    String sql = "delete from balance where id = " + id;
                    if(jdbcMySQL.deleteOrInsert(sql)> 0){
                        JOptionPane.showMessageDialog(null, "删除成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                        //前几行和他一样
                        String newobject[][] = new String[totalRows - 1][4];
                        for(i = 0; i < selectRows;i++)
                            for(j = 0; j < 4;j++)
                                newobject[i][j] = objects[i][j];
                        //向前移动一行
                        for( i = 0; i < 4;i++) {
                            for(j = selectRows; j < totalRows - 1;j++)
                                newobject[j][i] = objects[j+1][i];
                        }
                        totalRows--;
                        objects = new String[totalRows][4];
                        for(int i = 0; i < totalRows;i++)
                            for(int j = 0 ; j < 4;j++) {
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
        int isdelete = deletebox.getSelectedIndex();
        int balancestatus = balancebox.getSelectedIndex();
        if((isdelete == 1 || isdelete == 0)&&(balancestatus == 0 || balancestatus == 1 || balancestatus == 2)){
            return true;
        }else
            return false;
    }
}
