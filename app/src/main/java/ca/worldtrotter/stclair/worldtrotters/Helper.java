package ca.worldtrotter.stclair.worldtrotters;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.GeoDataClient;

import java.io.File;
import java.io.IOException;

import ca.worldtrotter.stclair.worldtrotters.MainActivity;

/**
 * Created by Dufour on 2018-04-05.
 */

public class Helper {


    /**
     * This method takes in a place id and returns a place object from Google
     * @param id
     * @return
     */
//    public static Place findPlaceById(String id){
//
//        Place place;
//
//        if(TextUtils.isEmpty(id) || client == null || !client.isConnected()) {
//            return null;
//        }
//
//        Places.GeoDataApi.getPlaceById(client, id).setResultCallback(new ResultCallback<PlaceBuffer>() {
//            @Override
//            public void onResult(@NonNull PlaceBuffer places) {
//                if(places.getStatus().isSuccess()) {
//                    Place place = places.get(0);
//
//                }
//                // release the PlaceBuffer to prevent a memory leak
//                places.release();
//            }
//        });
//        return null;
//    }

    /**
     * This method takes a placeId and tripId and adds the image to the database for the trip
     * @param placeId
     * @param tripId
     */
    public static void getPlacePhoto(String placeId, int tripId){
        final GoogleApiClient client = MainActivity.googleClient;
        Places.GeoDataApi.getPlacePhotos(client, placeId).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
            @Override
            public void onResult(@NonNull PlacePhotoMetadataResult placePhotoMetadataResult) {
                if(placePhotoMetadataResult.getStatus().isSuccess()){
                    PlacePhotoMetadataBuffer photoBuffer = placePhotoMetadataResult.getPhotoMetadata();

                    PlacePhotoMetadata photoMetadata = photoBuffer.get(0);
                    photoMetadata.getPhoto(client).setResultCallback(new ResultCallback<PlacePhotoResult>() {
                        @Override
                        public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                            Bitmap photo = placePhotoResult.getBitmap();


                        }
                    });
                }
            }
        });

    }

    /**
     * This method will create a temporary file wih a unique name to store the photo on the user's device
     * @return
     * @throws IOException
     */
    File createTempImageFile() throws IOException {
        //make the file name
        String fileName = "worldtrotters_app_v1_" + System.currentTimeMillis() + ".jpg";
        //grab the directory to save the image in
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File photo = File.createTempFile(fileName, ".jpg", dir);
        return photo;
    }


}















