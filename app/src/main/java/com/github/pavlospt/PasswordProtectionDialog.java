package com.github.pavlospt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordProtectionDialog {

    private static String DIALOG_TITLE_TEXT = "Password Protection";
    private static String PASSWORD_TV_TEXT = "Password:";
    private static String CONFIRM_PASSWORD_TV_TEXT = "Confirm:";
    private static String PASSWORD_HINT_TEXT = "Password";
    private static String CONFIRM_PASSWORD_HINT_TEXT = "Confirm";
    private static String EMPTY_FIELD_ERROR_TEXT_TOAST = "You can not leave password empty!";
    private static String EMPTY_FIELD_ERROR_TEXT = "Can not be empty";
    private static String PASSWORDS_DO_NOT_MATCH_TEXT = "Passwords do not match! Please try again!";
    private static String WRONG_PASSWORD_TEXT = "Wrong password!";
    private static String WRONG_PASSWORD_ENTERED_TEXT = "You entered a wrong password!Please try again! ";

    private static int DEFAULT_MAX_TRIES = 3;

    private int mMaxTries = DEFAULT_MAX_TRIES;

    private String mDialogTitleText = DIALOG_TITLE_TEXT;
    private String mPasswordTVText = PASSWORD_TV_TEXT;
    private String mConfirmPasswordTVText = CONFIRM_PASSWORD_TV_TEXT;
    private String mPasswordETHint = PASSWORD_HINT_TEXT;
    private String mConfirmPasswordETHint = CONFIRM_PASSWORD_HINT_TEXT;
    private String mPasswordEmptyErrorText = EMPTY_FIELD_ERROR_TEXT;
    private String mConfirmPasswordEmptyErrorText = EMPTY_FIELD_ERROR_TEXT;
    private String mPasswordDoNotMatchErrorText = PASSWORDS_DO_NOT_MATCH_TEXT;
    private String mWrongPasswordText = WRONG_PASSWORD_TEXT;
    private String mEmptyFieldErrorTextToast = EMPTY_FIELD_ERROR_TEXT_TOAST;
    private String mWrongPasswordEnteredText = WRONG_PASSWORD_ENTERED_TEXT;

    private Context mContext;
    private Activity mInheritedActivity;

    private EditText mPasswordET,mConfirmPasswordET;
    private TextView mPasswordTV,mConfirmPasswordTV;
    private LinearLayout mConfirmPasswordLayout;

    private SharedPreferences mSharedPreferences;

    private boolean closeDialogFlag;
    private int mTriesCounter;

    public PasswordProtectionDialog(Activity activity) {
        mInheritedActivity = activity;
        mContext = activity.getApplicationContext();
        mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFS_KEY,Context.MODE_PRIVATE);
    }

    public void initializeDialog(){
        if(mSharedPreferences.getBoolean(Constants.PREF_PASS_PROTECTION_STATE,false))
            showPasswordDialog();
    }

    private void showPasswordDialog(){

        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.dialog_control_password,null,false);

        initializeViews(rootView);

        if(mSharedPreferences.getBoolean(Constants.PREF_USER_HAS_SET_PASSWORD,false)){
            mConfirmPasswordLayout.setVisibility(View.GONE);
        }

        final AlertDialog mDialog = new AlertDialog.Builder(mInheritedActivity)
                .setView(rootView)
                .setTitle(mDialogTitleText)
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        mDialog.show();

        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSharedPreferences.getBoolean(Constants.PREF_USER_HAS_SET_PASSWORD,false)){
                    if(TextUtils.isEmpty(mPasswordET.getText())){
                        showToastMessage(mEmptyFieldErrorTextToast);
                        mPasswordET.setError(mPasswordEmptyErrorText);
                        closeDialogFlag = false;
                    }else{
                        if(!mPasswordET.getText().toString()
                                .equals(mSharedPreferences.getString(Constants.PREF_USER_PASSWORD_KEY,""))){
                            mTriesCounter++;
                            if(mTriesCounter < mMaxTries){
                                showToastMessage(mWrongPasswordEnteredText + mTriesCounter + "/" + mMaxTries);
                                mPasswordET.setError(mWrongPasswordText);
                            }else{
                                mDialog.dismiss();
                                mInheritedActivity.finish();
                            }
                        }else{
                            closeDialogFlag = true;
                        }
                    }
                }else{
                    if (TextUtils.isEmpty(mPasswordET.getText().toString()) || TextUtils.isEmpty(mConfirmPasswordET.getText().toString())) {
                        showToastMessage(mEmptyFieldErrorTextToast);
                        if (TextUtils.isEmpty(mPasswordET.getText())) {
                            mPasswordET.setError(mPasswordEmptyErrorText);
                        }
                        if (TextUtils.isEmpty(mConfirmPasswordET.getText())) {
                            mConfirmPasswordET.setError(mConfirmPasswordEmptyErrorText);
                        }
                    } else {
                        if (!mPasswordET.getText().toString().equals(mConfirmPasswordET.getText().toString())) {
                            showToastMessage(mPasswordDoNotMatchErrorText);
                            closeDialogFlag = false;
                        } else {
                            mSharedPreferences.edit().putString(Constants.PREF_USER_PASSWORD_KEY,
                                    mPasswordET.getText().toString()).apply();
                            mSharedPreferences.edit().putBoolean(Constants.PREF_USER_HAS_SET_PASSWORD,
                                    true).apply();
                            closeDialogFlag = true;
                        }
                    }
                }

                if(closeDialogFlag)
                    mDialog.dismiss();
            }
        });

        mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mInheritedActivity.finish();
            }
        });

    }

    private void initializeViews(View view){
        mPasswordET = (EditText) view.findViewById(R.id.et_password);
        mConfirmPasswordET = (EditText) view.findViewById(R.id.et_confirm_password);
        mPasswordTV = (TextView) view.findViewById(R.id.tv_password);
        mConfirmPasswordTV = (TextView) view.findViewById(R.id.tv_confirm_password);
        mConfirmPasswordLayout = (LinearLayout) view.findViewById(R.id.confirm_password_layout);

        mPasswordTV.setText(mPasswordTVText);
        mConfirmPasswordTV.setText(mConfirmPasswordTVText);
        mPasswordET.setHint(mPasswordETHint);
        mConfirmPasswordET.setHint(mConfirmPasswordETHint);

    }

    private void showToastMessage(String message){
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
    }

    /**
     * SharedPreference that controls if the dialog should appear or not
     * */
    public void setShowPasswordDialog(boolean value){
        mSharedPreferences.edit().putBoolean(Constants.PREF_PASS_PROTECTION_STATE,value).apply();
    }

    /**
     * Sets the Dialog's title.
     * */
    public void setDialogTitleText(String text){
        this.mDialogTitleText = text;
    }

    /**
     * Get the text that is set on the Password TextView.
     * */
    public String getPasswordTVText() {
        return mPasswordTVText;
    }

    /**
     * Set the text for the Password TextView.
     * */
    public void setPasswordTVText(String mPasswordTVText) {
        this.mPasswordTVText = mPasswordTVText;
    }

    /**
     * Get the text that is set on the ConfirmPassword TextView.
     * */
    public String getConfirmPasswordTVText() {
        return mConfirmPasswordTVText;
    }

    /**
     * Set the text for the ConfirmPassword TextView.
     * */
    public void setConfirmPasswordTVText(String mConfirmPasswordTVText) {
        this.mConfirmPasswordTVText = mConfirmPasswordTVText;
    }

    /**
     * Get the hint that is set on the Password EditText.
     * */
    public String getPasswordETHint() {
        return mPasswordETHint;
    }

    /**
     * Set the hint for the Password EditText.
     * */
    public void setPasswordETHint(String mPasswordETHint) {
        this.mPasswordETHint = mPasswordETHint;
    }

    /**
     * Get the hint that is set on the ConfirmPassword EditText.
     * */
    public String getConfirmPasswordETHint() {
        return mConfirmPasswordETHint;
    }

    /**
     * Set the hint for the ConfirmPassword EditText.
     * */
    public void setConfirmPasswordETHint(String mConfirmPasswordETHint) {
        this.mConfirmPasswordETHint = mConfirmPasswordETHint;
    }

    /**
     * Get the text that is set on when Password field is empty.
     * */
    public String getPasswordEmptyErrorText() {
        return mPasswordEmptyErrorText;
    }

    /**
     * Set the text to appear, when Password field is empty.
     * */
    public void setPasswordEmptyErrorText(String mPasswordEmptyErrorText) {
        this.mPasswordEmptyErrorText = mPasswordEmptyErrorText;
    }

    /**
     * Get the text that is set on when ConfirmPassword field is empty.
     * */
    public String getConfirmPasswordEmptyErrorText() {
        return mConfirmPasswordEmptyErrorText;
    }

    /**
     * Set the text to appear, when ConfirmPassword field is empty.
     * */
    public void setConfirmPasswordEmptyErrorText(String mConfirmPasswordEmptyErrorText) {
        this.mConfirmPasswordEmptyErrorText = mConfirmPasswordEmptyErrorText;
    }

    /**
     * Get the text that is set on when passwords do not match.
     * */
    public String getPasswordDoNotMatchErrorText() {
        return mPasswordDoNotMatchErrorText;
    }

    /**
     * Set the text that appears when passwords do not match.
     * */
    public void setPasswordDoNotMatchErrorText(String mPasswordDontMatchErrorText) {
        this.mPasswordDoNotMatchErrorText = mPasswordDontMatchErrorText;
    }

    /**
     * Get the text that appears when password is wrong.
     * */
    public String getWrongPasswordText() {
        return mWrongPasswordText;
    }

    /**
     * Set the text that appears when password is wrong.
     * */
    public void setWrongPasswordText(String mWrongPasswordText) {
        this.mWrongPasswordText = mWrongPasswordText;
    }

    /**
     * Get the text that appears on the Toast message when a field is empty.
     * */
    public String getEmptyFieldErrorTextToast() {
        return mEmptyFieldErrorTextToast;
    }

    /**
     * Set the text that appears on the Toast message when a field is empty.
     * */
    public void setEmptyFieldErrorTextToast(String mEmptyFieldErrorTextToast) {
        this.mEmptyFieldErrorTextToast = mEmptyFieldErrorTextToast;
    }

    /**
     * Get the text that appears when a wrong password is entered.
     * */
    public String getWrongPasswordEnteredText() {
        return mWrongPasswordEnteredText;
    }

    /**
     * Set the text that appears when a wrong password is entered.
     * */
    public void setWrongPasswordEnteredText(String mWrongPasswordEnteredText) {
        this.mWrongPasswordEnteredText = mWrongPasswordEnteredText;
    }

    /**
     * Get the number of maximum password retries.
     * */
    public int getMaxTries() {
        return mMaxTries;
    }

    /**
     * Get the number of maximum password retries.
     * */
    public void setMaxTries(int mMaxTries) {
        this.mMaxTries = mMaxTries;
    }

}
