// Generated by view binder compiler. Do not edit!
package com.example.namespace.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.namespace.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutTickerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Guideline guidelineVerticalCenter;

  @NonNull
  public final RecyclerView rvHours;

  @NonNull
  public final RecyclerView rvMinutes;

  @NonNull
  public final RecyclerView tvAmpm;

  private LayoutTickerBinding(@NonNull ConstraintLayout rootView,
      @NonNull Guideline guidelineVerticalCenter, @NonNull RecyclerView rvHours,
      @NonNull RecyclerView rvMinutes, @NonNull RecyclerView tvAmpm) {
    this.rootView = rootView;
    this.guidelineVerticalCenter = guidelineVerticalCenter;
    this.rvHours = rvHours;
    this.rvMinutes = rvMinutes;
    this.tvAmpm = tvAmpm;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutTickerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutTickerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_ticker, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutTickerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.guideline_vertical_center;
      Guideline guidelineVerticalCenter = ViewBindings.findChildViewById(rootView, id);
      if (guidelineVerticalCenter == null) {
        break missingId;
      }

      id = R.id.rv_hours;
      RecyclerView rvHours = ViewBindings.findChildViewById(rootView, id);
      if (rvHours == null) {
        break missingId;
      }

      id = R.id.rv_minutes;
      RecyclerView rvMinutes = ViewBindings.findChildViewById(rootView, id);
      if (rvMinutes == null) {
        break missingId;
      }

      id = R.id.tv_ampm;
      RecyclerView tvAmpm = ViewBindings.findChildViewById(rootView, id);
      if (tvAmpm == null) {
        break missingId;
      }

      return new LayoutTickerBinding((ConstraintLayout) rootView, guidelineVerticalCenter, rvHours,
          rvMinutes, tvAmpm);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
