package net.zaim.decoratecalendarview;

import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewDecorateCalendarFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public static NewDecorateCalendarFragment newInstance(int year, int month) {
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        NewDecorateCalendarFragment fragment = new NewDecorateCalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int year = args.getInt("year");
        int month = args.getInt("month");
        View view = inflater.inflate(R.layout.fragment_new_decorate_calendar, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mRecyclerView.addItemDecoration(new DividerItemDecoration());
        mRecyclerView.setAdapter(new NewAdapter(getActivity(), year, month));
        return view;
    }

    private static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private GradientDrawable mDivider;

        public DividerItemDecoration() {
            mDivider = new GradientDrawable();
            mDivider.setStroke(1, 0x0c000000);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                int left = childView.getLeft();
                int right = childView.getRight();
                int top = childView.getTop();
                int bottom = childView.getBottom();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
