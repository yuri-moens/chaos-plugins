version = "1.0.8"

project.extra["PluginName"] = "Chaos Barrows"
project.extra["PluginDescription"] = "Automated fratricide"

dependencies {
    compileOnly(project(":combathelper"))
    compileOnly(project(":utils"))
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
                                nameToId("Chaos Combat Helper"),
                                nameToId("Chaos Utils")
                            ).joinToString(),
                    "Plugin-License" to project.extra["PluginLicense"]
                )
            )
        }
    }
}
