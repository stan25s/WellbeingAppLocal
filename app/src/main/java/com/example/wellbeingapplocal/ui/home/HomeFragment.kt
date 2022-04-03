package com.example.wellbeingapplocal.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellbeingapplocal.*
import com.example.wellbeingapplocal.ChatApp.Companion.user
import com.example.wellbeingapplocal.databinding.FragmentHomeBinding
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val sharedPrefFile = "sharedprefs"

    private lateinit var sessionId: String
    private lateinit var focus: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: MessageAdapter

    private var pusherAppKey = "PUSHER_APP_KEY"
    private var pusherAppCluster = "PUSHER_CLUSTER"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPref = activity?.getSharedPreferences(
            sharedPrefFile, Context.MODE_PRIVATE)
        sessionId = sharedPref?.getString("sessionId", "session").toString()
        //var defStringSet: Set<String>
        var tempFocus = sharedPref?.getStringSet("focuses", null)
        if (!tempFocus.isNullOrEmpty()) {
            var list = tempFocus.toList()
            focus = list.random()
        } else {
            focus = ""
        }

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pusherAppKey = getString(R.string.pusher_key)
        pusherAppCluster = getString(R.string.pusher_cluster)

        setupPusher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageList.layoutManager = LinearLayoutManager(context)
        //messageList.layoutManager = LinearLayoutManager(requireContext())
        adapter = context?.let { MessageAdapter(it) }!!
        messageList.adapter = adapter

        //When this view is initially created, initialise the recyclerview with messages
        viewModel.messages.value?.let { adapter.updateMessages(it) }

        //Only use this for testing!! Then delete
        //val sharedPrefs : SharedPreferences = getp
        //user = "testUser"
        val sharedPreferences: SharedPreferences = view.context
            .getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        user = sharedPreferences.getString("prefName", null).toString()

        if (user.isEmpty()) {
            user = "nullUser"
        }

        btnSend.setOnClickListener {
            if(txtMessage.text.isNotEmpty()) {
                var addBotText = txtMessage.text.toString()
                if(!addBotText.startsWith("@bot")) {
                    addBotText = "@bot" + addBotText
                }
                val message = Message(
                    user,
                    addBotText,
                    Calendar.getInstance().timeInMillis,
                    sessionId,
                    focus
                )
                //TODO: Set-up sessions and send sessionID alongside message content

                println(message)
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
        //val viewModel: HomeViewModel by viewModels()
        viewModel.messages.observe(viewLifecycleOwner, androidx.lifecycle.Observer { messages ->
            for(i in messages) {
                if(!adapter.getMessages().contains(i)) {
                    adapter.addMessage(i);
                }
            }
        })
    }

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
            val jsonObject = JSONObject(data.data)
            //val messageJsonArray = JSONArray(jsonObject["message"])
            val arrayAsString: StringBuilder = StringBuilder()
            var messageJsonArray = JSONArray()
            val user = jsonObject["user"]
            //var messageArray = false
            //var messageStringVal = ""


            if (jsonObject["message"] is JSONArray) {
                messageJsonArray = jsonObject["message"] as JSONArray
            } else if(jsonObject["message"] is String) {
                messageJsonArray = JSONArray(arrayOf(jsonObject["message"] as String))
            }

            var list : Array<String> = Array(messageJsonArray.length()) {
                //Add message strings to list to display:
                //Also remove '[""]' symbols around text and "\"
                messageJsonArray.getString(it)
            }

            var newList = ArrayList<String>()
            if(user == "bot") {
                for(i in list.indices) {
                    var tempString = ""
                    tempString = list[i].substring(2, list[i].length-2)
                    tempString = tempString.replace("\\", "")

                    newList.add(tempString)
                }
            } else {
                for(i in list) {
                    newList.add(i.replace("@bot",""))
                }
            }
            try {
                var messages : ArrayList<Message> = ArrayList()
                for (i in newList) {
                    messages.add(
                        Message(jsonObject["user"] as String, i,
                            jsonObject["time"] as Long, sessionId, focus))
                }

                activity?.runOnUiThread {
                    for(i in messages) {
                        adapter.addMessage(i)
                        viewModel.addMessage(i)
                    }
                    //adapter.addMessage(message)
                    // scroll the RecyclerView to the last added element
                    messageList.scrollToPosition(adapter.itemCount - 1);
                }
            } catch (e : NullPointerException) {
                println(e.message)
                println(e.cause)
            }
        }

        pusher.connect()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
