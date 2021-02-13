package ru.job4j.hibernate.myitems;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
        boolean check = Boolean.parseBoolean(req.getParameter("check"));
        List<MyItem> items = store.findAll(check);
        String json = mapper.writeValueAsString(items);
        writer.println(json);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Store store = (Store) this.getServletContext().getAttribute("store");
        ObjectMapper mapper = (ObjectMapper) this.getServletContext().getAttribute("mapper");
        String action = req.getParameter("action");
        if ("addItem".equals(action)) {
            String desc = req.getParameter("desc");
            System.out.println(desc);
            store.add(new MyItem(desc));
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