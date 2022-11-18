package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {


    public static final String API_POSTS = "/api/posts";
    public static final String API_POSTS_D = "/api/posts/\\d+";
    public static final String STR = "/";
    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);

        controller = (PostController) context.getBean("postController");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var path = req.getRequestURI();
        if (path.equals(API_POSTS)) {
            controller.save(req.getReader(), resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var path = req.getRequestURI();
        if (path.equals(API_POSTS)) {
            controller.all(resp);
            return;
        } else if (path.matches(API_POSTS_D)) {
            final var id = parseId(path);
            controller.getById(id, resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var path = req.getRequestURI();
        if (path.matches(API_POSTS_D)) {
            final var id = parseId(path);
            controller.removeById(id, resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private long parseId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf(STR) + 1));
    }
}