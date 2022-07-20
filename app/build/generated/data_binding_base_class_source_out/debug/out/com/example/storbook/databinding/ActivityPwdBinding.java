// Generated by view binder compiler. Do not edit!
package com.example.storbook.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.storbook.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPwdBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView albumName;

  @NonNull
  public final ImageButton albumbtn;

  @NonNull
  public final TextView peoplePage;

  @NonNull
  public final ImageButton peoplebtn;

  @NonNull
  public final RelativeLayout pwsPage;

  @NonNull
  public final Toolbar toolbar;

  private ActivityPwdBinding(@NonNull RelativeLayout rootView, @NonNull TextView albumName,
      @NonNull ImageButton albumbtn, @NonNull TextView peoplePage, @NonNull ImageButton peoplebtn,
      @NonNull RelativeLayout pwsPage, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.albumName = albumName;
    this.albumbtn = albumbtn;
    this.peoplePage = peoplePage;
    this.peoplebtn = peoplebtn;
    this.pwsPage = pwsPage;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPwdBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPwdBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_pwd, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPwdBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.album_name;
      TextView albumName = ViewBindings.findChildViewById(rootView, id);
      if (albumName == null) {
        break missingId;
      }

      id = R.id.albumbtn;
      ImageButton albumbtn = ViewBindings.findChildViewById(rootView, id);
      if (albumbtn == null) {
        break missingId;
      }

      id = R.id.people_page;
      TextView peoplePage = ViewBindings.findChildViewById(rootView, id);
      if (peoplePage == null) {
        break missingId;
      }

      id = R.id.peoplebtn;
      ImageButton peoplebtn = ViewBindings.findChildViewById(rootView, id);
      if (peoplebtn == null) {
        break missingId;
      }

      id = R.id.pws_page;
      RelativeLayout pwsPage = ViewBindings.findChildViewById(rootView, id);
      if (pwsPage == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityPwdBinding((RelativeLayout) rootView, albumName, albumbtn, peoplePage,
          peoplebtn, pwsPage, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
