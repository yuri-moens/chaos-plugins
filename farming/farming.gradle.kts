version = "1.5.3"

project.extra["PluginName"] = "Chaos Farming"
project.extra["PluginDescription"] = "It's not much but it's honest work"

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
                                            nameToId("Chaos Utils")
                                    ).joinToString(),
                            "Plugin-License" to project.extra["PluginLicense"]
                    )
            )
        }
    }
}