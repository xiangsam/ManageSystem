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

//预约面板
@SuppressWarnings("serial")
public class ReservePanel extends JPanel{
    private String[] isdelete_s = {"删除","显示"};
    private String[] reserve_s = {"失效","预约","等待","无法预约"};
    private JLabel title;
    private JPanel titlePanel;
   private  String titles[] = {"id","userid","桌号","预约号","预约状态","预约时间","处理状态"};
    private String keys[] = {"id","user_id","table_id","reserve_no","reserve_status","reserve_date","is_delete"};
    private String objects[][]= null;
    private JTable table = null;
    private JScrollPane scrollPane = null;
    private  JPanel centerPanel = new JPanel();
    //中间的textField
    private JLabel idLabel = new JLabel("id");
    private JLabel useridLabel = new JLabel("userid");
    private JLabel tableidLabel = new JLabel("桌号");
    private JLabel reservenoLabel = new JLabel("预约号");
    private JLabel reservestatusLabel = new JLabel("预约状态");
    private JLabel reservedataLabel = new JLabel("预约时间");
    private JLabel isdeleteLabel = new JLabel("处理状态");
    private  JTextField idTextField = new JTextField();
    private  JTextField useridTextField = new JTextField();
    private  JTextField tableidTextField = new JTextField();
    private  JTextField reservenoTextField = new JTextField();
    private  JTextField reservestatusTextField = new JTextField();
    private JComboBox reservestatusBox = new JComboBox();
    private  JTextField reservedataTextField = new JTextField();
    private  JTextField isdeleteTextField = new JTextField();
    private JComboBox isdeleteBox = new JComboBox();
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
    public ReservePanel(){
        // 创建标题面板
        titlePanel = new JPanel();

        // 设置标题面板的大小
        titlePanel.setPreferredSize(new Dimension(600, 140));

        // 设置标题面板上下左右的边距
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        // 设置标题的字体及大小
        title = new JLabel("预约信息", SwingConstants.CENTER);
        title.setFont(new Font("宋体", Font.BOLD, 28));

        // 把标题加入标题面板
        titlePanel.add(title);
        //进行数据库的链接初始化数组
        jdbcMySQL.getConnection();
        ResultSet res = jdbcMySQL.query("select * from reserve");
       try{
       res.last();
           totalRows = res.getRow();
           objects = new String[totalRows][7];
           res.beforeFirst();

           while (res.next()){
               for( j = 0; j < 7;j++)
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

           editPanel.add(useridLabel);
           useridTextField.setColumns(8);
           editPanel.add(useridTextField);

           editPanel.add(tableidLabel);
           tableidTextField.setColumns(8);
           editPanel.add(tableidTextField);

           editPanel.add(reservenoLabel);
           reservenoTextField.setColumns(8);
           editPanel.add(reservenoTextField);

           editPanel.add(reservestatusLabel);
           reservestatusBox.addItem(reserve_s[0]);
           reservestatusBox.addItem(reserve_s[1]);
           reservestatusBox.addItem(reserve_s[2]);
           reservestatusBox.addItem(reserve_s[3]);
           editPanel.add(reservestatusBox);

           editPanel.add(reservedataLabel);
           reservedataTextField.setColumns(20);
           editPanel.add(reservedataTextField);

           editPanel.add(isdeleteLabel);
           isdeleteBox.addItem(isdelete_s[0]);
           isdeleteBox.addItem(isdelete_s[1]);
           editPanel.add(isdeleteBox);
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
                       useridTextField.setText(objects[row][1]);
                       tableidTextField.setText(objects[row][2]);
                       reservenoTextField.setText(objects[row][3]);
                       reservestatusBox.setSelectedIndex(Integer.valueOf(objects[row][4]));
                       reservedataTextField.setText(objects[row][5]);
                       isdeleteBox.setSelectedIndex(Integer.valueOf(objects[row][6]));
                   }
               }
           });
           insertBtn.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   //插入语句 判断数据的有效性

                    if(check()) {
                        int id = Integer.valueOf(idTextField.getText().toString());
                        int userid = Integer.valueOf(useridTextField.getText().toString());
                        int tableid = Integer.valueOf(tableidTextField.getText().toString());
                        String reserveno = reservenoTextField.getText().toString();
                        int reservestatus = reservestatusBox.getSelectedIndex();
                        String reservedata = reservedataTextField.getText().toString();
                        int isdelete = isdeleteBox.getSelectedIndex();
                        String sql = "insert into reserve values (" + id  + ",\'"+" "+"\',"+ userid + "," + tableid + ",\'" + reserveno + "\'," + reservestatus + ",\'" + reservedata + "\',\'"+reservedata+"\',\'"+reservedata+"\',"+ isdelete+")";

                        if(jdbcMySQL.deleteOrInsert(sql) > 0) {
                            JOptionPane.showMessageDialog(null, "添加成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                        //插入行
                            String  newobjects[][] = objects;
                           int tempRow = totalRows;
                            totalRows += 1;

                            objects = new String[totalRows][7];
                          for(i = 0 ; i < tempRow;i++)
                              for (j = 0; j < 7;j++)
                                  objects[i][j] = newobjects[i][j];

                            objects[totalRows - 1][0] = String.valueOf(id);
                            objects[totalRows - 1][1] = String.valueOf(userid);
                            objects[totalRows - 1][2] = String.valueOf(tableid);
                            objects[totalRows - 1][3] = reserveno;
                            objects[totalRows - 1][4] = String.valueOf(reservestatus);
                            objects[totalRows - 1][5] = reservedata;
                            objects[totalRows - 1][6] = String.valueOf(isdelete);
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
                       int userid = Integer.valueOf(useridTextField.getText().toString());
                       int tableid = Integer.valueOf(tableidTextField.getText().toString());
                       String reserveno = reservenoTextField.getText().toString();
                       int reservestatus = reservestatusBox.getSelectedIndex();
                       String reservedata = reservedataTextField.getText().toString();
                       int isdelete = isdeleteBox.getSelectedIndex();
                   String sql = "UPDATE reserve SET id = " + id + ",user_id="+userid+",table_id=" + tableid + ",reserve_no=\'" + reserveno + "\',reserve_status=" + reservestatus
                           + ",reserve_date=\'" + reservedata + "\',is_delete="+isdelete + " where id = " + id;
                       if(jdbcMySQL.update(sql) > 0) {
                           JOptionPane.showMessageDialog(null, "修改成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                           objects[selectRows][0] = String.valueOf(id);
                           objects[selectRows][1] = String.valueOf(userid);
                           objects[selectRows][2] = String.valueOf(tableid);
                           objects[selectRows][3] = reserveno;
                           objects[selectRows][4] = String.valueOf(reservestatus);
                           objects[selectRows][5] = reservedata;
                           objects[selectRows][6] = String.valueOf(isdelete);
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
                   String sql = "delete from reserve where id = " + id;
                   if(jdbcMySQL.deleteOrInsert(sql)> 0){
                       JOptionPane.showMessageDialog(null, "删除成功", "恭喜您", JOptionPane.INFORMATION_MESSAGE);
                       //前几行和他一样
                       String newobject[][] = new String[totalRows - 1][7];
                       for(i = 0; i < selectRows;i++)
                           for(j = 0; j < 7;j++)
                               newobject[i][j] = objects[i][j];
                       //向前移动一行
                       for( i = 0; i < 7;i++) {
                           for(j = selectRows; j < totalRows - 1;j++)
                               newobject[j][i] = objects[j+1][i];
                       }
                        totalRows--;
                         objects = new String[totalRows][7];
                       for(int i = 0; i < totalRows;i++)
                           for(int j = 0 ; j < 7;j++) {
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
        int isdelete = isdeleteBox.getSelectedIndex();
        int reservestatus = reservestatusBox.getSelectedIndex();
        if((isdelete == 1 || isdelete == 0)&&(reservestatus == 0 || reservestatus == 1 || reservestatus == 2||reservestatus == 3)){
            return true;
        }else
            return false;
    }
}
