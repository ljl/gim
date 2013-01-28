package models;

import models.ImageEntry;

public class WSResponse
{
    public String type;

    public WSResponse(String type) {
        this.type = type;
    }

    public static class ImageResponse extends WSResponse {
        public static final String IMG_PATH = "/i/";
        public ImageEntry entry;
        public String imageUrl;

        public ImageResponse( final ImageEntry entry )
        {
            super("image");
            this.entry = entry;
            this.imageUrl = IMG_PATH + entry.id;
        }
    }

    public static class DeleteResponse extends WSResponse {
        public long id;

        public DeleteResponse( final long id )
        {
            super("delete");
            this.id = id;
        }
    }
}
