package controllers.image;

import java.io.IOException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import models.ImageEntry;
import play.Logger;

public class ImageHelper
{
    public static int extractMetadata( ImageEntry entry )
    {
        Metadata metadata = null;
        try
        {
            metadata = ImageMetadataReader.readMetadata( entry.image.getFile() );

            for ( Directory directory : metadata.getDirectories() )
            {
                for ( Tag tag : directory.getTags() )
                {
                    Logger.info( tag.toString() );
                }
            }
            return 0;
        }
        catch ( ImageProcessingException e )
        {
            return 1;
        }
        catch ( IOException e )
        {
            return 2;
        }


    }

}
