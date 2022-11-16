package kj.util;


import kj.entity.Doc;
import lombok.extern.java.Log;

import java.util.*;

@Log
public class FileUtil {


    /**
     * 统一启动时间
     */
    private static final Date startTime = new Date();

    static {
        init();
    }

    private FileUtil() {}


    private static Doc currentDir = new Doc(getFileRootPath());


    /**
     * 初始化当前文件对应时间表 <br/>
     * 结构如下：<br />
     * K : String - 相对地址 <br />
     * V : Date - 创建时间 <br />
     */
    public static void init() {
        // 更新当前文件夹的创建时间
        long start, end;
        log.info("获取到地址-时间匹配表，进行更新");
        start = System.currentTimeMillis();
        update(new Doc(getFileRootPath()));
        end = System.currentTimeMillis();
        log.info("更新完毕，耗时" + (end - start)+ "ms");
    }

    /**
     * 更新整个文件系统文件创建时间
     *
     * @param d Doc
     */
    public static void update(Doc d) {
        if(!d.getAbsolutePath().equals(getFileRootPath())) {
            log.info("当前更新地址：" + d.getAbsolutePath());
            // 写入记录
            Doc doc = readRecord(d.getAbsolutePath());
            if (doc == null) {
                //没有记录，写入一条记录，创建时间为启动时间
                addRecord(d, startTime);
                log.info("添加记录" + d.getAbsolutePath());
            }
        }
        // 遍历子目录
        List<Doc> children = d.getChildDocs();
        // 父对象是一个文件，或父对象是文件夹但为空，无需遍历
        if (!(children == null || children.isEmpty())) {
            for (Doc c : children) {
                update(c);
            }
        }
    }

    /**
     * 获取指定目录的子目录
     *
     * @param dir 网络请求参数
     * @return null表示这是文件或错误的请求参数；空的列表表示这是空目录；非空的列表就是当前目录的子元素
     */
    public static List<Doc> getChildList(String dir) {
        log.info("发送请求，当前路径为：" + currentDir.getRelativePath());
        log.info("请求dir参数：" + dir);
        // 首先得到请求的真实文件
        Doc doc = new Doc(convertPath(dir));
        // 当前dir是一个文件，没有子文件
        if (!doc.exists() || !doc.isDirectory()) return null;
        // 记录当前切换的目录
        currentDir = doc;
        log.info("切换到目录：" + currentDir.getRelativePath());
        return doc.getChildDocs();
    }

    /**
     * 通过前端请求的dir参数，返回本地的实际路径
     *
     * @param dir 网络请求参数
     * @return 实际地址
     */
    private static String convertPath(String dir) {
        if (dir == null || "".equals(dir.trim())) return null;
        String path;
        switch (dir) {
            case "root":
                path = getFileRootPath();
                break;
            case "parent":
                // 如果当前目录是根目录，同时又请求父级目录，直接返回根目录
                if (currentDir.getAbsolutePath().equals(getFileRootPath())) {
                    path = getFileRootPath();
                } else {
                    // 请求父目录
                    path = currentDir.getParent();
                }
                break;
            default:
                path = fixPath(dir);
                break;
        }
        return path;

    }

    /**
     * 将相对路径补充为绝对路径
     *
     * @param dir 相对路径
     * @return 绝对路径
     */
    private static String fixPath(String dir) {
        return getFileRootPath() + Doc.SEP + dir;
    }

    /**
     * 获取文件系统根目录
     *
     * @return 根目录地址
     */
    public static String getFileRootPath() {
        return PropertiesUtil.get("save.path.root", "./");
    }


    public static Date getStartTime() {
        return startTime;
    }


    /**
     * 写入一条记录
     *
     * @param doc 相对地址
     * @param date 时间
     */
    public static void addRecord(Doc doc, Date date) {
        doc.setCreatedTime(date);
        SqlUtil.doSqlWork(docMapper -> {
            int count = docMapper.addDoc(doc);
            log.info("写入记录数目" + count + "，" + doc);
        });
    }


    /**
     * 读取一条记录
     *
     * @param absolutePath 主键，绝对路径
     * @return 返回Doc对象，如果没有记录返回null
     */
    public static Doc readRecord(String absolutePath) {
        final Doc[] doc = new Doc[1];
        SqlUtil.doSqlWork(docMapper -> doc[0] = docMapper.getDocByAbsolutePath(absolutePath));
        log.info("读取记录" + doc[0]);
        return doc[0];
    }

    public static Doc getCurrentDir() {
        return currentDir;
    }

}
