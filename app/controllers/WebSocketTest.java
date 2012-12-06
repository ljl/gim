package controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import models.ImageEntry;
import play.Logger;
import play.libs.MimeTypes;
import play.mvc.Http.WebSocketFrame;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;
import play.mvc.WebSocketController;

public class WebSocketTest
    extends WebSocketController
{
    public static void hello( String name )
    {
        outbound.send( "Hello %s", name );
    }

    public static void image()
    {
        while ( inbound.isOpen() )
        {
            WebSocketEvent e = await( inbound.nextEvent() );
            if ( e instanceof WebSocketFrame )
            {
                WebSocketFrame frame = (WebSocketFrame) e;
                if ( frame.isBinary )
                {
                    InputStream is = new ByteArrayInputStream( frame.binaryData );

                    ImageEntry entry = new ImageEntry();
                    entry.name = "websocketimage";
                    entry.image.set( is, MimeTypes.getMimeType( "lol.jpg" ) );

                    outbound.send( "Uploaded!" );
                }
            }
            if ( e instanceof WebSocketClose )
            {
                Logger.info( "Socket closed!" );
            }
        }
    }
}
