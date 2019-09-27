package com.ue.uebook.LoginActivity.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.Data.NetworkAPI;
import com.ue.uebook.Data.NetworkService;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.Quickblox_Chat.App;
import com.ue.uebook.Quickblox_Chat.utils.SharedPrefsHelper;
import com.ue.uebook.Quickblox_Chat.utils.chat.ChatHelper;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignIn_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignIn_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignIn_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button login_btn;
    private TextView create_AccountBtn;
    private NetworkAPI networkAPI;
    private OnFragmentInteractionListener mListener;
    private EditText userName, userPassword;
    private CheckBox keepMeSign;
    private LinearLayout fragment;
    private static final int UNAUTHORIZED = 401;
    public SignIn_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignIn_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignIn_Fragment newInstance(String param1, String param2) {
        SignIn_Fragment fragment = new SignIn_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkAPI = NetworkService.getAPI().create(NetworkAPI.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_, container, false);

        login_btn = view.findViewById(R.id.login_btn);
        create_AccountBtn = view.findViewById(R.id.create_AccountBtn);
        userName = view.findViewById(R.id.user_login);
        userPassword = view.findViewById(R.id.password_login);
        keepMeSign = view.findViewById(R.id.keepMESIgned_Btn);
        fragment = view.findViewById(R.id.fragment);
        create_AccountBtn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view == login_btn) {

            if (isvalidate()) {
                String user = userName.getText().toString().trim();
                String userpass = userPassword.getText().toString().trim();
                requestforLogin(user, userpass);
            }


        } else if (view == create_AccountBtn) {

            loadFragment(new SignUp_Fragment());
        }
    }

    public void gotoHome() {
        Intent intent = new Intent(getActivity(), HomeScreen.class);
        intent.putExtra("login",1);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_Fragment, fragment);
        transaction.commit();
    }

//    public void userLogin(final String username, final String passwordu) {
//
//        RequestBody lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), pFile);
//        MultipartBody.Part user_name = MultipartBody.Part.createFormData("user_name", "vansh");
//        MultipartBody.Part password = MultipartBody.Part.createFormData("password", "vans@123");
//        Call<LoginResponse> loginResponseCall = networkAPI.userLogin(user_name,password);
//        loginResponseCall.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
//                LoginResponse loginResponse = response.body();
//
//                Log.w("Full",new Gson().toJson(response));
//
//
//                if (loginResponse != null) {
//
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
//                Log.d("error", "error");
//            }
//        });
//    }


    private Boolean isvalidate() {
        String user_NAme = userName.getText().toString();
        String userpass = userPassword.getText().toString();
        if (!user_NAme.isEmpty()) {
            if (!userpass.isEmpty()) {
                return true;
            } else {
                userPassword.setError("Enter your Password");
                userPassword.requestFocus();
                userPassword.setEnabled(true);
                return false;
            }

        } else {
            userName.setError("Enter your Username");
            userName.requestFocus();
            userName.setEnabled(true);

            return false;
        }
    }


    public void requestforLogin(final String user_name, final String password) {
        String url = null;
        url = "http://dnddemo.com/ebooks/api/v1/userLogin";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_name", user_name)
                .addFormDataPart("password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                final String myResponse = e.getLocalizedMessage();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final LoginResponse form = gson.fromJson(myResponse, LoginResponse.class);
                if (form.getError() == false) {

                    new SessionManager(getContext().getApplicationContext()).storeUserPublishtype(form.getResponse().getPublisher_type());
                    new SessionManager(getContext().getApplicationContext()).storeUseruserID(form.getResponse().getId());
                    new SessionManager(getContext().getApplicationContext()).storeUserName(form.getResponse().getUser_name());
                    getActivity().runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
//                            Toast.makeText(getContext(), "Succesfully Login", Toast.LENGTH_SHORT).show();
                            if (keepMeSign.isChecked()) {
                                new SessionManager(getContext().getApplicationContext()).storeUserLoginStatus(1);

                            } else {
                                new SessionManager(getContext().getApplicationContext()).storeUserLoginStatus(0);

                            }

                            String arr[] = form.getResponse().getUser_name().split(" ", 2);
                            String firstWord = arr[0];   //the
                            QBUser qbUser = new QBUser();
                            qbUser.setLogin(firstWord.trim());
                            qbUser.setFullName(form.getResponse().getUser_name());
                            qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
                            signIn(qbUser);
                        }
                    });




                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            showDialogWithOkButton("Login Error", form.getMessage());
                            final PrettyDialog pDialog=  new PrettyDialog(getActivity());
                            pDialog  .setTitle("Login Error");
                            pDialog.setIcon(R.drawable.cancel);
                            pDialog.setMessage(form.getMessage());
                            pDialog   .addButton(
                                            "OK",					// button text
                                            R.color.pdlg_color_white,		// button text color
                                            R.color.colorPrimary,		// button background color
                                            new PrettyDialogCallback() {		// button OnClick listener
                                                @Override
                                                public void onClick() {
                                                    pDialog.dismiss();
                                                }
                                            }
                                    )
                                    .show();

                        }
                    });
                }


            }
        });

    }


    public void showDialogWithOkButton(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(title);
        //dialog.setCancelable(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton(getString(R.string.ok_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void signIn(final QBUser user) {
        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                if (userFromRest.getFullName().equals(user.getFullName())) {
                    loginToChat(user);
                } else {
                    //Need to set password NULL, because server will update user only with NULL password
                    user.setPassword(null);
                    updateUser(user);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                if (e.getHttpStatusCode() == UNAUTHORIZED) {
                    signUp(user);
                } else {

                    signIn(user);
                }


            }
        });
    }

    private void updateUser(final QBUser user) {
        ChatHelper.getInstance().updateUser(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                loginToChat(user);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void loginToChat(final QBUser user) {
        //Need to set password, because the server will not register to chat without password
        user.setPassword(App.USER_DEFAULT_PASSWORD);
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
                updateUserChatId(String.valueOf(user.getId()));
                gotoHome();
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void signUp(final QBUser newUser) {
        SharedPrefsHelper.getInstance().removeQbUser();
        QBUsers.signUp(newUser).performAsync(new QBEntityCallback<QBUser>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {

                signIn(newUser);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateUserChatId(String chatID ) {
        ApiRequest request = new ApiRequest();
        request.requestforPostChatId(new SessionManager(getApplicationContext()).getUserID(),chatID, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();


            }
        });
    }


}
