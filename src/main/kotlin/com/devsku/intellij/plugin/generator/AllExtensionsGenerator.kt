package com.devsku.intellij.plugin.generator

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleTypeId
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleOrderEntry
import com.intellij.openapi.roots.ModuleRootManager

class AllExtensionsGenerator {

    fun generate(project: Project): Module {
        val allExtensionsModule = getOrCreateAllExtensionsModule(project)

        addAllProjectModulesAsDependencies(project, allExtensionsModule)

        return allExtensionsModule;
    }

    private fun getOrCreateAllExtensionsModule(project: Project): Module {

        val moduleManager = ModuleManager.getInstance(project)

        moduleManager.modules
            .firstOrNull { it.name == "all_extensions" }
            ?.let { return it }

        val model = moduleManager.modifiableModel

        val modulePath =
            "${project.basePath}/.idea/all_extensions.iml"

        val module =
            model.newModule(modulePath, ModuleTypeId.JAVA_MODULE)

        ApplicationManager.getApplication().runWriteAction {
            model.commit()
        }

        return module
    }

    private fun addAllProjectModulesAsDependencies(
        project: Project,
        allExtensionsModule: Module
    ) {

        val moduleManager = ModuleManager.getInstance(project)

        val rootManager =
            ModuleRootManager.getInstance(allExtensionsModule)

        val model = rootManager.modifiableModel

        // Ensure module inherits the project JDK
        model.inheritSdk()

        // Remove existing module dependencies
        model.orderEntries
            .filterIsInstance<ModuleOrderEntry>()
            .forEach { model.removeOrderEntry(it) }

        moduleManager.modules
            .filter { it.name != "all_extensions" }
            .sortedBy { it.name }
            .forEach { module ->
                model.addModuleOrderEntry(module)
            }

        ApplicationManager.getApplication().runWriteAction {
            model.commit()
        }
    }
}