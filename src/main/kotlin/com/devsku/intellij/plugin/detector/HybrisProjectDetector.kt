package com.devsku.intellij.plugin.detector

import com.intellij.openapi.project.Project
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object HybrisProjectDetector {
    fun isHybrisProject(project: Project): Boolean {
        val path = Paths.get(project.basePath ?: ".")
        return isHybrisRoot(path) || isHybrisRoot(path.resolve("hybris"))
    }

    private fun isHybrisRoot(path: Path): Boolean {
        val bin = path.resolve("bin")
        val config = path.resolve("config")

        return Files.exists(bin) && Files.exists(config)
    }
}