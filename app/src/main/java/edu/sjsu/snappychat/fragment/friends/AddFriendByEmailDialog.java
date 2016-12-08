package edu.sjsu.snappychat.fragment.friends;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Kamlendra on 12/7/2016.
 */

public class AddFriendByEmailDialog implements View.OnClickListener {

    private Activity activity;

    public AddFriendByEmailDialog(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this.activity);
        alert.setIcon(R.drawable.ic_person_add_black_18dp);
        alert.setTitle("Add Person By Email");
        final EditText email = new EditText(this.activity);
        email.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email.setHint("Enter Email");
        //email.setPadding(5, 5,5, 5);
        alert.setView(email);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onPositiveButtonClicked(email.getText().toString());

            }
        });
        alert.setNegativeButton("Cancel", null);

        final AlertDialog dialog = alert.create();
        dialog.show();

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (!isValidEmail(s.toString()) || TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

    }

    private void onPositiveButtonClicked(String receiver) {
        String sender = UserService.getInstance().getEmail();
        DatabaseService.sendFriendRequest(sender, receiver);

        Toast.makeText(getApplicationContext(), "Request Sent ", Toast.LENGTH_LONG).show();

    }

    private boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
