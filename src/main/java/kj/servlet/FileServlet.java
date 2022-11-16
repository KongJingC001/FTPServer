package kj.servlet;

import kj.util.FileUtil;
import kj.util.JsonUtil;
import lombok.extern.java.Log;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@Log
@WebServlet(name = "file", value = "/file")
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dir = req.getParameter("dir");
        if(dir != null) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(JsonUtil.ListToJson(FileUtil.getChildList(dir)));
            log.info("请求<" + dir + ">的子目录");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<h1>未知请求</h1>");
            log.warning("未知的请求参数");
        }
    }
}
