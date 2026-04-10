package com.devsku.intellij.plugin.actions

import com.devsku.intellij.plugin.AllExtensionsStartupActivity
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class GenerateAllExtensionsModuleAction :
        AnAction("Generate all_extensions module") {

    override fun actionPerformed(event: AnActionEvent) {

        val project: Project = event.project ?: return

        // run your plugin logic
        AllExtensionsStartupActivity().runActivity(project)

        // show notification
        Notifications.Bus.notify(
                Notification(
                        "Hybris Plugin",
                        "Hybris",
                        "all_extensions module generated successfully",
                        NotificationType.INFORMATION
                ),
                project
        )
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabled = event.project != null
    }
}