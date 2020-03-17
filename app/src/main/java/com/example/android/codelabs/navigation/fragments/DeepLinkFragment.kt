/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.navigation.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.codelabs.navigation.DEEPLINK_ID
import com.example.android.codelabs.navigation.DEEP_LINKS
import com.example.android.codelabs.navigation.MY_ARG
import com.example.android.codelabs.navigation.NAVIGATION_TITLE
import com.example.android.codelabs.navigation.NOTIFICATION_CONTENT
import com.example.android.codelabs.navigation.R
import kotlinx.android.synthetic.main.deeplink_fragment.args_edit_text
import kotlinx.android.synthetic.main.deeplink_fragment.text_deeplink

/**
 * Fragment used to show how to deep link to a destination
 */
class DeepLinkFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.deeplink_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_deeplink.text = arguments?.getString(MY_ARG)

        val notificationButton = view.findViewById<Button>(R.id.send_notification_button)
        notificationButton.setOnClickListener {
            args_edit_text
            val args = Bundle()
            args.putString(MY_ARG, args_edit_text.toString())

            val deeplink = findNavController().createDeepLink()
                    .setDestination(R.id.deeplink_dest)
                    .setArguments(args)
                    .createPendingIntent()

            val notificationManager =
                    context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(NotificationChannel(
                        DEEPLINK_ID, DEEP_LINKS, NotificationManager.IMPORTANCE_HIGH))
            }

            val builder = NotificationCompat.Builder(
                    context!!, DEEPLINK_ID)
                    .setContentTitle(NAVIGATION_TITLE)
                    .setContentText(NOTIFICATION_CONTENT)
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentIntent(deeplink)
                    .setAutoCancel(true)
            notificationManager.notify(0, builder.build())
        }
    }
}