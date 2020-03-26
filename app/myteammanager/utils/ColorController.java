package com.app.myteammanager.utils;

import android.content.Context;

public class ColorController {


    private static ColorController colorController;

    private ColorController() {

    }

    public static ColorController getInstance() {
        if (colorController == null) {
            colorController = new ColorController();
        }
        return colorController;
    }

}
