package code;
import http.HttpReuquest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;//创建客户端套接字                                            //1.1
    public ClientHandler(Socket socket) { this.socket = socket; }                     //1.2
    public void run(){                                                                //1.3
        try{
//            InputStream in = socket.getInputStream();//创建输入流将读取结果输入到控制台
//            int len =-1;//实际读取长度
//            char c1 ='a',c2='a';//c1表示上次读取到的字符 c2表示本次读取到字符              //2.1
//            StringBuilder sb = new StringBuilder();                                   //2.2
//            while((len = in.read())!=-1){                                             //2.3
//                if(c1==13&&c2==10) { break; }
//                sb.append(c2);
//                c1=c2;//把本次读到的字符记录为上次读取的字符
//                System.out.println(sb.toString().trim());//取出CRLF（空白)
            System.out.println("ClientHandler:开始处理");
//            1、解析请求
            System.out.println("ClientHandler:开始解析请求");
//            2、处理请求
            HttpReuquest request = new HttpReuquest(socket);
            String path = request.getUri();//根据请求请求对象获取用户请求的资源的路径    //5.1
            System.out.println("path" + path);                                      //5.2
            File file =new File("./webapps"+path);                         //5.3
            if(file.exists()){                                                      //5.4
                System.out.println("文件已存在");
                OutputStream out  =socket.getOutputStream();//通过socket获取输出流，用与给客户端发送消息
                String line = "HTTP/1.1 200 OK";        //1 发送状态行
                out.write( line.getBytes( "ISO8859-1" ) );
                out.write( 13 );//written CR
                out.write( 10 );//written LF
                line="Conten-Type: text/html";           //2 发送响应头
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);//written CR
                out.write(10);//written LF
                line = "Conten-Length: "+file.length();
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);//written CR
                out.write(10);//written LF
                //单独发送发送crlf表示响应头发送完毕
                out.write(13);//written CR
                out.write(10);//written LF
                //3 发送响应正文
                FileInputStream fis = new FileInputStream( file );
                int len = -1;
                byte[] data = new byte[1024*10];
                while((len=fis.read( data ))!=-1){
                    out.write( data,0,len );
                }
            }else{
                System.out.println("文件不存在");
            }
            System.out.println("ClientHandler：解析请求完毕");                        //3.7
//            3、响应客户端
            System.out.println("ClientHandler:处理完毕");
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {/*当响应完客户端后，与客户端断开连接此种操作是HTTP1.0的通讯方式，后期如果改为1.1可以在一次连接中进行多次请求与响应时就无需做这个操作了*/
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
