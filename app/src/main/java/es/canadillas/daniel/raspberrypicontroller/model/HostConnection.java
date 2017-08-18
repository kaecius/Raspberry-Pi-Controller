package es.canadillas.daniel.raspberrypicontroller.model;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dani on 18/08/2017.
 */

public class HostConnection {

    private JSch jSch;
    private Host host;
    private Session session;

    public HostConnection(Host host) {
        this.host = host;
        this.jSch = new JSch();
        this.session = getSessionInstance();
    }

    private Session getSessionInstance() {
        Session session = null;
        try {
            session = jSch.getSession(this.host.getUser(),this.host.getHostUrl(),this.host.getPort());
            session.setPassword(this.host.getPassword());
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking","no");
            session.setConfig(properties);
            session.setTimeout(1000);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return  session;
    }


    private static Session getSession(String user,String password, String host, int port){
        Session session = null;
        try {
            JSch JSCH = new JSch();
            session = JSCH.getSession(user,host,port);
            session.setPassword(password);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking","no");
            session.setConfig(properties);
            session.setTimeout(1000);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }

    public static boolean testConnection(String user,String password, String host, int port){
        boolean result = false;

        Session session = getSession(user,password,host,port);
        try {
            session.setTimeout(2000);
            session.connect();
            result = true;
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public List<Service> getServices(){
        List<Service>  services = new ArrayList<>();
        String result = null;
        try{
            result = this.execute("sudo /usr/sbin/service --status-all");
            List<String> servicesName = splitServicesString(result);
            for (String s : servicesName) {
                Service service = new Service();
                service.setName(s);
                service.setActivated(isServiceActivated(s));
                services.add(service);
            }
        }catch (Throwable t){
            t.printStackTrace();
        }
        return services;
    }


    public String execute(String command)
    {
        StringBuilder outputBuffer = new StringBuilder();

        try
        {
            if (!session.isConnected()){
                session.connect();
                session = getSessionInstance();
            }
            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while(readByte != 0xffffffff)
            {
                outputBuffer.append((char)readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();
        }
        catch(IOException ioX)
        {

            return null;
        }
        catch(JSchException jschX)
        {

            return null;
        }

        return outputBuffer.toString();
    }

    private List<String> splitServicesString(String result) {
        List<String> services = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\s\\w+([-._]?)\\w+\\n");
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            String temp = matcher.group();
            services.add(temp.substring(0, temp.length() - 1));
        }
        return services;
    }

    public boolean isServiceActivated(String s) {
        boolean result = false;


        String commandResult = this.execute("/usr/sbin/service " + s + " status");

        if (commandResult != null){
            result = commandResult.contains("Active: active");
        }

        return result;
    }

}
