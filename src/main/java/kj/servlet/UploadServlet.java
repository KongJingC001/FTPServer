package kj.servlet;

import kj.entity.Doc;
import kj.util.FileUtil;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Log
@MultipartConfig
@WebServlet(name = "upload", value = "/upload")
public class UploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part part = request.getPart("upload");
        log.info("接收到文件" + part.getSubmittedFileName());
        String savePath = FileUtil.getCurrentDir().getAbsolutePath() + Doc.SEP + part.getSubmittedFileName();
        log.info("上传到服务器地址：" + savePath);
        try (FileOutputStream stream = new FileOutputStream(savePath)) {
            IOUtils.copy(part.getInputStream(), stream);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<h2>上传文件成功</h2>");
        }
        // 上传文件完毕后，需要更新地址-时间表
        FileUtil.addRecord(new Doc(savePath), new Date());
    }
}
