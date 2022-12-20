package com.example.stories.Fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.stories.Models.Story
import com.example.stories.databinding.FragmentStoriesBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit



class StoriesFragment (val stories: ArrayList<Story>): Fragment() {
    private lateinit var storiesList: ArrayList<Story>
    private lateinit var binding : FragmentStoriesBinding
     var mDisposable: Disposable? = null
    var mCurrentProgress: Long = 0
    var mCurrentIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storiesList =stories
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoriesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImageStatusData()
        setProgressData()
        startViewing()
        binding.llStatus.setOnTouchListener(onTouchListener)
    }
    private fun startViewing() {
        binding.llStatus[0].visibility = View.VISIBLE
        emitStatusProgress()

    }

    private fun setImageStatusData() {
        storiesList.forEach { storyImage ->
            val imageView: ImageView = ImageView(this.context)
            imageView.layoutParams = ViewGroup.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            imageView.visibility = View.GONE
            Glide.with(this)
                .load(storyImage.link)
                .into(imageView)
            binding.llStatus.addView(imageView)
        }
    }

    private fun setProgressData() {
        binding.llProgressBar.weightSum = storiesList.size.toFloat()
        storiesList.forEachIndexed { index,  progressData ->
            val progressBar = ProgressBar(this.context, null, android.R.attr.progressBarStyleHorizontal)
            val params = LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            params.height = convertDpToPixel(8f).toInt()
            if (index != 3) {
                params.marginEnd = convertDpToPixel(10f).toInt()
            }
            progressBar.layoutParams = params
            progressBar.max = 50
            progressBar.indeterminateDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
            progressBar.progress = 0
            binding.llProgressBar.addView(progressBar)

        }
    }

    private fun emitStatusProgress() {

        mDisposable = io.reactivex.Observable.intervalRange(mCurrentProgress, 50-mCurrentProgress, 0, 100, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.computation())
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                moveToNextStatus()

            }
            .subscribe({
                updateProgress(it)
            }, {
                it.printStackTrace()
            })
    }

    private fun updateProgress(progress: Long) {
        mCurrentProgress = progress
        requireActivity().runOnUiThread {
            (binding.llProgressBar[mCurrentIndex] as? ProgressBar)?.progress = progress.toInt()
        }
    }

    private fun moveToNextStatus() {
        if ( mCurrentIndex < storiesList.size-1) {
            mCurrentProgress = 0
            mDisposable?.dispose()
            mDisposable = null
            requireActivity().runOnUiThread {
                binding.llStatus[mCurrentIndex].visibility = View.GONE
                mCurrentIndex++
                binding.llStatus[mCurrentIndex].visibility = View.VISIBLE
            }
            if (mCurrentIndex != storiesList.size-1)
                emitStatusProgress()
        } else {
            mDisposable?.dispose()
            mDisposable = null
            val fragment = parentFragmentManager.findFragmentByTag("storyFragment")
            parentFragmentManager.beginTransaction().remove(fragment!!).commit()
        }
    }

    var startTime: Long = System.currentTimeMillis()

    private val onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis()
                pauseStatus()
                return@OnTouchListener true
            }
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - startTime > 2000) {
                    resumeStatus()
                } else {
                    onSingleTapClicked(event.x)
                }
                startTime = 0
                return@OnTouchListener true
            }
            MotionEvent.ACTION_BUTTON_RELEASE -> {
                resumeStatus()
                return@OnTouchListener true
            }
        }
        false
    }

    private fun pauseStatus() {
        mDisposable?.dispose()
        mDisposable = null
    }
    private fun resumeStatus() {
        emitStatusProgress()
    }

    private fun onSingleTapClicked(x: Float) {
        if (x < getScreenWidth()/2) {
            startPreviousStatus()
        } else {
            startStatusNext()
        }
    }
    fun getScreenWidth(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.widthPixels
    }
    private fun startPreviousStatus() {
        mCurrentProgress = 0
        requireActivity().runOnUiThread {
            if (mCurrentIndex != 0) {
                (binding.llProgressBar[mCurrentIndex] as? ProgressBar)?.progress = 0
                binding.llStatus[mCurrentIndex].visibility = View.GONE
                mCurrentIndex--
                binding.llStatus[mCurrentIndex].visibility = View.VISIBLE
                if (mCurrentIndex != storiesList.size-1)
                    emitStatusProgress()
            } else {
                mCurrentIndex = 0
                (binding.llProgressBar[mCurrentIndex] as? ProgressBar)?.progress = 0
                binding.llStatus[mCurrentIndex].visibility = View.VISIBLE
                emitStatusProgress()
            }
        }
    }
    private fun startStatusNext() {
        mCurrentProgress = 0
        requireActivity().runOnUiThread {
            if (mCurrentIndex != storiesList.size-1) {
                (binding.llProgressBar[mCurrentIndex] as? ProgressBar)?.progress = 50
                binding.llStatus[mCurrentIndex].visibility = View.GONE
                mCurrentIndex++
                binding.llStatus[mCurrentIndex].visibility = View.VISIBLE
                (binding.llProgressBar[mCurrentIndex] as? ProgressBar)?.progress = 0
                emitStatusProgress()
            }
        }
    }

    fun convertDpToPixel(dp: Float): Float {
        val resources = this.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }

}