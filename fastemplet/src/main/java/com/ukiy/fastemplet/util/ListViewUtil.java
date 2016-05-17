package com.ukiy.fastemplet.util;

import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * Created by ukiy on 16/5/12.
 */
public class ListViewUtil {

    /**
     * 判断listView是否能上滑
     *
     * @param listView
     * @return 是否能上滑
     */
    public static boolean canScrollUp(ListView listView) {
        return !(
                //第一个可见位置是0，上面也不会有预加载
                listView.getFirstVisiblePosition() == 0
                        //第一个子view的top相对位置大于等于0
                        && listView.getChildAt(0).getTop() >= 0);
    }

    /**
     * 判断listView是否能下滑
     *
     * @param listView
     * @return 是否能下滑
     */
    public static boolean canScrollDown(ListView listView) {
        return !(
                listView.getLastVisiblePosition() == listView.getCount() - 1//最后一个可见位置是getCount，下面也不会有预加载
                        //最后一个子view的bottom相对位置小于等于listview高度
                        && listView.getChildAt(listView.getChildCount() - 1).getBottom() <= listView.getHeight());
    }

    /**
     * 设置listView的最大可越界滑动距离
     *
     * @param listView
     * @param maxOverScrollPixel 最大可滑动距离
     */
    public static void setMaxOverScrollPixel(ListView listView, int maxOverScrollPixel) {
        try {
            Field field = AbsListView.class.getDeclaredField("mOverscrollMax");
            field.set(listView, maxOverScrollPixel);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
