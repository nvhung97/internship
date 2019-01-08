package com.vincent.videocompressor;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Mp4TrackImpl;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;

public class SamsungCameraTool {

    public static File FixSamsungBug(File mFile) {

        DataSource channel = null;
        IsoFile isoFile = null;
        try {
            channel = new FileDataSourceImpl(mFile);
            isoFile = new IsoFile(channel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<TrackBox> trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);
        boolean sampleError = false;
        for (TrackBox trackBox : trackBoxes) {
            TimeToSampleBox.Entry firstEntry = trackBox
                    .getMediaBox()
                    .getMediaInformationBox()
                    .getSampleTableBox()
                    .getTimeToSampleBox()
                    .getEntries()
                    .get(0);

            // Detect if first sample is a problem and fix it in isoFile
            // This is a hack. The audio deltas are 1024 for my files, and video deltas about 3000
            // 10000 seems sufficient since for 30 fps the normal delta is about 3000
            if (firstEntry.getDelta() > 10000) {
                sampleError = true;
                firstEntry.setDelta(3000);
            }
        }

        if (sampleError) {
            Movie movie = new Movie();
            for (TrackBox trackBox : trackBoxes) {
                movie.addTrack(new Mp4TrackImpl(channel.toString() + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
            }
            movie.setMatrix(isoFile.getMovieBox().getMovieHeaderBox().getMatrix());
            Container out = new DefaultMp4Builder().build(movie);

            try {
                File newFile = new File(mFile.getParent(), "VID_" + System.currentTimeMillis() + ".mp4");
                newFile.createNewFile();
                FileChannel fc = new RandomAccessFile(newFile.getAbsolutePath(), "rw").getChannel();
                out.writeContainer(fc);
                fc.close();
                mFile.delete();
                return newFile;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mFile;
    }
}
