package com.example.android.travelapp.activity.editor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelapp.CheckoutActivity;
import com.example.android.travelapp.Preferences;
import com.example.android.travelapp.R;
import com.example.android.travelapp.api.ApiInterface;

public class EditorActivity extends AppCompatActivity implements EditorView {

    Integer valueJumlah = 1;
    Integer valuehargatiket = 0;
    Integer valuetotalharga = 0;
    String sharga, sjumlah, stotal;
    Button btnPlus, btnMin;
    TextView tvJumlah, tvDestinasi, tvTempat, tvHarga, totalharga;
    EditText etTanggal;
    ProgressDialog progressDialog;

    private Bitmap bitmap;

    ApiInterface apiInterface;
    EditorPresenter presenter;

    int id, id_destinasi, harga, jumlah, total;
    String destinasi, tempat, tanggal, bukti;

    Menu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        btnPlus = findViewById(R.id.btnPlus);
        btnMin = findViewById(R.id.btnMin);
        tvJumlah = findViewById(R.id.teks_jml);
        tvDestinasi = findViewById(R.id.tv_destinasi);
        tvTempat = findViewById(R.id.tv_tempat);
        tvHarga = findViewById(R.id.harga_tiket);
        etTanggal = findViewById(R.id.resTanggal);
        totalharga = findViewById(R.id.total_harga);


        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        id_destinasi = intent.getIntExtra("id_destinasi", 0);
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
        String username = Preferences.getLoggedInUser(getBaseContext());

        switch (item.getItemId()){
            case R.id.save:
                presenter.saveTiket(id_destinasi, mjumlah, mtotal, mtanggal, username);
                return true;

            case R.id.edit:
                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);

                return true;

            case R.id.update:
                presenter.updateTiket(id, id_destinasi, mjumlah, mtotal, mtanggal);
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
    }

    private void readMode() {
        btnMin.setEnabled(false);
        btnPlus.setEnabled(false);
        etTanggal.setEnabled(false);
    }

}
