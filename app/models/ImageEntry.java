package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Blob;
import play.db.jpa.Model;
import play.libs.MimeTypes;

@Entity
public class ImageEntry extends Model
{
    public String name;
    public Date created;
    public Blob image;
    public Blob thumb;

    public ImageEntry() {
        this.created = new Date( );
        this.image = new Blob();
    }

    public void setImage( final File pic )
        throws FileNotFoundException
    {
        InputStream data = new FileInputStream( pic );
        this.image.set( data, MimeTypes.getContentType( pic.getName() ) );

    }

}
