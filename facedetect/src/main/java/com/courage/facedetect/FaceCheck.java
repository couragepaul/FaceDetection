package com.courage.facedetect;

import android.content.Context;
import android.graphics.Bitmap;
//import android.media.FaceDetector;

import com.appliedrec.verid.core2.DetRecLib;
import com.appliedrec.verid.core2.Face;
import com.appliedrec.verid.core2.FaceDetectionRecognitionSettings;
import com.appliedrec.verid.core2.FaceRecognition;
import com.appliedrec.verid.core2.IFaceDetection;
import com.appliedrec.verid.core2.IFaceDetectionFactory;
import com.appliedrec.verid.core2.IFaceRecognition;
import com.appliedrec.verid.core2.IFaceRecognitionFactory;
import com.appliedrec.verid.core2.IRecognizable;
import com.appliedrec.verid.core2.RecognizableFace;
import com.appliedrec.verid.core2.RecognizableSubject;
import com.appliedrec.verid.core2.VerIDFactory;
import com.appliedrec.verid.core2.VerIDFactoryDelegate;
import com.appliedrec.verid.identity.VerIDIdentity;

import java.io.File;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public class FaceCheck {
    public static Observable<Float> getImage(Bitmap imageSrc, Context mContext, VerIDFactoryDelegate delegate) {
        float score = 0;

        try {
            VerIDIdentity identity = new VerIDIdentity(mContext, "c8f9a54a-48cc-4adf-a431-2a52155f739f");
            VerIDFactory verIDFactory = new VerIDFactory(mContext, identity);

            IFaceDetectionFactory faceDetectionFactory = verIDFactory.getFaceDetectionFactory();
            IFaceDetection faceDetection = faceDetectionFactory.createFaceDetection();
            Face[] faces = faceDetection.detectFacesInImage(imageSrc, 1, 0);
            RecognizableFace recogFace = new RecognizableFace(faces[0], faces[0].getData());
            RecognizableFace[] recogFaces = new RecognizableFace[1];
            recogFaces[0] = recogFace;
            // launch a Ver-ID liveness detection session
            boolean startedSession = startLivenessDetectionSession(verIDFactory, mContext, delegate);

            // If the liveness detection session succeeds,
            if (startedSession) {
                // take the first face
                IRecognizable firstFace = verIDFactory.getUserManagementFactory().createUserManagement().getFaces()[0];

                IRecognizable[] newFaces = new RecognizableFace[1];
                newFaces[0] = firstFace;
                IFaceRecognitionFactory faceRecognitionFactory = verIDFactory.getFaceRecognitionFactory();
                IFaceRecognition faceRecognition = faceRecognitionFactory.createFaceRecognition();

                // compare to the face from the supplied bitmap.
                score = faceRecognition.compareSubjectFacesToFaces(recogFaces, newFaces);

                // communicate the similarity score between the compared faces.
                System.out.println(score);
            } else {
                System.out.println("Live session did not start");
            }

        } catch (Exception e) {
            // Failed to create identity with your credentials.
        }

        Observable<Float> newObservable = Observable.just(score);

        return newObservable;
    }

    public static boolean startLivenessDetectionSession(VerIDFactory veridFactory, Context mContext, VerIDFactoryDelegate delegate) {
        boolean success;

        try {
            veridFactory.setDelegate(delegate);
            veridFactory.createVerID();
            success = true;
        } catch (Exception e) {
            // Failed to create identity with your credentials.
            success = false;
        }

        return success;
    }
}
