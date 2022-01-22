/*
 * Copyright 2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.module.feature.notification

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.fragment.app.Fragment
import app.module.feature.notification.databinding.FragmentNotificationBinding
import app.template.base_android.viewDelegation.viewBinding
import app.template.moduleNotification.Notify
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private val binding by viewBinding(FragmentNotificationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
    }

    private fun initializeViews() {
        binding.notifyBigPicture.setOnClickListener {
            notifyBigPicture()
        }

        binding.notifyBigText.setOnClickListener {
            notifyBigText()
        }

        binding.notifyBubble.setOnClickListener {
            notifyBubble()
        }

        binding.notifyDefault.setOnClickListener {
            notifyDefault()
        }

        binding.notifyDeterminateProgress.setOnClickListener {
            notifyDeterminateProgress()
        }

        binding.notifyMessage.setOnClickListener {
            notifyMessage()
        }

        binding.notifyTextList.setOnClickListener {
            notifyTextList()
        }

        binding.notifyInDeterminateProgress.setOnClickListener {
            notifyIndeterminateProgress()
        }
    }

    fun notifyDefault() {
        Notify.with(context!!)
            .content {
                title = "New dessert menu"
                text = "The Cheesecake Factory has a new dessert for you to try!"
            }
            .stackable {
                key = "test_key"
                summaryContent = "test summary content"
                summaryTitle = { count -> "Summary title" }
                summaryDescription = { count -> count.toString() + " new notifications." }
            }
            .show()
    }

    fun notifyTextList() {
        Notify.with(requireContext())
            .asTextList {
                lines = Arrays.asList("New! Fresh Strawberry Cheesecake.",
                    "New! Salted Caramel Cheesecake.",
                    "New! OREO Dream Dessert.")
                title = "New menu items!"
                text = lines.size.toString() + " new dessert menu items found."
            }
            .show()

    }

    fun notifyBigText() {
        Notify.with(requireContext())
            .asBigText {
                title = "Chocolate brownie sundae"
                text = "Try our newest dessert option!"
                expandedText = "Mouthwatering deliciousness."
                bigText = "Our own Fabulous Godiva Chocolate Brownie, Vanilla Ice Cream, Hot Fudge, Whipped Cream and Toasted Almonds.\n" +
                        "\n" +
                        "Come try this delicious new dessert and get two for the price of one!"
            }
            .show()
    }

    fun notifyBigPicture() {
        Notify.with(requireContext())
            .asBigPicture {
                title = "Chocolate brownie sundae"
                text = "Get a look at this amazing dessert!"
                expandedText = "The delicious brownie sundae now available."
                image = BitmapFactory.decodeResource(resources, R.drawable.chocolate_brownie_sundae)
            }
            .show()
    }

    fun notifyMessage() {
        Notify.with(requireContext())
            .asMessage {
                userDisplayName = "Karn"
                conversationTitle = "Sundae chat"
                messages = Arrays.asList(
                    NotificationCompat.MessagingStyle.Message("Are you guys ready to try the Strawberry sundae?",
                        System.currentTimeMillis() - (6 * 60 * 1000), // 6 Mins ago
                        "Karn"),
                    NotificationCompat.MessagingStyle.Message("Yeah! I've heard great things about this place.",
                        System.currentTimeMillis() - (5 * 60 * 1000), // 5 Mins ago
                        "Nitish"),
                    NotificationCompat.MessagingStyle.Message("What time are you getting there Karn?",
                        System.currentTimeMillis() - (1 * 60 * 1000), // 1 Mins ago
                        "Moez")
                )
            }
            .show()
    }

    fun notifyBubble() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(requireContext(), "Notification Bubbles are only supported on a device running Android Q or later.", Toast.LENGTH_SHORT).show()
            return
        }

        Notify.with(requireContext())
            .content {
                title = "New dessert menu"
                text = "The Cheesecake Factory has a new dessert for you to try!"
            }
            .bubblize {
                // Create bubble intent
                val target = Intent(requireContext(), BubbleActivity::class.java)
                val bubbleIntent = PendingIntent.getActivity(requireContext(), 0, target, 0 /* flags */)

                bubbleIcon = IconCompat.createWithResource(requireContext(), R.drawable.ic_app_icon)
                targetActivity = bubbleIntent
                suppressInitialNotification = true
            }
            .show()
    }

    fun notifyIndeterminateProgress() {

        Notify.with(requireContext())
            .asBigText {
                title = "Uploading files"
                expandedText = "The files are being uploaded!"
                bigText = "Daft Punk - Get Lucky.flac is uploading to server /music/favorites"
            }
            .progress {
                showProgress = true
            }
            .show()
    }

    fun notifyDeterminateProgress() {

        Notify.with(requireContext())
            .asBigText {
                title = "Bitcoin payment processing"
                expandedText = "Your payment was sent to the Bitcoin network"
                bigText = "Your payment #0489 is being confirmed 2/4"
            }
            .progress {
                showProgress = true
                enablePercentage = true
                progressPercent = 30
            }
            .show()
    }
}
