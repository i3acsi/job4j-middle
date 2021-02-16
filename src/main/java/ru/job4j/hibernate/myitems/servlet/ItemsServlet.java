package ru.job4j.hibernate.myitems.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.hibernate.myitems.model.MyItem;
import ru.job4j.hibernate.myitems.model.User;
import ru.job4j.hibernate.myitems.service.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(urlPatterns = "/process")
public class ItemsServlet extends HttpServlet {
    private Store store;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        this.store = (Store) this.getServletContext().getAttribute("store");
        this.mapper = (ObjectMapper) this.getServletContext().getAttribute("mapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User author = (User) req.getSession().getAttribute("user");
        if (author != null) {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("json");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
            boolean check = Boolean.parseBoolean(req.getParameter("check"));
            List<MyItem> items = store.findAll(check);
            String json = mapper.writeValueAsString(items);
            writer.println(json);
            writer.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User author = (User) req.getSession().getAttribute("user");
        if (author != null) {
            String action = req.getParameter("action");
            if ("addItem".equals(action)) {
                String desc = req.getParameter("desc");
                store.add(new MyItem(desc, author));
            } else if ("markDone".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                store.makeDone(id);
            }
            resp.setContentType("json");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
            writer.println(mapper.writeValueAsString("ok"));
            writer.flush();
        }
    }
}