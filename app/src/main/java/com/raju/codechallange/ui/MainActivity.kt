package com.raju.codechallange.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.raju.codechallange.databinding.ActivityMainBinding
import com.raju.codechallange.domain.annotation.ExceptionType.Companion.TOAST
import com.raju.codechallange.domain.exception.BaseException
import com.raju.codechallange.ui.adapter.CountryAdapter
import com.raju.codechallange.ui.viewmodel.CountryViewModel
import com.raju.codechallange.ui.viewmodel.CountryViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: CountryAdapter

    private val viewModel: CountryViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when {
                        state.isLoading -> binding.progressBar.visibility = View.VISIBLE
                        state.exception != null -> state.handleException(state.exception)
                        state.countryList.isNotEmpty() -> state.setUptRecyclerView()
                    }
                }
            }
        }
    }

    private fun CountryViewState.setUptRecyclerView() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.adapter = adapter
        adapter.submitList(this.countryList)
    }

    @SuppressLint("SwitchIntDef")
    private fun CountryViewState.handleException(exception: BaseException) {
        binding.progressBar.visibility = View.GONE
        when (exception.type) {
            TOAST -> {
                Toast.makeText(
                    this@MainActivity,
                    this.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}