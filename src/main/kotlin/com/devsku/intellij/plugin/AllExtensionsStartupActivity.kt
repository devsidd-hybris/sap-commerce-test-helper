package com.devsku.intellij.plugin

import com.devsku.intellij.plugin.detector.HybrisProjectDetector
import com.devsku.intellij.plugin.generator.AllExtensionsGenerator
import com.intellij.execution.CommonJavaRunConfigurationParameters
import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.configurations.ModuleBasedConfiguration
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class AllExtensionsStartupActivity : StartupActivity {

    override fun runActivity(project: Project) {
        if (!HybrisProjectDetector.isHybrisProject(project)) return

        project.messageBus.connect(project).subscribe(
            ExecutionManager.EXECUTION_TOPIC,
            object : ExecutionListener {

                override fun processStartScheduled(
                    executorId: String,
                    env: ExecutionEnvironment
                ) {

                    val profile = env.runProfile

                    if (profile !is ModuleBasedConfiguration<*, *>) {
                        return
                    }

                    if (!profile.javaClass.name.contains("JUnit")) {
                        return
                    }

                    val moduleManager = ModuleManager.getInstance(project)

                    val allExtensionsModule =
                        moduleManager.findModuleByName("all_extensions")
                            ?: AllExtensionsGenerator().generate(project)

                    profile.configurationModule.module = allExtensionsModule

                    // add VM parameter
                    if (profile is CommonJavaRunConfigurationParameters) {

                        val existing = profile.vmParameters ?: ""

                        val hybrisParam = """
                            --add-opens java.base/java.lang=ALL-UNNAMED
                            --add-opens java.base/java.util=ALL-UNNAMED
                            --add-opens java.base/java.io=ALL-UNNAMED
                            --add-opens java.base/java.lang.reflect=ALL-UNNAMED
                        """.trimIndent()

                        if (!existing.contains(hybrisParam)) {
                            profile.vmParameters = "$existing $hybrisParam".trim()
                        }
                    }
                }
            }
        )
    }
}