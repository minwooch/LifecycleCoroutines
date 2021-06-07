package com.applsh.lifecyclecoroutines.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.applsh.lifecyclecoroutines.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        const val TAG = "LifecycleCoroutines"
    }

    private lateinit var viewModel: MainViewModel

    private var job: Job? = null

    override fun onStart() {
        super.onStart()
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collect {
                // Toast.makeText(MainApplication.ctx, "Fragment - 지금 상태 (${lifecycle.currentState.name})에서 ViewModel에게 받은 값: $it", Toast.LENGTH_SHORT).show()
                Log.v(TAG, "Fragment - 지금 상태 (${lifecycle.currentState.name})에서 ViewModel에게 받은 값: $it")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        job = null
    }

    /*
    override fun onResume() {
        super.onResume()
        job = lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewModel.flow.collect {
                Log.v(TAG, "Fragment - 지금 상태 (${lifecycle.currentState.name})에서 ViewModel에게 받은 값: $it")
            }
        }
    }

    override fun onPause() {
        job?.cancel()
        job = null
        super.onPause()
    }
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        /*
        lifecycleScope.launchWhenStarted {
                viewModel.flow.collect {
                    Log.v(
                        TAG,
                        "Fragment - 지금 상태 (${lifecycle.currentState.name})에서 ViewModel에게 받은 값: $it"
                    )
                }
        }
         */

        lifecycle.addObserver(LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                // viewModel.sendEvent(event.name)
            } else if (event == Lifecycle.Event.ON_START) {

            }
        })

        /*
        addRepeatingJob(Lifecycle.State.STARTED) {
            launch {
                viewModel.flow.collect {
                    Log.v(
                        TAG,
                        "Fragment - 지금 상태 (${lifecycle.currentState.name})에서 ViewModel에게 받은 값: $it"
                    )
                }
            }
        }

         */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }
}