package kj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kj.util.FileUtil;
import lombok.Data;
import lombok.extern.java.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Log
@Data
public class Doc {

    @JsonIgnore
    final File file;

    public static final String SEP = "/";

    String relativePath;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date       createdTime;

    String size;


    public Doc(String path) {
        this.file = new File(path);
        // 初始化相对路径
        this.relativePath = this.getAbsolutePath()
                .replace(FileUtil.getFileRootPath() + SEP, "");
        // 默认为启动时间
        this.createdTime = FileUtil.getStartTime();
        // 计算得到字符串形式容量
        this.size = this.file.isFile() ? getSizeString(this.file.length()) : "-";
    }

    public Doc(String path, Date createdTime) {
        this.file = new File(path);
        this.createdTime = createdTime;
        // 初始化相对路径
        this.relativePath = this.getAbsolutePath()
                .replace(FileUtil.getFileRootPath() + SEP, "");
        // 计算得到字符串形式容量
        this.size = this.file.isFile() ? getSizeString(this.file.length()) : "-";
    }


    public Doc(File f) {
        this(f.getAbsolutePath());
    }

    /**
     * 得到当前Doc的子项目信息
     * @return List存储所有Doc信息，null表示这是一个文件，[]表示这个文件夹没有子项目
     */
    @JsonIgnore
    public List<Doc> getChildDocs() {
        File[] files = this.file.listFiles();
        List<Doc> docList = new ArrayList<>();
        if (files == null) return docList;
        for (File file : files) {
            // 查询记录
            Doc r = FileUtil.readRecord(new Doc(file).getAbsolutePath());
            if(r != null) {
                docList.add(r);
            } else {
                // 如果没有记录，说明在服务器运行期间，通过本地模式访问资源目录
                // 写入记录
                FileUtil.addRecord(new Doc(file), FileUtil.getStartTime());
                log.warning("非法访问，试图直接访问本地目录！！！");
            }
        }
        return docList;
    }


    /**
     * 返回父文件夹的路径
     * @return 父文件夹的绝对路径
     */
    @JsonIgnore
    public String getParent() {
        return new Doc(this.file.getParent()).getAbsolutePath();
    }

    /**
     * 返回当前Doc的绝对路径
     * @return 绝对路径
     */
    @JsonIgnore
    public String getAbsolutePath() {
        return this.file.getAbsolutePath().replace(File.separator, SEP);
    }


    /**
     * 文件或文件夹是否真实存在
     * @return true / false
     */
    public boolean exists() {
        return this.file.exists();
    }

    public boolean isDirectory() {
        return this.file.isDirectory();
    }


    /**
     * 将传入的字节转换为带单位的容量
     *
     * @param bytes 字节数
     * @return 返回带单位的容量
     */
    public static String getSizeString(long bytes) {
        if (bytes < 0) return "";
        String[] units = new String[]{"B", "KiB", "MiB", "GiB", "TiB"};
        short index = 0;
        if (bytes <= 1024) return bytes + " " + units[index];
        double s = bytes;
        do {
            s /= 1024;
            index++;
        } while (s >= 1024);
        return String.format("%.2f", s) + " " + units[index];
    }

}
