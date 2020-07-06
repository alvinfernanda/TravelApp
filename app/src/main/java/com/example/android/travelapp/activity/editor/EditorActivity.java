package com.example.android.travelapp.activity.editor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelapp.R;
import com.example.android.travelapp.activity.keranjang.KeranjangActivity;
import com.example.android.travelapp.api.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditorActivity extends AppCompatActivity implements EditorView {

    Integer valueJumlah = 1;
    Integer valuehargatiket = 0;
    Integer valuetotalharga = 0;
    String sharga, sjumlah, stotal;
    Button btnPlus, btnMin, btnCheckout;
    TextView tvJumlah, tvDestinasi, tvTempat, tvHarga, totalharga;
    EditText etTanggal;
    ImageView uploadBukti;
    ProgressDialog progressDialog;

    private Bitmap bitmap;

    ApiInterface apiInterface;
    EditorPresenter presenter;

    int id, harga, jumlah, total;
    String destinasi, tempat, tanggal, bukti;

    Menu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        btnPlus = findViewById(R.id.btnPlus);
        btnMin = findViewById(R.id.btnMin);
        tvJumlah = findViewById(R.id.teks_jml);
        btnCheckout = findViewById(R.id.btn_checkout);
        tvDestinasi = findViewById(R.id.tv_destinasi);
        tvTempat = findViewById(R.id.tv_tempat);
        tvHarga = findViewById(R.id.harga_tiket);
        etTanggal = findViewById(R.id.resTanggal);
        uploadBukti = findViewById(R.id.upload);
        totalharga = findViewById(R.id.total_harga);


        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        destinasi = intent.getStringExtra("destinasi");
        tempat = intent.getStringExtra("tempat");
        harga = intent.getIntExtra("harga", 1);
        sharga = Integer.toString(harga);
        jumlah = intent.getIntExtra("jumlah", 0);
        sjumlah = Integer.toString(jumlah);
        total = intent.getIntExtra("total", 0);
        stotal = Integer.toString(total);
        tanggal = intent.getStringExtra("tanggal");
        bukti = intent.getStringExtra("bukti");

        setDataFromIntentExtra();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        presenter = new EditorPresenter(this);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumlah += 1;
                sjumlah = Integer.toString(jumlah);
                tvJumlah.setText(sjumlah);

                valuetotalharga = harga * jumlah;
                totalharga.setText(valuetotalharga.toString());
            }
        });

        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jumlah > 1){
                    jumlah -= 1;
                    sjumlah = Integer.toString(jumlah);
                    tvJumlah.setText(sjumlah);
                }

                valuetotalharga = harga * jumlah;
                totalharga.setText(valuetotalharga.toString());

            }
        });

        uploadBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihFile();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //panggil method sendNotification
                //sendNotification();
                String bukti = null;
                if (bitmap == null) {
                    bukti = "";
                } else {
                    bukti = getStringImage(bitmap);
                }

                if (uploadBukti.getDrawable() == null){
                    btnCheckout.setEnabled(false);
                } else {
                    presenter.uploadBukti(id, bukti);

                    Intent intent = new Intent(EditorActivity.this, KeranjangActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;

        if (id != 0){
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        } else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String mdestinasi = tvDestinasi.getText().toString().trim();
        String mtempat = tvTempat.getText().toString().trim();
        String harga = tvHarga.getText().toString();
        int mharga = Integer.parseInt(harga);
        String nilaiJumlah = tvJumlah.getText().toString();
        int mjumlah = Integer.parseInt(nilaiJumlah);
        String total = totalharga.getText().toString();
        int mtotal = Integer.parseInt(total);
        String mtanggal = etTanggal.getText().toString().trim();

        switch (item.getItemId()){
            case R.id.save:
                presenter.saveTiket(mdestinasi, mtempat, mharga, mjumlah, mtotal, mtanggal);
                return true;

            case R.id.edit:
                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);

                return true;

            case R.id.update:
                presenter.updateTiket(id, mdestinasi, mtempat, mharga, mjumlah, mtotal, mtanggal);
                return true;

            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Konfirmasi!");
                alertDialog.setMessage("Apakah kamu yakin ?");
                alertDialog.setNegativeButton("Ya", (dialog, which) -> {
                    dialog.dismiss();
                    presenter.deleteTiket(id);
                });
                alertDialog.setPositiveButton("Tidak",
                        (dialog, which) -> dialog.dismiss());

                alertDialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void pilihTanggal(View view) {
        DialogFragment newFragment = new DatePicker.DatePickerFragment();
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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void pilihFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                uploadBukti.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
    public void onRequestSuccess(String message) {
        Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setDataFromIntentExtra() {
        if (id != 0){
            tvDestinasi.setText(destinasi);
            tvTempat.setText(tempat);
            tvHarga.setText(sharga);
            tvJumlah.setText(sjumlah);
            totalharga.setText(stotal);
            etTanggal.setText(tanggal);

            getSupportActionBar().setTitle("Update Tiket");
            readMode();
        } else {
            tvDestinasi.setText(destinasi);
            tvTempat.setText(tempat);
            tvHarga.setText(sharga);
            tvJumlah.setText(sjumlah);
            totalharga.setText(stotal);
            etTanggal.setText(tanggal);
            editMode();
        }

    }

    private void editMode() {
        btnMin.setEnabled(true);
        btnPlus.setEnabled(true);
        etTanggal.setEnabled(true);
        etTanggal.setFocusable(false);
        btnCheckout.setVisibility(View.GONE);
        uploadBukti.setVisibility(View.GONE);
    }

    private void readMode() {
        btnMin.setEnabled(false);
        btnPlus.setEnabled(false);
        etTanggal.setEnabled(false);
    }

}
