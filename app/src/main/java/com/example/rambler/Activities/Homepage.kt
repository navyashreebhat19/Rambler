package com.example.rambler.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rambler.Modules.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE
import com.example.rambler.Modules.HomeViewModal
import com.example.rambler.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.example.rambler.Modules.getAccount
import com.example.rambler.Modules.getFitnessOptions
import kotlinx.android.synthetic.main.activity_main.*



class Homepage : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModal
    private var adapter: StepsCountAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(HomeViewModal::class.java)

        registerObservers()
        showPageUI()
        checkForPermissions()
    }



    private fun showPageUI() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        steps_list.layoutManager = layoutManager

        adapter = StepsCountAdapter()
        steps_list.adapter = adapter

        sort_imageview?.setOnClickListener {
            viewModel.reverseOrder()
        }

        sort_reverse_button?.setOnClickListener {
            viewModel.reverseOrder()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                viewModel.reloadSteps()
            }
        }
    }

    private fun registerObservers() {
        viewModel.getSortedStepsSummaries().observe(this, Observer { data ->
//            Timber.d("stepSummaries count: ${summaries.size}")
            adapter?.setList(data)

            if (data.isNotEmpty()) {
                steps_list.visibility = View.VISIBLE
                empty_results_textview?.visibility = View.GONE
            } else {
                steps_list.visibility = View.GONE
                empty_results_textview?.visibility = View.VISIBLE
            }

            if (viewModel.setSortedAscending()) {
                sort_imageview.setImageResource(R.drawable.ic_arrow_up_24)
            } else {
                sort_imageview.setImageResource(R.drawable.ic_arrow_down_24)
            }

            steps_list?.smoothScrollToPosition(0)
        })
    }

    private fun checkForPermissions() {
        val fit = getFitnessOptions()
        val account =
            getAccount(this, fit)

        if (!GoogleSignIn.hasPermissions(account, fit)) {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account,
                fit
            );
        } else {
            viewModel.reloadSteps();
        }
    }
}