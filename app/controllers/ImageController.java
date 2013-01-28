package controllers;

import java.io.IOException;
import java.util.Map;

import models.ImageBoard;
import models.ImageEntry;
import play.Logger;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.WebSocketController;

import static play.libs.F.Matcher.ClassOf;
import static play.libs.F.Matcher.String;
import static play.mvc.Http.WebSocketEvent.BinaryFrame;
import static play.mvc.Http.WebSocketEvent.TextFrame;

public class ImageController extends Controller
{

    public static void displayImage(long id) {
        ImageEntry entry = ImageEntry.findById( id );
        renderBinary( entry.image.get() );
    }

    public static void deleteAll() {
        ImageEntry.deleteAll();
        redirect( request.headers.get( "referer" ).value() );
    }

    public static class WebSocket extends WebSocketController {
        public static void image()
                throws IOException
            {
                ImageBoard board = ImageBoard.get();

                F.EventStream<ImageBoard.Event> imageStream = board.connect();

                while ( inbound.isOpen() )
                {
                    F.Either<Http.WebSocketEvent,ImageBoard.Event> e = await(
                        F.Promise.waitEither( inbound.nextEvent(),
                                              imageStream.nextEvent() ));

                    // When client uploades
                    for (byte[] bytes: BinaryFrame.match( e._1 )) {
                        Logger.info( "Recieved binary event" );
                        board.upload(bytes);
                    }

                    for(String delete: TextFrame.match( e._1 )) {

                    }

                    // When someone uploads something
                    for (ImageBoard.Upload upload: ClassOf( ImageBoard.Upload.class ).match( e._2 )) {
                        outbound.sendJson( upload.response );
                    }

                    for (ImageBoard.Delete delete: ClassOf( ImageBoard.Delete.class ).match( e._2 ) ) {
                        outbound.sendJson( delete.response );
                    }

                }
            }
    }

}
