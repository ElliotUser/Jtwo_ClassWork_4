package ru.rickSanchez.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler{

    //размер окна
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    //КОМПОНЕНТЫ

    //Это поля куда пишется текст. Это общий журнал чата
    private final JTextArea log = new JTextArea();
    //Это панелька на которой мы можем собират компоненты. Собирать мы их будем
    //сеточкой из двух строчек и трех столбиков
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    //Отличие от JTextArea в том, что JTextField это одна строчка,
    //a JTextArea многострочная. С ip адресом
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    //С портом. то куда мы как клиент будем подключаться
    private final JTextField tfPort = new JTextField("8189");
    //Представляет собой галочку в квадратике которую мы можем поставить или убрать.
    // Галочка будет устанавливаться для подтверждения того что окошко может находиться
    // поверх всех других окон.
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top", true);
    //Наш Ник
    private final JTextField tfLogin = new JTextField("ivan");
    //Наш пароль и будет отображаться в виде замаскированного поля
    private final JPasswordField tfPassword = new JPasswordField("123");
    //Кнопка для Логина
    private final JButton btnLogin = new JButton("Login");

    //Данная панелька будет находиться у нас внизу и на ней будут
    //расположены перечисленные ниже компоненты.
    private final JPanel panelBottom = new JPanel(new BorderLayout());
    //Кнопка отключения от чата
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    //Поле для ввода сообщения, которое далее будет выводиться в журнал сообщений
    private final JTextField tfMessage = new JTextField();
    //Сама кнопка при нажатии которой будет происходить передача сообщения в журнал(log)
    private final JButton btnSend = new JButton("Send");
    //Тут будет находиться список клиентов
    private final JList<String> userList = new JList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    File file = new File("History.txt");

    ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setAlwaysOnTop(true);
        userList.setListData(new String[]{"user1", "user2", "user3", "user4",
                "user5", "user6", "user7", "user8", "user9",
                "user-with-exceptionally-long-name-in-this-chat"});
        //скролим наши окошки для того чтобы данные не вылазили за поля
        JScrollPane scrUser = new JScrollPane(userList);
        JScrollPane scrLog = new JScrollPane(log);
        scrUser.setPreferredSize(new Dimension(100, 0));
        //перенос строк
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        //ничего нельзя вписать в лог вручную
        log.setEditable(false);
        //Отображения окна поверх других окон
        cbAlwaysOnTop.addActionListener(this);


        //Добавляем все на наши панели
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);

        //На кнопку добавляем дисконнект
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        //поле с сообщением
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        //отправка сообщения
        panelBottom.add(btnSend, BorderLayout.EAST);

        //при нажатии на кнопку отправляем сообщеник
        btnSend.addActionListener(this);
        //обработка событий от нажатия кнопки Enter
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        String msg = tfMessage.getText();
                        log.append(msg+"\n");
                        tfMessage.setText("");
                    try(FileWriter writer = new FileWriter(file, true)) {
                        writer.write(msg + "\n");
                        writer.flush();
                    } catch(FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        //Добавляем лог и наших клиентов
        add(scrUser, BorderLayout.EAST);
        add(scrLog, BorderLayout.CENTER);

        //добавляем панельки на окошко
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        }else if(src == btnSend ){
            String msg = tfMessage.getText();
            log.append(msg+"\n");
            tfMessage.setText("");
            try(FileWriter writer = new FileWriter(file, true)) {
                    writer.write(msg + "\n");
                    writer.flush();
                } catch(FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch(IOException ex) {
                    ex.printStackTrace();
            }
        }else {
            throw new RuntimeException("Unknown source:" + src);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
