package com.raju.codechallange.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.raju.codechallange.databinding.ActivityMainBinding
import com.raju.codechallange.network.model.PersonListResponse
import com.raju.codechallange.ui.adapter.PersonAdapter
import com.raju.codechallange.ui.person.PersonState
import com.raju.codechallange.ui.person.PersonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: PersonAdapter

    private val personViewModel: PersonViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observePersonState()
    }

    private fun observePersonState() {
        //diego.anfuso@compass.com
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                personViewModel.state.collect { state ->
                    when (state) {
                        PersonState.Loading -> {
                            Log.d("Raju", "Loading---")
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is PersonState.SuccessResponse -> {
                            Log.e("Raju", "Data---" + state.personListResponse)
                            state.personListResponse.setUptRecyclerView()
                        }

                        is PersonState.Error -> {
                            Log.d("Raju", "Error---" + state.message)
                            handleException(state.message)
                        }
                    }
                }
            }
        }
    }


    private fun PersonListResponse.setUptRecyclerView() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.adapter = adapter
        adapter.submitList(this.people)
    }

    @SuppressLint("SwitchIntDef")
    private fun handleException(error: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(
            this@MainActivity,
            error,
            Toast.LENGTH_SHORT
        ).show()
    }
}