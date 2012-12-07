package controllers;

import models.ImageEntry;
import play.mvc.Controller;

public class ImageController extends Controller
{

    public static void displayImage(long id) {
        ImageEntry entry = ImageEntry.findById( id );
        renderBinary( entry.image.get() );
    }

    public static void deleteAll() {
        ImageEntry.deleteAll();
    }

}
