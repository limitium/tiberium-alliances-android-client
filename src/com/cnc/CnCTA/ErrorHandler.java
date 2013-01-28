package com.cnc.CnCTA;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import com.cnc.api.CncApiException;


public class ErrorHandler {
    private final HandleActivity errorGenerator;

    public ErrorHandler(HandleActivity errorGenerator) {
        this.errorGenerator = errorGenerator;
    }

    void showError(final CncApiException e) {
        errorGenerator.getHandler().post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(errorGenerator.getActivity());
                builder.setTitle("Error!");
                builder.setMessage(e.getMessage());
                builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    public interface HandleActivity {
        public Activity getActivity();

        public Handler getHandler();
    }
}