package com.courage.facedetectapi;

import androidx.appcompat.app.AppCompatActivity;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.appliedrec.verid.core2.Face;
import com.appliedrec.verid.core2.VerID;
import com.appliedrec.verid.core2.VerIDFactory;
import com.appliedrec.verid.core2.VerIDFactoryDelegate;
import com.appliedrec.verid.core2.session.LivenessDetectionSessionSettings;
import com.appliedrec.verid.core2.session.VerIDSessionResult;
import com.appliedrec.verid.identity.VerIDIdentity;
import com.appliedrec.verid.identity.VerIDSDKIdentity;
import com.appliedrec.verid.ui2.IVerIDSession;
import com.appliedrec.verid.ui2.VerIDSession;
import com.appliedrec.verid.ui2.VerIDSessionDelegate;


//import com.courage.facedetect.FaceCheck;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
//import com.courage.facedetect.FaceCheck;

import java.util.concurrent.CompletableFuture;

import static android.graphics.Bitmap.createBitmap;
import static androidx.test.InstrumentationRegistry.getContext;
import static java.util.concurrent.CompletableFuture.completedFuture;
//import com.appliedrec.verid.ui2.

public class MainActivity extends AppCompatActivity implements VerIDFactoryDelegate, VerIDSessionDelegate {

    ImageView imageView;
    Button btnTakePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);



       // FaceCheck.getImage(null,this);

       // startLivenessDetectionSession();
    }




    @Override
    public void onVerIDCreated(VerIDFactory verIDFactory, VerID verID) {
        // You can now start a Ver-ID session

        Log.i("TAG", "onVerIDCreated: ");
        LivenessDetectionSessionSettings settings = new LivenessDetectionSessionSettings();
        VerIDSession session = new VerIDSession(verID, settings);
        session.setDelegate(this);
        session.start();
    }

    @Override
    public void onVerIDCreationFailed(VerIDFactory verIDFactory, Exception e) {
        // Failed to create an instance of Ver-ID
        Log.i("TAG", "onVerIDCreationFailed: " + e.getCause());
        e.printStackTrace();
    }

    @Override
    public void onSessionFinished(IVerIDSession<?> session, VerIDSessionResult result) {
        if (!result.getError().isPresent()) {
            // Session succeeded
            Log.i("TAG", "onSessionFinished: ");
        } else {
            Log.i("TAG", "onSessionFinished: unsuccessful");
            // Session failed
        }
    }



    @Override
    public void onSessionCanceled(IVerIDSession<?> session) {
        session.getSessionIdentifier();
        Log.i("TAG", "onSessionCanceled: ");
    }


}