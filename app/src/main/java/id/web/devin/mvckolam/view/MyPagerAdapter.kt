package id.web.devin.mvvmkolam.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.web.devin.mvckolam.view.PelatihListFragment
import id.web.devin.mvckolam.view.ProductListFragment

class MyPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    private val fragmentList: List<Fragment> = listOf(
        ProductListFragment(),
        PelatihListFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}
