package org.Simbot.utils;

import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片工具类
 */
public class ImageUtil {

    //自定义字体路径
    private static final String fontPath = "cache/custom.ttf";
    //文生图图片输出路径
    private static final String outputPath = "cache/tempImg/";

    private static final Canvas CANVAS = new Canvas();

    /**
     * 分割文本，比起正则表达式，这个方法的效率更高
     *
     * @param text     文本
     * @param font     字体
     * @param maxWidth 最大宽度
     * @return 分割后的文本
     */
    private static List<String> splitText(final String text, final Font font, final int maxWidth) {
        final AttributedString attributedText = new AttributedString(text);
        attributedText.addAttribute(TextAttribute.FONT, font);
        final LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(attributedText.getIterator(), new FontRenderContext(null, true, true));

        final List<String> lines = new ArrayList<>();
        while (lineBreakMeasurer.getPosition() < text.length()) {
            int endIndex = lineBreakMeasurer.nextOffset(maxWidth);
            String line = text.substring(lineBreakMeasurer.getPosition(), endIndex);

            // 处理换行符
            final int newLineIndex = line.indexOf("\n");
            if (newLineIndex != -1) {
                endIndex = lineBreakMeasurer.getPosition() + newLineIndex + 1;
                line = line.substring(0, newLineIndex);
            }

            lines.add(line);
            lineBreakMeasurer.setPosition(endIndex);
        }
        return lines;
    }

    /**
     * 把一段文本生成图片
     *
     * @param text     文本
     * @param maxWidth 最大宽度
     * @return 图片
     */
    @SneakyThrows
    public static ByteArrayInputStream createImage(final String text, final int maxWidth) {
//        outputPath = outputPath + imageName + ".png";
        final float fontSize = 26;//字体大小
        final Font font = loadFont(fontPath, fontSize);
        final FontMetrics fontMetrics = CANVAS.getFontMetrics(font);
        //计算文本的宽度
        final List<String> lines = splitText(text, font, maxWidth * 2);
        final int width = maxWidth * 2 + 40;//左右各20像素的边距
        final int height = (fontMetrics.getHeight() + 20) * lines.size() + 20;//上下各10像素的边距
        // 创建一个ByteArrayOutputStream,用于存放字节数据,指定大小为width*height*4,每个像素大约占用4个字节（RGBA）
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(width * height * 4);
        //创建图片,RGB比ARGB效率高
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = image.createGraphics();
        //抗锯齿,虽然会降低性能，但是图片效果会更清晰
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        //绘制文本
        for (int i = 0; i < lines.size(); i++) {
            graphics.drawString(lines.get(i), 20, 20 + (fontMetrics.getHeight() + 20) * i + fontMetrics.getAscent());
        }
        //释放资源
        graphics.dispose();
        //保存图片
//        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
//            ImageIO.write(image, "png", outputStream);
//        }
        // 将BufferedImage写入ByteArrayOutputStream
        ImageIO.write(image, "png", byteArrayOutputStream);
        // 使用字节数组创建ByteArrayInputStream
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     * 把一段文本生成图片
     *
     * @param text 文本
     * @return 图片
     */
    public static ByteArrayInputStream createImage(final String text) {
        return createImage(text, 500);
    }


    /**
     * 加载自定义字体
     *
     * @param fontPath 字体路径
     * @param fontSize 字体大小
     * @return 字体
     */
    @SneakyThrows
    public static Font loadFont(final String fontPath, final float fontSize) {
        final File fontFile = new File(fontPath);
        final Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        return font.deriveFont(fontSize);
    }

    /**
     * 删除图片
     *
     * @param imgName 图片名
     * @return 是否删除成功
     */
    public static boolean delImage(final String imgName) {
        return FileUtil.del("../../cache/tempImg/" + imgName + ".png");
    }
}