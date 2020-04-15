package ru.rickSanchez.company;
//создание чат сервера который имеет параметры включения и отключения
public class ChatServer {
    //запуск сервера
    public void start(int port){
        System.out.println("Server started at port: " + port);
    }

    //остановка сервера
    public void stop(){
        System.out.println("Server stopped");
    }
}
