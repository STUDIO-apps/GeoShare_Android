package uk.co.appsbystudio.geoshare.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import uk.co.appsbystudio.geoshare.R;

public class ProfilePictureOptions extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder optionsMenu = new AlertDialog.Builder(getActivity());
        optionsMenu.setTitle("").setItems(R.array.profilePictureOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent selectPicture = new Intent();
                    selectPicture.setType("image/*");
                    selectPicture.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(selectPicture, "Select Picture"), 1);
                } if (which == 1) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePicture, 1);
                    }
                }
            }
        });

        return optionsMenu.create();
    }
}