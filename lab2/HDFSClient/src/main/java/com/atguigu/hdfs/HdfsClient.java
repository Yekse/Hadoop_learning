package com.atguigu.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class HdfsClient{

    private FileSystem fs;
    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        //连接的集群nn地址
        String str = "hdfs://hadoop102:8020";
        URI uri = new URI(str);
        //创建一个配置文件
        Configuration configuration = new Configuration();

        configuration.set("dfs.replication", "2");

        //用户
        String user = "atguigu";

        //1 获取到了客户端对象
        fs = FileSystem.get(uri, configuration, user);
    }


    @Test
    public void testmkdir() throws URISyntaxException, IOException, InterruptedException {

        //创建一个文件夹
        String pathString = "/Lab2";
        fs.mkdirs(new Path(pathString));
    }

    @Test
    public void program1() throws IOException {
        String pathString1 = "C:\\Users\\HP\\Desktop\\bit\\大三下\\big data\\prog1.txt";
        String pathString2 = "hdfs://hadoop102/Lab2";
        String pathString3 = "hdfs://hadoop102/Lab2/prog1.txt";

        Path path_win_file = new Path(pathString1);
        Path path_hdfs = new Path(pathString2);
        Path path_hdfs_file = new Path(pathString3);

        if (fs.exists(path_hdfs_file)) {

            System.out.println("the file exists on HDFS,do you want (a)ppend or (o)verwrite?");
            Scanner input = new Scanner(System.in);
            String s=input.nextLine();

            if(s.equals("o")){
                System.out.println("overwrite");
                fs.copyFromLocalFile(false, true, path_win_file, path_hdfs);
            }
            else if(s.equals("a")) {
                System.out.println("append");
                FileInputStream in = new FileInputStream(path_win_file.toString());
                FSDataOutputStream app_f = fs.append(path_hdfs_file);
                byte[] data = new byte[in.available()];
                int read = -0;
                while ((read = in.read(data)) != -1) {
                    app_f.write(data, 0, read);
                }
            }
        }
        else {
            fs.copyFromLocalFile(false, path_win_file, path_hdfs);
        }
    }

    @Test
    public void program2() throws IOException {

        String pathString1 = "C:\\Users\\HP\\Desktop\\bit\\大三下\\big data\\prog2.txt";
        String pathString2 = "C:\\Users\\HP\\Desktop\\bit\\大三下\\big data";
        String pathString3 = "hdfs://hadoop102/Lab2/prog2.txt";

        Path path_win_file = new Path(pathString1);
        Path path_win = new Path(pathString2);
        Path path_hdfs_file = new Path(pathString3);

        File dstFile=new File(path_win_file.toString());

        if(!dstFile.exists()){
            //不存在
            fs.copyToLocalFile(path_hdfs_file,path_win);
        }
        else{
            //存在
            Path new_path_win = new Path(path_win.toString()+"\\prog2_new.txt");
            fs.copyToLocalFile(path_hdfs_file, new_path_win);
        }
    }
    @After
    public void close() throws IOException {

        //关闭资源
        fs.close();
    }

}


