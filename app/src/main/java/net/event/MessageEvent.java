package net.event;

import net.widget.DrawableStickerPixel;

/**
 * Copyright (C) 2021,2021/6/30, a Tencent company. All rights reserved.
 * <p>
 * User : v_xhangxie
 * <p>
 * Desc :
 */
public class MessageEvent {

    public final String[] message;
    public DrawableStickerPixel stickerPixel;

    public MessageEvent(String... message) {
        this.message = message;
    }

    public MessageEvent(DrawableStickerPixel stickerPixel, String... message){
        this.stickerPixel = stickerPixel;
        this.message = message;
    }

    public DrawableStickerPixel getStickerPixel() {
        return stickerPixel;
    }

    public String[] getMessage() {
        return message;
    }
}
