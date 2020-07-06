package com.example.android.travelapp.activity.tiket;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelapp.AlarmReceiver;
import com.example.android.travelapp.FetchAddressTask;
import com.example.android.travelapp.R;
import com.example.android.travelapp.activity.main.MainActivity;
import com.example.android.travelapp.api.ApiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

public class TiketActivity extends AppCompatActivity implements TiketView {

    Integer valueJumlah = 1;
    Integer valuehargatiket = 0;
    Integer valuetotalharga = 0;
    Button btnPlus, btnMin, btnKeranjang;
    TextView jumlah, tvDestinasi, tvTempat, tvHarga, totalharga;
    EditText etTanggal;
    ProgressDialog progressDialog;

    ApiInterface apiInterface;
    TiketPresenter presenter;

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    private static final int NOTIFICATION_ID = 0;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private static final String TRACKING_LOCATION_KEY = "tracking_location";

    // Location classes
    private boolean mTrackingLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket);

        btnPlus = findViewById(R.id.btnPlus);
        btnMin = findViewById(R.id.btnMin);
        jumlah = findViewById(R.id.teks_jml);
        btnKeranjang = findViewById(R.id.btn_cart);
        tvDestinasi = findViewById(R.id.tv_destinasi);
        tvTempat = findViewById(R.id.tv_tempat);
        tvHarga = findViewById(R.id.harga_tiket);
        etTanggal = findViewById(R.id.resTanggal);
        totalharga = findViewById(R.id.total_harga);

        etTanggal.setFocusable(false);

        // menampilkan data destinasi, tempat, dan harga
        tvDestinasi.setText(getIntent().getStringExtra("exDestinasi"));
        tvTempat.setText(getIntent().getStringExtra("exTempat"));

        Intent intent = getIntent();
        int hargatiket = intent.getIntExtra("exHarga", 0);
        valuehargatiket = hargatiket;
        tvHarga.setText(valuehargatiket.toString());

        //menampilkan total harga tiket
        valuetotalharga = valuehargatiket * valueJumlah;
        totalharga.setText(valuetotalharga.toString());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        presenter = new TiketPresenter(this);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlah += 1;
                jumlah.setText(valueJumlah.toString());

                valuetotalharga = valuehargatiket * valueJumlah;
                totalharga.setText(valuetotalharga.toString());
            }
        });

        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valueJumlah > 1){
                    valueJumlah -= 1;
                    jumlah.setText(valueJumlah.toString());
                }

                valuetotalharga = valuehargatiket * valueJumlah;
                totalharga.setText(valuetotalharga.toString());

            }
        });

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        btnKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menyimpan data inputan kedatabase
                String destinasi = tvDestinasi.getText().toString().trim();
                String tempat = tvTempat.getText().toString().trim();
                String harga = tvHarga.getText().toString();
                int mharga = Integer.parseInt(harga);
                String nilaiJumlah = jumlah.getText().toString();
                int mjumlah = Integer.parseInt(nilaiJumlah);
                String total = totalharga.getText().toString();
                int mtotal = Integer.parseInt(total);
                String mtanggal = etTanggal.getText().toString().trim();

                if(mtanggal.equals("")){
                    etTanggal.setError("Tanggal harus diisi!");
                    etTanggal.requestFocus();
                } else {
                    presenter.saveTiket(destinasi, tempat, mharga, mjumlah, mtotal, mtanggal);

                    Intent intent = new Intent(TiketActivity.this, MainActivity.class);
                    startActivity(intent);

                    //panggil method sendNotification
                    sendNotification();

                    //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
//                    if (alarmManager != null) {
//                        alarmManager.set
//                                (AlarmManager.ELAPSED_REALTIME,
//                                        SystemClock.elapsedRealtime() + 1000 * 300,
//                                        notifyPendingIntent);
//                    }
                }


            }
        });

        createNotificationChannel();
        //alarm
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


    }



    public void pilihTanggal(View view) {
        DialogFragment newFragment = new FetchAddressTask.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }


    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (day_string +
                "-" + month_string + "-" + year_string);

        TextView tanggal = findViewById(R.id.resTanggal);

        tanggal.setText(dateMessage);
    }

   //fungsi untuk mengirim notifikasi
    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

//    //fungsi untuk membuat notifikasi
    public void createNotificationChannel()
    {
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //fungsi untuk mengambil data notifikasi
    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Travel App")
                .setContentText("Tiket berhasil dimasukan ke keranjang. " +
                        "Silahkan untuk mengupload bukti pembayaran agar pesanan dapat diproses")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_android);
        return notifyBuilder;
    }



    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onAddSuccess(String message) {
        Toast.makeText(TiketActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onAddError(String message) {
        Toast.makeText(TiketActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
    }
}
