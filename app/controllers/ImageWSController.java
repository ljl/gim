package controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import controllers.image.ImageHelper;
import models.ImageEntry;
import play.Logger;
import play.libs.MimeTypes;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;
import play.mvc.Http.WebSocketFrame;
import play.mvc.WebSocketController;

public class ImageWSController
    extends WebSocketController
{

    public static void image()
        throws IOException
    {
        while ( inbound.isOpen() )
        {
            WebSocketEvent e = await( inbound.nextEvent() );
            if ( e instanceof WebSocketFrame )
            {
                WebSocketFrame frame = (WebSocketFrame) e;
                Logger.info( String.valueOf( ( (WebSocketFrame) e ).isBinary ) );
                if ( frame.isBinary )
                {
                    Logger.info( "Recived binary input" );
                    InputStream is = new ByteArrayInputStream( frame.binaryData );


                    ImageEntry entry = new ImageEntry();
                    entry.name = "websocketimage";
                    entry.image.set( is, MimeTypes.getMimeType( "lol.jpg" ) );

                    entry.save();

                    int extract = ImageHelper.extractMetadata(entry);

                    outbound.sendJson( new ImageResult(entry) );
                }
            }
            if ( e instanceof WebSocketClose )
            {
                Logger.info( "Socket closed!" );
            }
        }
    }

    public static void renameImage() {
        while ( inbound.isOpen() ) {

        }
    }
}
