package com.example.wellbeingapplocal.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.applozic.mobicomkit.api.account.register.RegistrationResponse
import com.example.wellbeingapplocal.databinding.FragmentHomeBinding
import io.kommunicate.KmConversationBuilder
import io.kommunicate.Kommunicate
import io.kommunicate.callbacks.KMLoginHandler
import io.kommunicate.callbacks.KmCallback
import io.kommunicate.users.KMUser


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val sharedPrefFile = "sharedprefs"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Kommunicate.init(context, "38eac480f499b449606408459aa35b6d0")
//
//        val sharedPreferences: SharedPreferences? =
//            context?.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        val userID = sharedPreferences?.getString("userID", "defaultUser")
//        val userImageLink = sharedPreferences?.getString("avatarURL", "https://i.imgur.com/kEJul3R.png")
//        val prefName = sharedPreferences?.getString("prefName", "You")
//
//        var user = KMUser()
//        user.userId = userID
//        user.password = "password"
//        user.imageLink = userImageLink
//        user.displayName = prefName
//
//        Kommunicate.login(context, user, object : KMLoginHandler {
//            override fun onSuccess(registrationResponse: RegistrationResponse, context: Context) {
//                KmConversationBuilder(context)
//                    .setKmUser(user)
//                    .launchConversation(object : KmCallback {
//                        override fun onSuccess(message: Any) {
//                            Log.d("Conversation", "Success : $message")
//                        }
//
//                        override fun onFailure(error: Any) {
//                            Log.d("Conversation", "Failure : $error")
//                        }
//                    })
//                // You can perform operations such as opening the conversation, creating a new conversation or update user details on success
//            }
//
//            override fun onFailure(
//                registrationResponse: RegistrationResponse,
//                exception: Exception
//            ) {
//                // You can perform actions such as repeating the login call or throw an error message on failure
//            }
//        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}