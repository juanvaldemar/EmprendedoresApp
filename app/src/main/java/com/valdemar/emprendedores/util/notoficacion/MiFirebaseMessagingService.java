package com.valdemar.emprendedores.util.notoficacion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.SplashActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MiFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "Emprendedores App";
    public static final String NOTIFICATION_CHANNEL_ID = "Proyectos";
    private DatabaseReference mDatabase, mDatabaseNotificacion;
    private String titulo = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d(TAG, "Mensaje recibido de: " + from);

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notificación; " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "title; " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "url; " + remoteMessage.getNotification().getImageUrl());

            if (remoteMessage.getNotification().getBody() != null) {
                mostrarNotificacion("SPOOK",
                        remoteMessage.getNotification().getBody()
                        , getBitmapFromURL(remoteMessage.getNotification().getImageUrl().toString())
                        , replaceCharsLower(remoteMessage.getNotification().getTitle()));

            } else {
                String limpieza_uno = remoteMessage.getNotification().getTitle().replace("\"", "");
                String limpieza_dos = limpieza_uno.replace("{", "");
                String limpieza_tres = limpieza_dos.replace("}", "");

                String limpieza_cuatro [] = limpieza_tres.split(",");

                mostrarNotificacionSuscritos(limpieza_cuatro[2]);


            }


        }
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }

    }

    private String replaceCharsLower(String txt) {
        // if(txt == null) return "XYZ";
        //  txt = txt.toLowerCase().replace("á","a").replace("é","e")
        //       .replace("í","i").replace("ó","o").replace("ú","u");
        return txt;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private void mostrarNotificacion(String title,
                                     final String body,
                                     final Bitmap url,
                                     final String categoria) {
        final Intent intent = new Intent(this, SplashActivity.class);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
        mDatabaseNotificacion = FirebaseDatabase.getInstance().getReference().child("Proyectos");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot : dataSnapshot.getChildren()) {
                    String idEmprendedor = (String) itemSpanshot.child("id_emprendedor").getValue();
                    String intereses_emprendedor = (String) itemSpanshot.child("intereses").getValue();
                    String estadoTrazabilidad = (String) itemSpanshot.child("estadoTrazabilidad").getValue();
                    if (user.getUid().equalsIgnoreCase(idEmprendedor)) {
                        if (intereses_emprendedor != null) {
                            String[] segmentacionCanalSplit = intereses_emprendedor.split(",");

                            for (String i : segmentacionCanalSplit) {
                                String i_ = i.replace("[", "");
                                String i__ = i_.replace("]", "");
                                if (i__.trim().equalsIgnoreCase(categoria)) {
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

                                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

                                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Emprendedores App")
                                            .setContentText(body)
                                            .setAutoCancel(true)
                                            .setSound(soundUri)
                                            .setStyle(new NotificationCompat.BigPictureStyle()
                                                    .bigPicture(url).bigLargeIcon(null)
                                            )
                                            .setContentIntent(pendingIntent);


                                    int mNotificationId = (int) System.currentTimeMillis();
                                    // NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    NotificationManager mNotifyMgr =
                                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "News", importance);

                                        notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                                        mNotifyMgr.createNotificationChannel(notificationChannel);
                                    }

                                    mNotifyMgr.notify(mNotificationId, notificationBuilder.build());
                                    break;
                                }
                            }


                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void mostrarNotificacionSuscritos(final String id_transaccion) {



        final Intent intent = new Intent(this, SplashActivity.class);
        System.out.println(id_transaccion);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseNotificacion = FirebaseDatabase.getInstance().getReference().child("Proyectos");

        mDatabaseNotificacion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot : dataSnapshot.getChildren()) {
                    String idEmprendedor = (String) itemSpanshot.child("id_emprendedor").getValue();
                    String nombreEmprendedor = (String) itemSpanshot.child("nombre").getValue();
                    String imagen = (String) itemSpanshot.child("imagen").getValue();


                    if(itemSpanshot.getKey().equalsIgnoreCase(id_transaccion)){
                        if(idEmprendedor.equalsIgnoreCase(user.getUid())){
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

                            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Emprendedores App")
                                    .setContentText("Tienes un suscrito en "+nombreEmprendedor)
                                    .setAutoCancel(true)
                                    .setSound(soundUri)
                                    .setContentIntent(pendingIntent);


                            int mNotificationId = (int) System.currentTimeMillis();
                            // NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "News", importance);

                                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                                mNotifyMgr.createNotificationChannel(notificationChannel);
                            }

                            mNotifyMgr.notify(mNotificationId, notificationBuilder.build());
                            break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
