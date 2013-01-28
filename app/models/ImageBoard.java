package models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import play.libs.F;
import play.libs.MimeTypes;
/*
* This class takes care of events
* */
public class ImageBoard
{

    final F.ArchivedEventStream<ImageBoard.Event> imageEvents = new F.ArchivedEventStream<ImageBoard.Event>(100);

    public F.EventStream<ImageBoard.Event> connect() {
        return imageEvents.eventStream();
    }

    public void upload( final byte[] bytes )
    {
        imageEvents.publish( new Upload( bytes ) );
    }

    public void delete() {

    }

    public static abstract class Event {

            final public String type;
            final public Long timestamp;

            public Event(String type) {
                this.type = type;
                this.timestamp = System.currentTimeMillis();
            }

        }

    public static class Upload extends Event {

        final public WSResponse response;

        public Upload(byte [] bytes) {
            super("upload");
            InputStream is = new ByteArrayInputStream( bytes );
            ImageEntry entry = new ImageEntry();
            entry.name = "websocketimage";
            entry.image.set( is, MimeTypes.getMimeType( "lol.jpg" ) );

            entry.save();
            this.response = new WSResponse.ImageResponse( entry );
        }
    }

    public static class Delete extends Event {

        final public WSResponse response;

        public Delete(Long id) {
            super("delete");
            ImageEntry.findById( id )._delete();
            this.response = new WSResponse.DeleteResponse( id );
        }
    }

    static ImageBoard instance = null;
    public static ImageBoard get() {
        if(instance == null) {
            instance = new ImageBoard();
        }
        return instance;
    }
}
