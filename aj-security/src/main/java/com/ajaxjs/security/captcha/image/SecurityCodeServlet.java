package com.ajaxjs.security.captcha.image;


import com.ajaxjs.util.StrUtil;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * 可以旋转文字的验证码
 * <a href="https://blog.csdn.net/collonn/article/details/41724099">...</a>
 */
public class SecurityCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private int width = 173;
    private final int height = 24;
    private final int fontSize = height;
    private int securityCodeLength = 4;
    private int interferingLineCount = 20;
    private Font font = new Font("Times New Romans", Font.BOLD, fontSize);
    private String charStr = "A0KLBMNC2PD3QRE4STF5UVG6WXH7YZ8J9秋花惨淡秋草黄耿耿秋灯秋夜长已觉秋窗秋不尽那堪风雨助凄凉";
    private String[] chars;
    private int charWidth;
    private int charHeight;

    @Override
    public void init() {
        try {
            Properties config = new Properties();
            config.load(SecurityCodeServlet.class.getResourceAsStream("/config.properties"));

            String withStr = config.getProperty("security.code.width");
            if (StrUtil.hasText(withStr)) width = Integer.parseInt(withStr);

            String securityCodeLengthStr = config.getProperty("security.code.length");
            if (StrUtil.hasText(securityCodeLengthStr))
                securityCodeLength = Integer.parseInt(securityCodeLengthStr);

            String interferingLineCountStr = config.getProperty("security.code.interfering.line.count");
            if (StrUtil.hasText(interferingLineCountStr))
                this.interferingLineCount = Integer.parseInt(interferingLineCountStr);

            String fontStr = config.getProperty("security.code.fontStyle");
            if (StrUtil.hasText(fontStr)) {
                if (!fontStr.contains(".ttf"))
                    font = new Font(fontStr, Font.BOLD, fontSize);
                else font = registerFont(fontStr);
            }

            String charsStr = config.getProperty("security.code.text");
            if (StrUtil.hasText(charsStr))
                this.charStr = charsStr;

            chars = new String[charsStr.length()];
            for (int i = 0; i < charsStr.length(); i++)
                chars[i] = String.valueOf(charsStr.charAt(i));

            FontMetrics fontMetrics = FontDesignMetrics.getMetrics(font);
            charWidth = fontMetrics.stringWidth("M");
            charHeight = fontMetrics.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Random random = new Random();
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffImg.createGraphics();
            g.setFont(font);

            //画背景
            g.setColor(getRandomColor(random, 200, 55));
            g.fillRect(0, 0, width, height);
            //画边框
            g.setColor(getRandomColor(random, 100, 155));
            g.drawRect(0, 0, width - 1, height - 1);

            //画干扰线
            g.setColor(getRandomColor(random, 0, 255));
            g.setStroke(new BasicStroke(1f));

            for (int i = 0; i < interferingLineCount; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(width);
                int yl = random.nextInt(height);
                g.drawLine(x, y, x + xl, y + yl);
            }

            //画旋转文字
            int charX = 0;
            List<String> chartList = this.getRandomString(random);
            int charsRealWidth = this.charWidth * this.securityCodeLength;
            if (this.width > charsRealWidth) charX = (this.width - charsRealWidth) / 2;

            double radianPercent;
            int chartY = this.height - 5;

            for (String chart : chartList) {
                g.setColor(this.getRandomColor(random, 80, 120));
                radianPercent = Math.PI * (random.nextInt(60) / 180D);
                if (random.nextBoolean()) radianPercent = -radianPercent;
                g.rotate(radianPercent, charX + 9, chartY);
                g.drawString(chart, charX, chartY);
                g.rotate(-radianPercent, charX + 9, chartY);
                charX += this.charWidth;
            }

            //释放此图形的上下文以及它使用的所有系统资源
            g.dispose();

            //设置response类型
            response.setContentType("image/jpeg");
            //取消缓存
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            //输出图像
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(buffImg, "jpeg", os);

            os.close();

            //设置Session，将字符串转换成小写
            request.getSession().setAttribute(SECURITY_CODE_SESSION_KEY, String.join("", chartList).toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static final String SECURITY_CODE_SESSION_KEY = "SECURITY_CODE_SESSION_KEY";

    private Font registerFont(String fontStr) throws Exception {
        InputStream fontInputStream = SecurityCodeServlet.class.getClassLoader().getResourceAsStream(fontStr);
        Font fontNew = Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
        Font fontNewPt = fontNew.deriveFont(Font.BOLD, this.fontSize);
        fontInputStream.close();

        //注意这里，如果不注册文字的话，什么都画不出来
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(fontNew);

        return fontNewPt;
    }

    private List<String> getRandomString(Random random) {
        List<String> chartList = new LinkedList<>();

        for (int i = 0; i < this.securityCodeLength; i++) {
            String character = this.chars[random.nextInt(this.chars.length)];
            character = (random.nextBoolean() == true ? character.toUpperCase() : character.toLowerCase());
            chartList.add(character);
        }

        return chartList;
    }

    private Color getRandomColor(Random random, int start, int max) {
        int r = start + random.nextInt(max);
        int g = start + random.nextInt(max);
        int b = start + random.nextInt(max);

        return new Color(r, g, b);
    }
}