package controllers;

import models.ImageEntry;

public class ImageResult
{
    public static final String IMG_PATH = "/i/";
    public ImageEntry entry;
    public String imageUrl;

    public ImageResult( final ImageEntry entry )
    {
        this.entry = entry;
        this.imageUrl = IMG_PATH + entry.id;
    }
}
