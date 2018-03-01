package com.xq.live.common;

import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

/**
 * 二维码图片对象
 *
 * @author zhangpeng32
 * @date 2018-02-28 17:18
 * @copyright:hbxq
 **/
public class TwoQRCodeImage implements QRCodeImage {

    BufferedImage bufImg;

    public TwoQRCodeImage(BufferedImage bufImg) {
        this.bufImg = bufImg;
    }

    @Override
    public int getHeight() {
        return bufImg.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        return bufImg.getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return bufImg.getWidth();
    }
}
