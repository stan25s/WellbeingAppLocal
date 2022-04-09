package com.example.wellbeingapplocal.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
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
import com.example.wellbeingapplocal.ui.dashboard.DashboardViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    //private val sharedPrefFile = "sharedprefs"

    private lateinit var sessionId: String
    private lateinit var focus: String
    private lateinit var dashboardViewModel : ViewModel

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
    private var fqq : String = ""
    private var fqa : String = ""
    private var jou : String = ""
    private var gratQ : String = ""
    private var gratA : String = ""

    private var jouReceived : Boolean = false

    private var gratReceived : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        dashboardViewModel = DashboardViewModel()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPref = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
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
            fqa = viewModel.checkInData.value?.get("fq1").toString()
        }
        if (!viewModel.checkInData.value?.get("jou").isNullOrEmpty()) {
            jou = viewModel.checkInData.value?.get("jou").toString()
        }

        val prefs: SharedPreferences = context?.let {
            PreferenceManager.getDefaultSharedPreferences(
                it
            )
        }!!

        // Instance field for listener
        val listener = OnSharedPreferenceChangeListener { prefs, key ->
            // Your Implementation
            if (key == "sessionId") {
                sessionId = prefs.getString(key, "").toString()
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

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
        if (viewModel.messages.value?.size == 0) {
            viewModel.clearJournal()
            viewModel.clearAnswerMap()
        }

        //Only use this for testing!! Then delete
        //val sharedPrefs : SharedPreferences = getp
        //user = "testUser"
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            requireContext()
        )
        //val sharedPreferences: SharedPreferences = view.context
            //.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
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
                    gratQ,
                    gratA,
                    fqa
                )
                //TODO: Set-up sessions and send sessionID alongside message content

                if(jouReceived) {
                    //add journal message to viewModel and save it
                    jou = txtMessage.text.toString()
                    viewModel.setJournalString(jou)
                    jouReceived = false
                    viewModel.saveJournalToFile(requireContext())
                }
                if(gratReceived) {
                    //Add gratitude response to viewModel
                    gratA = txtMessage.text.toString()
                    viewModel.addAnswerToMap("gratA", gratA)
                    gratReceived = false
                }

                println(message)
                println("mq1: $mq1")
                println("mq2: $mq2")
                println("fq1: $fqa")
                println("jou: $jou")
                println("gratQ: $gratQ")
                println("gratA: $gratA")
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
                    if (tempString.last() == '}') {
                        val messageCode = tempString.split("{")
                        when (messageCode[1]) {
                            "grat}" -> {
                                //Save gratitude question and set flag to record answer when user
                                //sends message
                                gratQ = messageCode[0]
                                viewModel.addAnswerToMap("gratQ", gratQ)
                                gratReceived = true
                            }
                            "fqq}" -> {
                                fqq = messageCode[0]
                                viewModel.addAnswerToMap("fqQ", fqq)
                            }
                            "jou}" -> jouReceived = true
                            "sjou}" -> {
                                jouReceived = false
                                viewModel.clearJournal()
                            }
                            "sgrat}" -> {
                                gratReceived = false
                                viewModel.removeAnswerFromMap("gratA")
                                viewModel.removeAnswerFromMap("gratQ")
                            }
                            "end}" -> {
                                context?.let { viewModel.saveAnswersToFile(it) }
                                context?.let { viewModel.saveJournalToFile(it) }
                                //dashboardViewModel.readFilesAndUpdate()
                                //Sets a new Session Variable
                                (activity as MainActivity).newSession()
                                //viewModel.clearAnswerMap()
                                //viewModel.clearJournal()
                            }
                        }
                        tempString = messageCode[0]
                    }
                    if (jsonObject["mq1"] as String != "") {
                        mq1 = jsonObject["mq1"] as String
                        viewModel.addAnswerToMap("mq1", mq1)
                    }
                    if (jsonObject["mq2"].toString() != "") {
                        mq2 = jsonObject["mq2"].toString()
                        viewModel.addAnswerToMap("mq2", mq2)
                    }
                    if (jsonObject["fq1"] as String != "") {
                        fqa = jsonObject["fq1"] as String
                        viewModel.addAnswerToMap("fq1", fqa)
                        context?.let { viewModel.saveAnswersToFile(it) }
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
                            jsonObject["time"] as Long, sessionId, focus, mq1, mq2, gratQ, gratA, fqa))
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
