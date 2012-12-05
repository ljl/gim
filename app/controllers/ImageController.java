package controllers;

import java.io.File;

import play.Logger;
import play.mvc.Controller;

public class ImageController extends Controller
{
    public static void upload(File pic) {
        Logger.info( "Uploading..." );
        Logger.info( pic.getName() );
        Logger.info( "Size->" + pic.getTotalSpace() );

    }
}
