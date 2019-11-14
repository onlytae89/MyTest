package code;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author shkstart
 * @create 2019-11-13 10:41
 */
public class WebServer {
    private ServerSocket server;//创建服务器套接字                                     //1.4
    public WebServer() {                                                            //1.5
        try {
            System.out.println("启动服务器中···");
            server = new ServerSocket(8088);//启动端口
            System.out.println("启动服务器完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){                                                             //1.6
        try {
            System.out.println("等待客户端连接···");
            Socket socket = server.accept();//接收请求
            System.out.println("一个客户端连接了！");
            ClientHandler handler = new ClientHandler(socket);
            Thread t = new Thread(handler);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {                                          //1.7
        WebServer webServer = new WebServer();
        webServer.start();
    }
}
