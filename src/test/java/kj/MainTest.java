package kj;

import kj.util.FileUtil;
import kj.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.File;


public class MainTest {

    @Test
    public void fuTest() {
        System.out.println(FileUtil.getChildList("."));
        System.out.println(FileUtil.getChildList("Personal/SHANGRI-LA GROUP"));
    }

    @Test
    public void jsonTest() {
        System.out.println(JsonUtil.ListToJson(FileUtil.getChildList("root")));
    }

    @Test
    public void fileTest() {
        File f = new File("E:/Database/README.md");
        File d = new File("E:/Database/ebook");
        System.out.println(f.length());
        System.out.println(d.length());
        System.out.println(f.getParent());
    }


    @Test
    public void utilTest() {
        FileUtil.getChildList("University\\HBEU\\DoubleDegree");
        FileUtil.getChildList("parent");
    }

    @Test
    public void fTimeTest() {
        File f = new File("E://Database/README.md");
    }

}
