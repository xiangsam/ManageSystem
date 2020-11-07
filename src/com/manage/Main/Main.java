package com.manage.Main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//主界面
public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    private int WINDOW_WIDTH = 1000;
    private int WINDOW_HEIGHT = 600;
    //左侧的导航栏
    private JPanel navigatorPanel;
    //四个面板
    private BalancePanel balancePanel;
    private ReservePanel reservePanel;
    private DishPanel dishPanel;
    private PasswordPanel passwordPanel;
    //设置四个label 为nav所有 分别为处理记录，查询记录，重置密码，菜单管理
    private JLabel reserveLabel, balanceLabel,passwdLabel,dishLabel,newLabel;
    //桌面图片
    private JLabel imageLabel;
    //设置上方的菜单栏
    private  JMenuBar menuBar;
    //外观菜单
    private JMenu preMenu;

    //外观菜单的item  3个风格的item
  private  JRadioButtonMenuItem  metalItem,metifItem,windowItem;
    public Main(){
        ImageIcon icon = new ImageIcon("/src/background.jpg");
        imageLabel = new JLabel(icon);
        //设置一些默认值
        // set title
        this.setTitle("后台管理系统");

        // set size for the form
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // set layout for the frame
        this.setLayout(new BorderLayout(10, 10));

        // specify the operation for the close button
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // build navigator panel
        buildNavigatorPanel();

        // build first panel and second panel
        // 把要显示的两个面板对象都建立起来
        buildOtherPanels();

        // add the created panels to the frame
        //加载panel
        this.add(navigatorPanel, BorderLayout.WEST);

        // 把要显示的两个面板对象到放到相同区域，在窗体中显示的为最后放入的面板。
        // 这样做的目的是把两个对象都和该窗体关联起来。只有关联起来，之后应用外观风格（例如Motif）才会作用于这两个面板对象。

        this.add(reservePanel, BorderLayout.CENTER);
        this.add(passwordPanel,BorderLayout.CENTER);
        this.add(balancePanel, BorderLayout.CENTER);
        this.add(dishPanel, BorderLayout.CENTER);
         this.add(imageLabel,BorderLayout.CENTER);
        // show the window


        this.setVisible(true);

    }


    public void buildNavigatorPanel() {
        // create a panel for navigator labels
        navigatorPanel = new JPanel();

        // 设置边框来控制外观以及上下左右边距
        Border insideBorder = BorderFactory.createEmptyBorder(20, 0, 0, 0);
        Border outsideBorder = BorderFactory.createLoweredBevelBorder();
        navigatorPanel.setBorder(BorderFactory.createCompoundBorder(
                outsideBorder, insideBorder));

        // set the size for the navigator panel
        navigatorPanel.setPreferredSize(new Dimension(100, 500));
        //4个label的创建
        // create label objects for navigator
        reserveLabel = new JLabel("预约处理", SwingConstants.CENTER);
        reserveLabel.setPreferredSize(new Dimension(100, 30));
        reserveLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 当鼠标移到标签上时显示手型图标

        balanceLabel = new JLabel("订单处理", SwingConstants.CENTER);
        balanceLabel.setPreferredSize(new Dimension(100, 30));
        balanceLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 当鼠标移到标签上时显示手型图标

        dishLabel = new JLabel("菜单管理", SwingConstants.CENTER);
        dishLabel.setPreferredSize(new Dimension(100, 30));
        dishLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 当鼠标移到标签上时显示手型图标

        passwdLabel = new JLabel("重置密码", SwingConstants.CENTER);
        passwdLabel.setPreferredSize(new Dimension(100, 30));
        passwdLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 当鼠标移到标签上时显示手型图标

        newLabel = new JLabel("刷新", SwingConstants.CENTER);
        newLabel.setPreferredSize(new Dimension(100, 30));
        newLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // add the labels to the panel
        navigatorPanel.add(reserveLabel);
        navigatorPanel.add(balanceLabel);
        navigatorPanel.add(dishLabel);
        navigatorPanel.add(passwdLabel);
        navigatorPanel.add(newLabel);

        // register action listener for the navigator labels
        reserveLabel.addMouseListener(new LabelClick());
        balanceLabel.addMouseListener(new LabelClick());
        passwdLabel.addMouseListener(new LabelClick());
        dishLabel.addMouseListener(new LabelClick());
        newLabel.addMouseListener(new LabelClick());
    }

    public void buildOtherPanels() {
        // 下面创建面板对象，它们将会显示在同一个区域，但同一时刻只有一个可见
        balancePanel = new BalancePanel();
        balancePanel.setVisible(false);

        reservePanel = new ReservePanel();
        reservePanel.setVisible(true);

        passwordPanel = new PasswordPanel();
        passwordPanel.setVisible(false);

        dishPanel = new DishPanel();
        dishPanel.setVisible(false);
        //加载菜单栏
        menuBar = new JMenuBar();
        //创建系统外观菜单
        buildPreMenu();
        menuBar.add(preMenu);
        setJMenuBar(menuBar);

    }


//创建系统外观菜单
public void buildPreMenu() {
    preMenu = new JMenu("系统外观");
    metalItem = new JRadioButtonMenuItem("Metal风格");
    metifItem = new JRadioButtonMenuItem("Metif风格");
    windowItem = new JRadioButtonMenuItem("Windows风格");
    ButtonGroup group = new ButtonGroup();
    group.add(metalItem);
    group.add(metifItem);
    group.add(windowItem);
    metalItem.setSelected(true);
    preMenu.add(metalItem);
    preMenu.add(metifItem);
    preMenu.add(windowItem);
    PreMenuItemListener itemListener = new PreMenuItemListener();
    metifItem.addActionListener(itemListener);
    metalItem.addActionListener(itemListener);
    windowItem.addActionListener(itemListener);

}
//四个label的点击事件 切换不同的panel
    public class LabelClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getSource() == reserveLabel) {
                imageLabel.setVisible(false);
                // the label clicked is set to red, other labels are black
                reserveLabel.setForeground(Color.red);
                balanceLabel.setForeground(Color.black);
                dishLabel.setForeground(Color.black);
                passwdLabel.setForeground(Color.black);
                newLabel.setForeground(Color.BLACK);
                // show the first panel
                // 把要显示的面板放入指定区域
                if (!reservePanel.isVisible()) {
                    Main.this.add(reservePanel, BorderLayout.CENTER);
                    reservePanel.setVisible(true);
                }
                // 原先显示的面板设置为不可见
                balancePanel.setVisible(false);
                dishPanel.setVisible(false);
                passwordPanel.setVisible(false);
            } else if (e.getSource() == balanceLabel) {
                imageLabel.setVisible(false);
                // the label clicked is set to red, other labels are black
                balanceLabel.setForeground(Color.red);
                reserveLabel.setForeground(Color.black);
                dishLabel.setForeground(Color.black);
                passwdLabel.setForeground(Color.black);
                newLabel.setForeground(Color.BLACK);
                // show the second panel
                if (!balancePanel.isVisible()) {
                    Main.this.add(balancePanel, BorderLayout.CENTER);
                    balancePanel.setVisible(true);
                }
                reservePanel.setVisible(false);
                dishPanel.setVisible(false);
                passwordPanel.setVisible(false);
            }else if(e.getSource() == dishLabel){
                imageLabel.setVisible(false);
                // the label clicked is set to red, other labels are black
                dishLabel.setForeground(Color.red);
                reserveLabel.setForeground(Color.black);
                balanceLabel.setForeground(Color.black);
                passwdLabel.setForeground(Color.black);
                newLabel.setForeground(Color.BLACK);
                // show the second panel
                if (!dishPanel.isVisible()) {
                    Main.this.add(dishPanel, BorderLayout.CENTER);
                    dishPanel.setVisible(true);
                }
                reservePanel.setVisible(false);
                balancePanel.setVisible(false);
                passwordPanel.setVisible(false);
            }else if(e.getSource() == passwdLabel){
                imageLabel.setVisible(false);
                // the label clicked is set to red, other labels are black
                passwdLabel.setForeground(Color.red);
                reserveLabel.setForeground(Color.black);
                dishLabel.setForeground(Color.black);
                balanceLabel.setForeground(Color.black);
                newLabel.setForeground(Color.BLACK);
                // show the second panel
                if (!passwordPanel.isVisible()) {
                    Main.this.add(passwordPanel, BorderLayout.CENTER);
                    passwordPanel.setVisible(true);
                }
                reservePanel.setVisible(false);
                balancePanel.setVisible(false);
                dishPanel.setVisible(false);
            }else if(e.getSource() == newLabel){
                imageLabel.setVisible(false);
                // the label clicked is set to red, other labels are black
                passwdLabel.setForeground(Color.black);
                reserveLabel.setForeground(Color.black);
                dishLabel.setForeground(Color.black);
                balanceLabel.setForeground(Color.black);
                newLabel.setForeground(Color.RED);
                reservePanel.setVisible(false);
                balancePanel.setVisible(false);
                dishPanel.setVisible(false);
                passwordPanel.setVisible(false);
                remove(reservePanel);
                remove(balancePanel);
                remove(dishPanel);
                remove(passwordPanel);
                reservePanel = new ReservePanel();
                balancePanel = new BalancePanel();
                dishPanel = new DishPanel();
                passwordPanel = new PasswordPanel();
                add(reservePanel);
                add(balancePanel);
                add(dishPanel);
                add(passwordPanel);
                reservePanel.setVisible(true);
                balancePanel.setVisible(false);
                dishPanel.setVisible(false);
                passwordPanel.setVisible(false);
            }
        }
    }

    //外观菜单栏下的item的点击事件
    private  class PreMenuItemListener  implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent ex) {
            if(ex.getSource() == metalItem){
                //设置窗体风格
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(navigatorPanel);
                    SwingUtilities.updateComponentTreeUI(menuBar);
                    SwingUtilities.updateComponentTreeUI(reservePanel);
                    SwingUtilities.updateComponentTreeUI(balancePanel);
                    SwingUtilities.updateComponentTreeUI(dishPanel);
                    SwingUtilities.updateComponentTreeUI(passwordPanel);

                } catch(Exception e){
                    JOptionPane.showMessageDialog(null,
                            "Error setting the look and feel.");
                    System.exit(0);

                }
            }
            if(ex.getSource() == metifItem) {
                try {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(navigatorPanel);
                    SwingUtilities.updateComponentTreeUI(menuBar);
                    SwingUtilities.updateComponentTreeUI(reservePanel);
                    SwingUtilities.updateComponentTreeUI(balanceLabel);
                    SwingUtilities.updateComponentTreeUI(dishPanel);
                    SwingUtilities.updateComponentTreeUI(passwordPanel);

                } catch(Exception e){
                    JOptionPane.showMessageDialog(null,
                            "Error setting the look and feel.");
                    System.exit(0);

                }
            }
            if(ex.getSource() == windowItem){
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(navigatorPanel);
                    SwingUtilities.updateComponentTreeUI(menuBar);
                    SwingUtilities.updateComponentTreeUI(reservePanel);
                    SwingUtilities.updateComponentTreeUI(balancePanel);
                    SwingUtilities.updateComponentTreeUI(dishPanel);
                    SwingUtilities.updateComponentTreeUI(passwordPanel);

                } catch(Exception e){
                    JOptionPane.showMessageDialog(null,
                            "Error setting the look and feel.");
                    System.exit(0);

                }
            }
        }
    }
    public static void main(String[] args) {
      new Main();
    }

}
