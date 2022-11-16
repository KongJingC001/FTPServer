package kj.servlet;

import kj.entity.Doc;
import kj.util.FileUtil;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log
@WebServlet(name = "download", value = "/download")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("filename");
        log.info("请求下载的文件名：" + fileName);
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        String filePath = FileUtil.getFileRootPath() + Doc.SEP + fileName;
        log.info("文件地址：" + filePath);
        try(InputStream inputStream = Files.newInputStream(Paths.get(filePath));
            OutputStream outputStream = resp.getOutputStream()) {
            //直接使用copy方法完成转换
            IOUtils.copy(inputStream, outputStream);
            log.info("服务器完成发送");
        }
    }
}
