package ru.job4j.hibernate.myitems.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.hibernate.myitems.model.Category;
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

@WebServlet(urlPatterns = "/cat")
public class CategoryServlet extends HttpServlet {
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
        List<Category> categories = store.findAllCategories();
        String json = mapper.writeValueAsString(categories);
        writer.println(json);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryName = req.getParameter("categoryName");
        store.add(Category.of(categoryName));
        resp.setContentType("json");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
        writer.println(mapper.writeValueAsString("ok"));
        writer.flush();
    }
}
