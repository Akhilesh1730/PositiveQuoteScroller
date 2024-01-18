import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.positivequotescroller.view.QuotesFragment
import com.example.positivequotescroller.view.SavedQuotesFragment

class QuotesPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> QuotesFragment()
            1 -> SavedQuotesFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
