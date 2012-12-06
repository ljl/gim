package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import models.ImageEntry;
import play.Logger;
import play.libs.MimeTypes;
import play.mvc.Controller;

public class ImageController extends Controller
{
    public static void upload(File pic)
        throws FileNotFoundException
    {
        Logger.info( "Uploading..." );
        Logger.info( pic.getName() );
        Logger.info( "Size->" + pic.getTotalSpace() );

        ImageEntry entry = new ImageEntry();
        entry.name = pic.getName();
        entry.setImage(pic);
        entry.save();

        Logger.info( "Current images" );
        List<ImageEntry> entries = ImageEntry.all().fetch();
        for (ImageEntry e : entries) {
            Logger.info( e.name );
        }
    }

    public static void download(String imageUrl)
        throws IOException
    {
        URL url = new URL( imageUrl );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        ImageEntry entry = new ImageEntry();
        entry.name = "wat";
        entry.image.set( connection.getInputStream(), MimeTypes.getMimeType( "lol.jpg" ) );

        entry.save();

    }

    public static void displayImage(long id) {
        ImageEntry entry = ImageEntry.findById( id );
        renderBinary( entry.image.get() );
    }

}
