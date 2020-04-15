package ru.rickSanchez.company;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {
    //координаты левого верхнего угла окна
    private static final int POS_X = 1000;
    private static final int POS_Y = 550;
    //размеры окна
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;

    private final ChatServer server;
    //Создание кнопок с названиями
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");

    public static void main(String[] args) {
        //запуск объекта ServerGUI() в отдельном потоке исполнения
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
//        throw new RuntimeException("Main died!");
        System.out.println("Main finished");
    }

    ServerGUI() {
        //В этом потоке устанавлеваем данный метод. Мы устанавливаем обработчик
        //непойманных исключений по умолчанию для текущего потока. И реализуем
        //метод uncaughtException(Thread t, Throwable e);

        Thread.setDefaultUncaughtExceptionHandler(this);
        //закрыть окно
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //установление положения фрейма
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        //изменение размера фрейма
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
        //определяет, каким образом на форме будут располагаться компоненты.И выбираем
        //GridLayout - табличное расположение(сетка).
        setLayout(new GridLayout(1, 2));

        //добовляем поведение и вешаем слушателя на кнопки
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);

        //добавляем кнопки на GridLayout
        add(btnStart);
        add(btnStop);
        //создание объекта для взаимодействия с ним
        server = new ChatServer();
        //делаем окно видимым
        setVisible(true);
    }

    //Реализуем интерфейс ActionListener(Слушатель для обработки событий от кнопки).
    //У данного интерфейса есть единственный метод actionPerformed(ActionEvent e)
    //И по этому мы можем передаем методу btnStart.addActionListener(this) this в качестве
    // аргумента; Объект данного класса.

    @Override
    public void actionPerformed(ActionEvent e) {
        //Как нам разлечить объект которые сюда попали
        Object src = e.getSource();
        if (src == btnStart) {
            server.start(8189);
        } else if (src == btnStop) {
//            server.stop();
            throw new RuntimeException("Hello from EDT!");
        } else {
            //напоминалка для другой кнопки
            throw new RuntimeException("Unknown source:" + src);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //в консоль выведем
        e.printStackTrace();
        //Сформируем сообщение которое будет состоять из StackTrace элементов
        String msg;
        //И данный массив можно забрать у нашего Throwable е
        StackTraceElement[] ste = e.getStackTrace();
        //присваеваем строке данное сообщение с выводом перечисленной ниже информации
        msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);

        //С помошью JOptionPane выводим диологовое окно, которому передаем
        //перечисленные аргументы.
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        //наша программа завершилось нештатно(возникли какие-то ошибки)
        System.exit(1);
    }
}