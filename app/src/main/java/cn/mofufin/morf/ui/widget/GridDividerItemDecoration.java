package cn.mofufin.morf.ui.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.List;

import cn.mofufin.morf.ui.entity.Coupons;

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
//    private Context mContext;
    private List<Coupons.DataBean.ListBean> mList;

    public GridDividerItemDecoration(List<Coupons.DataBean.ListBean> list) {
        super();
//        this.mContext = context;
        mList = list;

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int position = parent.getChildAdapterPosition(view);
            Coupons.DataBean.ListBean bean = mList.get(position);
            if (bean != null && bean.isTitle()) {
                outRect.set(0, 30, 0, 30);
            } else {
                outRect.set(35, 5, 35, 50);
            }
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            outRect.set(0, 0, 0, 5);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
