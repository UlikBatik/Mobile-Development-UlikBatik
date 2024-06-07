package com.example.ulikbatik.ui.boarding


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.local.dataStore
import com.example.ulikbatik.databinding.ActivityBoardingBinding
import com.example.ulikbatik.ui.auth.AuthActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class BoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardingBinding
    private lateinit var viewPagerAdapter: BoardingPagerAdapter

    private val titles = arrayOf(
        R.string.onboarding_welcome_title,
        R.string.onboarding_share_title,
        R.string.onboarding_discover_title,
        R.string.onboarding_starting_title,
    )

    private val descriptions = arrayOf(
        R.string.onboarding_welcome_description,
        R.string.onboarding_share_description,
        R.string.onboarding_discover_description,
        R.string.onboarding_starting_description,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerAdapter = BoardingPagerAdapter(this@BoardingActivity)
        binding.slideViewPagerTop.adapter = viewPagerAdapter

        val currentPosition = savedInstanceState?.getInt("current_position") ?: 0
        binding.slideViewPagerTop.setCurrentItem(currentPosition, false)
        updateContent(currentPosition)

        binding.apply {
            slideViewPagerTop.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.titleBoarding.setText(titles[position])
                    binding.descriptionBoarding.setText(descriptions[position])
                }
            }
            )

            TabLayoutMediator(indicatorLayout, slideViewPagerTop) { tab, _ ->
                tab.setIcon(R.drawable.boarding_pager_selector)
            }.attach()

            buttonBoardingNext.setOnClickListener {
                val nextItem = binding.slideViewPagerTop.currentItem + 1
                if (nextItem < viewPagerAdapter.itemCount) {
                    binding.slideViewPagerTop.currentItem = nextItem
                } else {
                    finishOnboarding()
                }
            }
        }
    }

    private fun finishOnboarding() {
        var pref = UserPreferences.getInstance(this.dataStore)

        lifecycleScope.launch{
            pref.setSession()
        }
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun updateContent(position: Int) {
        binding.titleBoarding.setText(titles[position])
        binding.descriptionBoarding.setText(descriptions[position])
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("current_position", binding.slideViewPagerTop.currentItem)
    }
}