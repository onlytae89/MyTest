package http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2019-11-13 13:04
 */
public class HttpReuquest {
    private Socket socket;//通过Socket获得输入流      //3.1
    private InputStream in;//输入流                  //3.2
    //    请求行属性                                      //3.3
    private String method;//请求方法
    private String uri;////请求资源的抽象路径（URL中的抽象路径部分）
    private String protocol;//协议版本
    private Map<String,String> headers = new HashMap<>();//    消息头相关信息          //3.8
    public HttpReuquest(Socket socket) {            //3.4
        try {
            in = socket.getInputStream();//获取输入流
            this.socket = socket;
            parseRequestLine();
            parseHeaders();
            parseContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseRequestLine(){ // 请求行                                //3.5
        System.out.println("HttpRequest：开始解析请求行...");
        try {                                                               //3.5
            String line = readline();
            System.out.println("请求行内容：" + line);
            String [] data = line.split( "\\s" );
            method=data[0];
            uri=data[1];
            protocol=data[2];
            System.out.println("method" + method);
            System.out.println("uri" + uri);
            System.out.println("protocol" + protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest：解析请求行完毕！");
    }
    private String readline() throws IOException {                                //3.6
        InputStream in = socket.getInputStream();//创建输入流将读取结果输入到控制台
        int len =-1;//实际读取长度
        char c1 ='a',c2='a';//c1表示上次读取到的字符 c2表示本次读取到字符              //2.1
        StringBuilder sb = new StringBuilder();                                   //2.2
        while((len = in.read())!=-1){
            //2.3
            c2 =(char)len;                              //4.1
            if(c1==13&&c2==10) { break; }
            sb.append(c2);
            c1=c2;//把本次读到的字符记录为上次读取的字符
        }
        return sb.toString().trim();//取出CRLF（空白)
    }
    private void parseHeaders(){//  消息头                               //3.7
        System.out.println("HttpRequest：开始解析消息头...");
        try {
            while(true){
                String line = readline();
                if("".equals( line )){
                    break; //死循环后最后一行出现了空字符串，因为CRLF也拼接上了sb后面readline方法返回时又调了trim（）方法
                }
                System.out.println("parseHeaders" + line);
                String[] data = line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:" + headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest：解析消息头完毕！");
    }
    private void parseContent(){  //    解析消息正文                  //3.8
        System.out.println("HttpRequest：开始解析消息正文...");
        System.out.println("HttpRequest:解析消息正文完毕！");
    }

    public String getMethod() { return method; }                    //3.9
    public String getUri() { return uri; }
    public String getProtocol() { return protocol; }
    public String getHeader(String name){//获取指定的消息头             //3.10
        return headers.get(name);
    }
}
