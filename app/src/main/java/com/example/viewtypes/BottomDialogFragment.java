package com.example.viewtypes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class BottomDialogFragment extends BottomSheetDialogFragment {

    Chip chip1, chip2, chip3, chip4, chip5, chip6, chip7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText name = view.findViewById(R.id.etSteakName);

        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);

        chip1 = view.findViewById(R.id.chip1);
        chip2 = view.findViewById(R.id.chip2);
        chip3 = view.findViewById(R.id.chip3);
        chip4 = view.findViewById(R.id.chip4);
        chip5 = view.findViewById(R.id.chip5);
        chip6 = view.findViewById(R.id.chip6);
        chip7 = view.findViewById(R.id.chip7);

        TextView addBtn = view.findViewById(R.id.tvAdd);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Steak steak = new Steak();
                steak.days = getDateListFromChipGroup();
                steak.title = name.getText().toString();

                BottomDialogFragmentDirections.ActionBottomDialogFragmentToNavigationEdit direction = BottomDialogFragmentDirections.actionBottomDialogFragmentToNavigationEdit();
                direction.setSteak(steak);
                dismiss();
                Navigation.findNavController(requireParentFragment().getView()).navigate(direction);
            }
        });
    }

    protected List<Integer> getDateListFromChipGroup(){
        List<Integer> res = new ArrayList<>();
        if (chip1.isChecked())
            res.add(DateTimeConstants.MONDAY);
        if (chip2.isChecked())
            res.add(DateTimeConstants.TUESDAY);
        if (chip3.isChecked())
            res.add(DateTimeConstants.WEDNESDAY);
        if (chip4.isChecked())
            res.add(DateTimeConstants.THURSDAY);
        if (chip5.isChecked())
            res.add(DateTimeConstants.FRIDAY);
        if (chip6.isChecked())
            res.add(DateTimeConstants.SATURDAY);
        if (chip7.isChecked())
            res.add(DateTimeConstants.SUNDAY);
        return res;
    }
}
