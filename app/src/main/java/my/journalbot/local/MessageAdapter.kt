package my.journalbot.local

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import my.journalbot.local.R
import kotlinx.android.synthetic.main.bot_message.view.*
import kotlinx.android.synthetic.main.my_message.view.*
import kotlinx.android.synthetic.main.other_message.view.*

private const val VIEW_TYPE_MY_MESSAGE = 1
private const val VIEW_TYPE_OTHER_MESSAGE = 2
private const val VIEW_TYPE_BOT_MESSAGE = 3

class MessageAdapter (val context: Context) : RecyclerView.Adapter<MessageViewHolder>() {
    private val messages: ArrayList<Message> = ArrayList()

    private var isFirstMessage: Boolean = false

    fun addMessage(message: Message) {
        if (messages.isNotEmpty()) {
            if ((message.message == messages.last().message || messages.last().message == "@bot" + message.message)
                && message.session == messages.last().session) {
                //If the message given to add is the same as the last message already added, skip this
                println("Message Already Added")
            } else {
                messages.add(message)
            }
        } else {
            isFirstMessage = true
            messages.add(message)
        }
        notifyDataSetChanged()

    }

    fun getLastUser(): String {
        if(messages.isEmpty()) {
            return ""
        } else {
            return messages.last().user
        }

    }

    fun updateMessages(newMessages: ArrayList<Message>) {
        messages.clear()
        for(i in newMessages) {
            messages.add(i)
        }
    }

    fun getMessages(): ArrayList<Message> {
        return messages
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return when {
            ChatApp.user == message.user -> {
                VIEW_TYPE_MY_MESSAGE
            }
            ChatApp.botUser == message.user -> {
                VIEW_TYPE_BOT_MESSAGE
            }
            else -> {
                VIEW_TYPE_OTHER_MESSAGE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> {
                MyMessageViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.my_message, parent, false)
                )
            }
            VIEW_TYPE_BOT_MESSAGE -> {
                BotMessageViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.bot_message, parent, false)
                )
            }
            else -> {
                OtherMessageViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.other_message, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.bind(message)
    }

    inner class MyMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtMyMessage
        private var messageView: View = view
        //private var timeText: TextView = view.txtMyMessageTime

        override fun bind(message: Message) {
            if(message.message.startsWith("@bot")) {
                messageText.text = message.message.replace("@bot", "")
            } else {
                messageText.text = message.message
            }
//            if(isFirstMessage || message.message == "Hi" || message.message == "hi") {
//                messageView.visibility = View.GONE
//                isFirstMessage = false
//            }
            //messageText.text = message.message
            //timeText.text = DateUtils.formatDateTime(context, message.time, DateUtils.FORMAT_SHOW_TIME)
        }
    }

    inner class OtherMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtOtherMessage
        private var userText: TextView = view.txtOtherUser
        private var timeText: TextView = view.txtOtherMessageTime

        override fun bind(message: Message) {
            messageText.text = message.message
            userText.text = message.user
            timeText.text = DateUtils.formatDateTime(context, message.time, DateUtils.FORMAT_SHOW_TIME)  //(message.time)
        }
    }

    inner class BotMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtBotMessage
        //private var timeText: TextView = view.txtBotMessageTime

        override fun bind(message: Message) {
            messageText.text = message.message
            //timeText.text = DateUtils.formatDateTime(context, message.time, DateUtils.FORMAT_SHOW_TIME)
        }
    }
}

open class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: Message) {}
}