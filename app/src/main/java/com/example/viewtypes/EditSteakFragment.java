package com.example.viewtypes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.viewtypes.ui.EditDTEFragmentArgs;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditSteakFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditSteakFragment extends Fragment {

    protected ChipGroup chipGroup;
    protected Chip chip1, chip2, chip3, chip4, chip5, chip6, chip7;
    private SwitchMaterial swRepeatSteaks;
    private MaterialToolbar topBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditSteakFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditSteakFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditSteakFragment newInstance(String param1, String param2) {
        EditSteakFragment fragment = new EditSteakFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_steak, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText name = view.findViewById(R.id.etSteakName);

        chipGroup = view.findViewById(R.id.chipGroup);

        chip1 = view.findViewById(R.id.chip1);
        chip2 = view.findViewById(R.id.chip2);
        chip3 = view.findViewById(R.id.chip3);
        chip4 = view.findViewById(R.id.chip4);
        chip5 = view.findViewById(R.id.chip5);
        chip6 = view.findViewById(R.id.chip6);
        chip7 = view.findViewById(R.id.chip7);

        swRepeatSteaks = view.findViewById(R.id.swRepeatSteaks);



        EditSteakFragmentArgs args = EditSteakFragmentArgs.fromBundle(getArguments());

        if (args.getSteak() != null){
            name.setText(args.getSteak().title);
            if (!args.getSteak().days.isEmpty()){
                swRepeatSteaks.setChecked(true);
                chipGroup.setVisibility(View.VISIBLE);
                setChipGroupFromDateList(args.getSteak().days);
            }
            else {
                swRepeatSteaks.setChecked(false);
                chipGroup.setVisibility(View.GONE);
            }
        }

        swRepeatSteaks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chipGroup.setVisibility(View.VISIBLE);
                }
                else{
                    chipGroup.setVisibility(View.GONE);
                }
            }
        });

        topBar = view.findViewById(R.id.topBar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EditSteakFragmentDirections.ActionEditSteakFragmentToNavigationAnimal direction = EditSteakFragmentDirections.actionEditSteakFragmentToNavigationAnimal();

                switch (item.getItemId()){
                    case (R.id.miDone):
                        Steak steak = args.getSteak();
                        steak.title = name.getText().toString();
                        steak.days = getDateListFromChipGroup();
                        direction.setSteak(steak);
                        Navigation.findNavController(getView()).navigate(direction);
                }
                return true;
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

    protected void setChipGroupFromDateList(List<Integer> dow){
        if (dow.contains(1)){
            chip1.setChecked(true);
        }
        if (dow.contains(2)){
            chip2.setChecked(true);
        }
        if (dow.contains(3)){
            chip3.setChecked(true);
        }
        if (dow.contains(4)){
            chip4.setChecked(true);
        }
        if (dow.contains(5)){
            chip5.setChecked(true);
        }
        if (dow.contains(6)){
            chip6.setChecked(true);
        }
        if (dow.contains(7)){
            chip7.setChecked(true);
        }
    }
}
