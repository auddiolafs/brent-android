package is.hi.hbv601g.brent;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class RequireInternet extends DialogFragment {
    private CurrentActivity currentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.currentActivity = (CurrentActivity) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("The application needs Internet connection to continue, please activate Internet connection")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(currentActivity.isConnected()) {
                            currentActivity.setUp();
                        }
                    }
                });
        return builder.create();
    }
}
