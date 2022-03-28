package com.example.wellbeingapplocal.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellbeingapplocal.Message
import com.example.wellbeingapplocal.MessageAdapter
import com.example.wellbeingapplocal.R
import com.example.wellbeingapplocal.databinding.FragmentHomeBinding
import com.pusher.pusherchat.ChatService
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wellbeingapplocal.ChatApp
import kotlinx.android.synthetic.main.fragment_home.*
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val sharedPrefFile = "sharedprefs"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: MessageAdapter

    private val pusherAppKey = "PUSHER_APP_KEY"
    private val pusherAppCluster = "PUSHER_APP_CLUSTER"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageList.layoutManager = LinearLayoutManager(context)
        //messageList.layoutManager = LinearLayoutManager(requireContext())
        adapter = context?.let { MessageAdapter(it) }!!
        messageList.adapter = adapter

        //Only use this for testing!! Then delete
        ChatApp.user = "testUser"

        btnSend.setOnClickListener {
            if(txtMessage.text.isNotEmpty()) {
                val message = Message(
                    ChatApp.user,
                    txtMessage.text.toString(),
                    Calendar.getInstance().timeInMillis
                )

                val call = ChatService.create().postMessage(message)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        resetInput()
                        if (!response.isSuccessful) {
                            Log.e(TAG, response.code().toString());
                            Toast.makeText(context,"Response was not successful", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        resetInput()
                        Log.e(TAG, t.toString());
                        Toast.makeText(context,"Error when calling the service", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context,"Message should not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        setupPusher()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //setContentView(R.layout.fragment_home)
//
////        if(binding == null) {
////
////        }
//
////        messageList.layoutManager = LinearLayoutManager(context)
////        //messageList.layoutManager = LinearLayoutManager(requireContext())
////        adapter = context?.let { MessageAdapter(it) }!!
////        messageList.adapter = adapter
//
//        btnSend.setOnClickListener {
//            if(txtMessage.text.isNotEmpty()) {
//                val message = Message(
//                    ChatApp.user,
//                    txtMessage.text.toString(),
//                    Calendar.getInstance().timeInMillis
//                )
//
//                val call = ChatService.create().postMessage(message)
//
//                call.enqueue(object : Callback<Void> {
//                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                        resetInput()
//                        if (!response.isSuccessful) {
//                            Log.e(TAG, response.code().toString());
//                            Toast.makeText(context,"Response was not successful", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Void>, t: Throwable) {
//                        resetInput()
//                        Log.e(TAG, t.toString());
//                        Toast.makeText(context,"Error when calling the service", Toast.LENGTH_SHORT).show()
//                    }
//                })
//            } else {
//                Toast.makeText(context,"Message should not be empty", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        setupPusher()
//    }

    private fun resetInput() {
        // Clean text box
        txtMessage.text.clear()

        // Hide keyboard

        fun Fragment.hideKeyboard() = view?.let { ViewCompat.getWindowInsetsController(it)?.hide(WindowInsetsCompat.Type.ime()) }
        hideKeyboard()
    }

    private fun setupPusher() {
        val options = PusherOptions()
        options.setCluster(pusherAppCluster)

        val pusher = Pusher(pusherAppKey, options)
        val channel = pusher.subscribe("chat")

        channel.bind("new_message") { data ->
            //val jsonObject = data.

            val message = Message(
                data.getProperty("user").toString(),
                data.getProperty("message").toString(),
                data.getProperty("time") as Long
            )

            activity?.runOnUiThread {
                adapter.addMessage(message)
                // scroll the RecyclerView to the last added element
                messageList.scrollToPosition(adapter.itemCount - 1);
            }

        }

        pusher.connect()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}