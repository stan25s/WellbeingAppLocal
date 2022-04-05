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

    private var paramsobj : JSONObject = JSONObject()

    private var mq1 : String = ""
    private var mq2 : String = ""
    private var fq1 : String = ""
    private var jou : String = ""

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
        val tempFocus = sharedPref?.getStringSet("focuses", null)
        if (!tempFocus.isNullOrEmpty()) {
            val list = tempFocus.toList()
            focus = list.random()
            println(list.toString())
        } else {
            focus = "_empty"
        }

        if (!viewModel.checkInData.value?.get("mq1").isNullOrEmpty()) {
            mq1 = viewModel.checkInData.value?.get("mq1").toString()
        }
        if (!viewModel.checkInData.value?.get("mq2").isNullOrEmpty()) {
            mq2 = viewModel.checkInData.value?.get("mq2").toString()
        }
        if (!viewModel.checkInData.value?.get("fq1").isNullOrEmpty()) {
            fq1 = viewModel.checkInData.value?.get("fq1").toString()
        }
        if (!viewModel.checkInData.value?.get("jou").isNullOrEmpty()) {
            jou = viewModel.checkInData.value?.get("jou").toString()
        }
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
                    focus,
                    //Add params jsonObject here
                    mq1,
                    mq2,
                    fq1,
                    jou
                )
                //TODO: Set-up sessions and send sessionID alongside message content

//                if (mq1Received) {
//                    //handle saving question responses here
//                    viewModel.addAnswerToMap("mq1", txtMessage.text.toString())
//                    mq1Received = false
//                } else if (mq2Received) {
//                    //handle saving question responses here
//
//                    mq2Received = false
//                } else if (fq1Received) {
//                    //handle saving question responses here
//
//                    fq1Received = false
//                } else if (jouReceived) {
//                    //handle saving question responses here
//
//                    jouReceived = false
//                }

                println(message)
                val call = ChatService.create().postMessage(message)
                resetInput()
                call.enqueue(object : Callback<Void> {

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        //resetInput()
                        if (!response.isSuccessful) {
                            Log.e(TAG, response.code().toString());
                            Toast.makeText(context,"Response was not successful", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        //resetInput()
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

        viewModel.checkInData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { checkInData ->
            //Not sure if I need this Observer
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
            var messageJsonArray = JSONArray()
            val user = jsonObject["user"]

            if (jsonObject["message"] is JSONArray) {
                messageJsonArray = jsonObject["message"] as JSONArray
            } else if(jsonObject["message"] is String) {
                messageJsonArray = JSONArray(arrayOf(jsonObject["message"] as String))
            }

            val list : Array<String> = Array(messageJsonArray.length()) {
                //Add message strings to list to display:
                //Also remove '[""]' symbols around text and "\"
                messageJsonArray.getString(it)
            }

            val newList = ArrayList<String>()
            if(user == "bot") {
                for(i in list.indices) {
                    var tempString = ""
                    tempString = list[i].substring(2, list[i].length-2)
                    tempString = tempString.replace("\\", "")

                    //Check for code at end of message
//                    if (tempString.last() == '}') {
//                        val messageCode = tempString.split("}")
//                        when (messageCode[1]) {
//                            "mq1" -> mq1Received = true
//                            "mq2" -> mq2Received = true
//                            "fq1" -> fq1Received = true
//                            "jou" -> jouReceived = true
//                            "smq1" -> {
//                                viewModel.checkInData.value?.set("mq1", "skipped")
//                                mq1Received = false
//                            }
//                            "smq2" -> {
//                                viewModel.checkInData.value?.set("mq2", "skipped")
//                                mq2Received = false
//                            }
//                            "sfq1" -> {
//                                viewModel.checkInData.value?.set("fq1", "skipped")
//                                fq1Received = false
//                            }
//                            "sjou" -> {
//                                viewModel.setJournalString("")
//                                jouReceived = false
//                            }
//                        }
//                    }
                    if (jsonObject["mq1"] as String != "") {
                        mq1 = jsonObject["mq1"] as String
                        viewModel.addAnswerToMap("mq1", mq1)
                    }
                    if (jsonObject["mq2"] as String != "") {
                        mq2 = jsonObject["mq1"] as String
                        viewModel.addAnswerToMap("mq2", mq2)
                    }
                    if (jsonObject["fq1"] as String != "") {
                        fq1 = jsonObject["mq1"] as String
                        viewModel.addAnswerToMap("fq1", fq1)
                        //Save answers to file
                        context?.let { viewModel.saveAnswersToFileAndClear(it) }
                    }
                    if (jsonObject["jou"] as String != "") {
                        jou = jsonObject["jou"] as String
                        viewModel.setJournalString(jou)
                        //Save answers to file
                        context?.let { viewModel.saveJournalToFileAndClear(it) }
                    }

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
                            jsonObject["time"] as Long, sessionId, focus, mq1, mq2, fq1, jou))
                }

                activity?.runOnUiThread {
                    for(i in messages) {
                        adapter.addMessage(i)
                        viewModel.addMessage(i)
                    }

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
