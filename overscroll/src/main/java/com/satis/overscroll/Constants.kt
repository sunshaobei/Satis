package com.satis.overscroll

interface Constants {
    companion object {
        const val STARTNESTEDSCROLL =
            "execution(* androidx.core.view.NestedScrollingChild2+.startNestedScroll(..))"
        const val DISPATCHNESTEDPRESCROLL =
            "execution(* androidx.core.view.NestedScrollingChild2+.dispatchNestedPreScroll(..))"
        const val DISPATCHNESTEDSCROLL =
            "execution(* androidx.core.view.NestedScrollingChild2+.dispatchNestedScroll(..))"
        const val DISPATCHNESTEDPREFLING =
            "execution(* androidx.core.view.NestedScrollingChild+.dispatchNestedPreFling(..))"
        const val STOPNESTEDSCROLL =
            "execution(* androidx.core.view.NestedScrollingChild2+.stopNestedScroll(..))"
    }
}