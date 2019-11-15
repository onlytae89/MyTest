package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author shkstart
 * @create 2019-11-14 20:49
 */
public class HttpResponse {
    //跟连接相关的信息                      //7.1
    private Socket socket;
    private OutputStream out;
    //状态行相关信息                       //7.1
    private String htmlVer = "HTTP/1.1";//状态行 响应协议版本
    private int statusCode = 200;//状态行状态码 默认成功200
    private String statusReason = "OK"; //状态描述 与状态码一致 OK
    private File entity;//响应正文的实体文件 该文件通常是用户请的实际资源文件
    public HttpResponse(Socket socket) {    ////7.1
        try {
            this.socket = socket; //通过socket获取输出流，用与给客户端发送消息
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void flush(){            //7.1
        System.out.println("HttpResponse:开始发送响应....");
        sendStatusLine();
        sendHeaders();
        sendContent();
        System.out.println("HttpResponse:发送响应完毕");
    }

    public void sendStatusLine() {          //7.1
        System.out.println("HttpResponse:开始发送状态行....");
        try{
            String line = htmlVer+" "+statusCode+" "+statusReason;
            out.write(line.getBytes( "ISO-8859-1" ) );
            out.write( 13 );
            out.write( 10 );
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("HttpResponse:发送状态行完毕");
    }
    private void sendHeaders(){         //7.1
        System.out.println("HttpResponse:开始发送响应头....");
        try {
            String line = "Conten-Type: text/html";
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//written CR
            out.write(10);//written LF
            line = "Content-Length: "+entity.length();
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//written CR
            out.write(10);//written LF
            //单独发送发送crlf表示响应头发送完毕
            out.write(13);//written CR
            out.write(10);//written LF
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendContent(){         //7.1
        System.out.println("HttpResponse:开始发送响应正文....");
        try( FileInputStream fis = new FileInputStream(entity)){//读取响应正文
            int len=-1;
            byte[]data = new byte[1024*10];
            while(((len=fis.read( data ))!=-1)){ //读取响应正文
                out.write( data,0,len);     //将响应正文写出到控制台
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("HttpResponse:开始发送响应正文....");
    }

    public File getEntity() { return entity; }
    public void setEntity(File entity) { this.entity = entity; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public String getStatusReason() { return statusReason; }
    public void setStatusReason(String statusReason) { this.statusReason = statusReason; }
    public String getHtmlVer() { return htmlVer; }
}
