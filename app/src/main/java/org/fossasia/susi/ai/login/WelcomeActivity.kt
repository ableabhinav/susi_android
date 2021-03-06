package org.fossasia.susi.ai.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button

import org.fossasia.susi.ai.R
import org.fossasia.susi.ai.helper.PrefManager

class WelcomeActivity : AppCompatActivity() {

    private var imageViewPager: ViewPager? = null
    private var layouts: IntArray? = null
    private var skip: Button? = null
    private var next: Button? = null

    private val viewPagerPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            if (position == layouts?.size?.minus(1)) {
                next?.text = getString(R.string.start)
                skip?.visibility = View.GONE
            } else {
                next?.text = getString(R.string.next)
                skip?.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            // empty body
        }

        override fun onPageScrollStateChanged(arg0: Int) {
            //empty body
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PrefManager.getBoolean(R.string.activity_executed_key, false)) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)

        imageViewPager = findViewById<View>(R.id.pager) as ViewPager
        val tabLayout = findViewById<View>(R.id.tabDots) as TabLayout
        tabLayout.setupWithViewPager(imageViewPager, true)
        skip = findViewById(R.id.btn_skip)
        next = findViewById(R.id.btn_next)
        layouts = intArrayOf(R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3, R.layout.welcome_slide4)

        val myViewPagerAdapter = MyViewPagerAdapter()
        imageViewPager?.adapter = myViewPagerAdapter
        imageViewPager?.addOnPageChangeListener(viewPagerPageChangeListener)

        skip?.setOnClickListener { launchHomeScreen() }
        next?.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(1)
            val currentLayouts = layouts
            if (currentLayouts != null && current != null) {
                if (current < currentLayouts.size) {
                    //  to next screen
                    imageViewPager?.currentItem = current
                } else {
                    launchHomeScreen()
                }
            }
        }
    }

    private fun getItem(i: Int): Int? {
        return imageViewPager?.currentItem?.plus(i)
    }

    private fun launchHomeScreen() {
        startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
        finish()
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (`object` is View) container.removeView(`object`)
        }
    }
}