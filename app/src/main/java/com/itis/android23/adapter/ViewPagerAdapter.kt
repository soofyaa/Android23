package com.itis.android23.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.itis.android23.fragments.QuestionFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val itemCount: Int,
    private val questionCount: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return if (itemCount <= 0) 0 else Integer.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        val realPosition = position % itemCount
        return QuestionFragment.newInstance(realPosition, questionCount)
    }
}