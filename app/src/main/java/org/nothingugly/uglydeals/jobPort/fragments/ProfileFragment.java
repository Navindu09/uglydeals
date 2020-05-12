package org.nothingugly.uglydeals.jobPort.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.activity.Constants;
import org.nothingugly.uglydeals.jobPort.adapters.RvEducationAdapter;
import org.nothingugly.uglydeals.jobPort.interfaces.RemoveItemInterfaces;
import org.nothingugly.uglydeals.jobPort.models.EducationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements RemoveItemInterfaces {
    public static final int PICK_IMAGE = 1;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.et_summary)
    EditText etSummary;
    @BindView(R.id.tv_education)
    TextView tvEducation;
    @BindView(R.id.iv_add_education)
    ImageView ivAddEducation;
    @BindView(R.id.tv_skills)
    TextView tvSkills;
    @BindView(R.id.et_skills)
    EditText etSkills;
    Unbinder unbinder;
    @BindView(R.id.iv_summary)
    ImageView ivSummary;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.iv_skills)
    ImageView ivSkills;
    @BindView(R.id.et_education_one)
    EditText etEducationOne;
    @BindView(R.id.iv_education_one)
    ImageView ivEducationOne;
    @BindView(R.id.rl_one)
    RelativeLayout rlOne;
    @BindView(R.id.et_education_two)
    EditText etEducationTwo;
    @BindView(R.id.iv_education_two)
    ImageView ivEducationTwo;
    @BindView(R.id.rv_education)
    RecyclerView rvEducation;
    @BindView(R.id.iv_edit_name)
    ImageView ivEditName;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Dialog dialog;
    private TextView tvDone;
    private TextView tvCancel;
    private EditText edt;
    private Bitmap bitmap;
    private ArrayList<EducationModel> educationList;
    private RvEducationAdapter rvEducationAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        educationList = new ArrayList<>();
        show();
        setHasOptionsMenu(true);
        //Initialise Firebase app
        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("customers").document(mAuth.getUid()).
                collection("profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        getProfileData();
                    } else {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("education", "");
                        data.put("professionalSkills", "");
                        data.put("profileImage", "");
                        data.put("summary", "");
                        mFirestore.collection("customers").document(mAuth.getUid()).
                                collection("profile").document("profileId" + mAuth.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mFirestore.collection("customers").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        String userName = (String) documentSnapshot.get("name");
                                        String contact = (String) documentSnapshot.get("mobile");
                                        String emailId = (String) documentSnapshot.get("email");
                                        if (!userName.equalsIgnoreCase("")) {
                                            etName.setText(userName);
                                            etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                                        }
                                        if (!emailId.equalsIgnoreCase("")) {
                                            tvEmail.setText(emailId);
                                        }
                                        if (!contact.equalsIgnoreCase("")) {
                                            tvContact.setText(contact);
                                        }
                                        HashMap<String, Object> educationMap = new HashMap<>();
                                        educationMap.put("educationId", FieldValue.arrayUnion(""));
                                        educationMap.put("education", FieldValue.arrayUnion(""));
                                        mFirestore.collection("customers").document(mAuth.getUid()).
                                                collection("profile").document("education" + mAuth.getUid())
                                                .set(educationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ArrayList<String> arrayList = new ArrayList<>();
                                                arrayList.add("");
                                                EducationModel educationModel = new EducationModel();
                                                educationModel.setEducation(arrayList);
                                                educationModel.setEducationId(arrayList);
                                                ArrayList<EducationModel> list = new ArrayList<>();
                                                list.add(educationModel);
                                                educationList.addAll(list);
                                                setEducationAdapter();
                                            }
                                        });
                                    }
                                });
                                hide();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hide();
                            }
                        });
                    }
                }
            }
        });
        return view;
    }

    private void setEducationAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvEducation.setLayoutManager(linearLayoutManager);
        rvEducationAdapter = new RvEducationAdapter(getActivity(), educationList);
        rvEducation.setAdapter(rvEducationAdapter);
        rvEducationAdapter.setListener(this);
    }

    private void initDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_education);
        dialog.setCanceledOnTouchOutside(false);
        edt = dialog.findViewById(R.id.et_education);
        tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvDone = dialog.findViewById(R.id.tv_done);
        dialog.show();
    }

    private void getProfileData() {
        String userId = mAuth.getUid();
        mFirestore.collection("customers").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    try {
                        String userName = (String) documentSnapshot.get("name");
                        String contact = (String) documentSnapshot.get("mobile");
                        String emailId = (String) documentSnapshot.get("email");
                        if (!userName.equalsIgnoreCase("")) {
                            etName.setText(userName);
                            etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                        }
                        if (!emailId.equalsIgnoreCase("")) {
                            tvEmail.setText(emailId);
                        }
                        if (!contact.equalsIgnoreCase("")) {
                            tvContact.setText(contact);
                        }
                        mFirestore.collection("customers").document(userId).collection("profile").document("profileId" + userId)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String education = (String) documentSnapshot.get("education");
                                String skills = (String) documentSnapshot.get("professionalSkills");
                                String profImage = (String) documentSnapshot.get("profileImage");
                                String summary = (String) documentSnapshot.get("summary");
                                Glide.with(getContext()).load(profImage).into(ivProfile);
                                etSkills.setText(skills);
                                etSummary.setText(summary);
                                mFirestore.collection("customers").document(userId).collection("profile")
                                        .document("education" + userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot1 = task.getResult();
                                            //Convert the particular document into a Deal object
                                            EducationModel educationModel = documentSnapshot1.toObject(EducationModel.class);
                                            ArrayList<EducationModel> list = new ArrayList<>();
                                            list.add(educationModel);
                                            educationList.addAll(list);
                                            educationList.get(0).getEducation().remove(0);
                                            setEducationAdapter();
//                                            rvEducationAdapter.notifyDataSetChanged();
                                        } else {
                                            hide();
                                        }
                                    }
                                });
                            }
                        });
                        hide();
                    } catch (
                            NullPointerException e) {
                        hide();
                        Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_education_one, R.id.iv_education_two, R.id.iv_skills, R.id.iv_summary, R.id.et_name, R.id.iv_profile, R.id.et_summary, R.id.iv_add_education, R.id.et_skills})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_education_one:
                show();
                mFirestore.collection("customers").document(mAuth.getUid()).
                        collection("profile").document("profileId" + mAuth.getUid())
                        .update("educationOne", "").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hide();
                    }
                });
                break;
            case R.id.iv_education_two:
                show();
                mFirestore.collection("customers").document(mAuth.getUid()).
                        collection("profile").document("profileId" + mAuth.getUid())
                        .update("educationTwo", "").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hide();
                    }
                });

                break;
            case R.id.iv_skills:
                initDialog();
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tvDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        show();
                        if (edt.getText() != null) {
                            String getSkills = edt.getText().toString();
                            mFirestore.collection("customers").document(mAuth.getUid()).
                                    collection("profile").
                                    document("profileId" + mAuth.getUid()).update("professionalSkills", getSkills).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    etSkills.setText(getSkills);
                                    edt.setText(null);
                                    Constants.hide(getActivity());
                                    progressBar.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    hide();
                                    dialog.dismiss();
                                    edt.setText(null);
                                }
                            });
                        }
                    }
                });
                break;
            case R.id.iv_summary:
                initDialog();
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tvDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        show();
                        if (edt.getText() != null) {
                            String getSummary = edt.getText().toString();
                            mFirestore.collection("customers").document(mAuth.getUid()).
                                    collection("profile").document("profileId" + mAuth.getUid()).update("summary", getSummary).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    hide();
                                    etSummary.setText(getSummary);
                                    edt.setText(null);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    hide();
                                    dialog.dismiss();
                                    edt.setText(null);
                                }
                            });

                        }
                    }
                });
                break;
            case R.id.iv_profile:
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
                break;
            case R.id.iv_add_education:
                addEducation();
                break;
            case R.id.et_skills:
                break;
        }
    }

    private void addEducation() {
        initDialog();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                show();
                if (edt.getText() != null) {
                    String getSummary = edt.getText().toString();
                    int id = educationList.get(0).getEducation().size();
                    HashMap<String, Object> educationMap = new HashMap<>();
                    educationMap.put("educationId", FieldValue.arrayUnion(id + ""));
                    educationMap.put("education", FieldValue.arrayUnion(getSummary));
                    mFirestore.collection("customers").document(mAuth.getUid()).
                            collection("profile").document("education" + mAuth.getUid()).update(educationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFirestore.collection("customers").document(mAuth.getUid()).collection("profile")
                                    .document("education" + mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot1 = task.getResult();
                                        //Convert the particular document into a Deal object
                                        EducationModel educationModel = documentSnapshot1.toObject(EducationModel.class);
                                        ArrayList<EducationModel> list = new ArrayList<>();
                                        list.add(educationModel);
                                        educationList.clear();
                                        educationList.addAll(list);
                                        educationList.get(0).getEducation().remove(0);
                                        rvEducationAdapter.notifyDataSetChanged();
                                        hide();
                                    } else {
                                        hide();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hide();
                            dialog.dismiss();
                            edt.setText(null);
                        }
                    });

                }
            }
        });
    }

    private void askForPermission(String readExternalStorage, Integer readExst) {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_IMAGE);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                show();
                uploadToFirebase(selectedImage);
            } catch (Exception e) {
                hide();
                Log.e("Error reading file", e.toString());
            }
        } else {
            Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadToFirebase(Uri filePath) {
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("profilePics/" + mAuth.getUid() + ".png");
        riversRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        uri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUri = uri.toString();
                                mFirestore.collection("customers").document(mAuth.getUid()).collection("profile").document("profileId" + mAuth.getUid()).update("profileImage", imageUri)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Profile uploaded", Toast.LENGTH_SHORT).show();
                                                    Glide.with(getContext()).load(imageUri).into(ivProfile);
                                                    hide();
                                                } else {
                                                    hide();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hide();
                        //if the upload is not successfull
                        //hiding the progress dialog
                        //and displaying error message
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void hide() {
        Constants.hide(getActivity());
        progressBar.setVisibility(View.GONE);
    }

    private void show() {
        Constants.showProgessBar(getActivity());
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void addItem() {
        addEducation();
    }

    @Override
    public void remove(int pos) {
        educationList.get(0).getEducation().remove(pos);
        rvEducationAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeItem(String userId) {
        show();
        //Map to remove user from array
        final Map<String, Object> removeUserFromArrayMap = new HashMap<>();
        removeUserFromArrayMap.put("education", FieldValue.arrayRemove(userId));
        mFirestore.collection("customers").document(mAuth.getUid()).
                collection("profile").document("education" + mAuth.getUid()).update(removeUserFromArrayMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hide();
            }
        });
    }

    @OnClick(R.id.iv_edit_name)
    public void onViewClicked() {
        initDialog();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                show();
                if (edt.getText() != null) {
                    String getSummary = edt.getText().toString();
                    HashMap<String, Object> educationMap = new HashMap<>();
                    educationMap.put("name", getSummary);
                    mFirestore.collection("customers").document(mAuth.getUid()).update(educationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            etName.setText(getSummary);
                            etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                            edt.setText(null);
                            hide();
                        }
                    });
                }
            }
        });
    }
}
