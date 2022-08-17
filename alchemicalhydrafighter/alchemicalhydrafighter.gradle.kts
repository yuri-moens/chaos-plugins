import ProjectVersions.combatHelperVersion

version = "1.0.0"

project.extra["PluginName"] = "Chaos Alchemical Hydra Fighter"
project.extra["PluginDescription"] = "Fights the Alchemical Hydra"

dependencies {
    compileOnly(group = "io.reisub.unethicalite", name = "alchemicalhydra", version = "5.1.1")
    compileOnly(group = "io.reisub.unethicalite", name = "combathelper", version = combatHelperVersion)
}

tasks {
    jar {
        manifest {
            attributes(
                mapOf(
                    "Plugin-Version" to project.version,
                    "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                    "Plugin-Provider" to project.extra["PluginProvider"],
                    "Plugin-Description" to project.extra["PluginDescription"],
                    "Plugin-Dependencies" to
                            arrayOf(
                                nameToId("Chaos Alchemical Hydra"),
                                nameToId("Chaos Combat Helper"),
                                nameToId("Chaos Utils")
                            ).joinToString(),
                    "Plugin-License" to project.extra["PluginLicense"]
                )
            )
        }
    }
}
