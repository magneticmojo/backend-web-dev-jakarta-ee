package com.example.graphicsservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Generates a png image of a 3x3 grid with cells filled with randomly chosen pastel colors.
 * The grid size and the cell size can be adjusted through the constants {@code GRID_SIZE} and {@code CELL_SIZE}.
 * Colors can be added or removed from the {@code pastelColors} array.
 * The servlet responds to HTTP GET requests and sets the response content type as "image/png".
 *
 * @author Björn Forsberg
 */

@WebServlet(name = "graphicsGridServlet", value = "/")
public class GraphicsGridServlet extends HttpServlet {

    private static final int GRID_SIZE = 3;
    private static final int CELL_SIZE = 100;
    private static final int width = GRID_SIZE * CELL_SIZE;
    private static final int height = GRID_SIZE * CELL_SIZE;
    private Random random = new Random();

    private Color[] pastelColors = {
            new Color(255, 179, 186), new Color(255, 223, 186), new Color(255, 255, 186),
            new Color(186, 255, 201), new Color(186, 225, 255), new Color(186, 186, 255),
            new Color(255, 186, 255),
    };

    /**
     * Handles GET requests and generates an image of a grid.
     *
     * @param request  the incoming HTTP request
     * @param response the outgoing HTTP response
     * @throws IOException if an error occurs during image generation or writing to the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        generateImage(response);
    }

    /**
     * Generates an image with a grid and writes it to the response.
     *
     * @param response the outgoing HTTP response
     * @throws IOException if an error occurs during image writing
     */
    private void generateImage(HttpServletResponse response) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Draw each cell in the grid
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                drawCell(g2d, row, col);
            }
        }

        g2d.dispose();

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        os.close();
    }

    /**
     * Draws an individual cell of the grid with a random pastel color.
     *
     * @param g2d Graphics2D object for drawing
     * @param row Row index of the cell
     * @param col Column index of the cell
     */
    private void drawCell(Graphics2D g2d, int row, int col) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;

        Color color = pastelColors[random.nextInt(pastelColors.length)];
        g2d.setColor(color);
        g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    }
}

